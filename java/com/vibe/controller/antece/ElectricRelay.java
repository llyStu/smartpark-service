package com.vibe.controller.antece;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.Executors;

import com.fazecast.jSerialComm.SerialPort;

public class ElectricRelay implements AutoCloseable {
	private SerialPort port;
	private ByteBuffer buf = ByteBuffer.allocate(1024);
	
	public static ElectricRelay getInstance(String portDescriptor) {
		try {
			return new ElectricRelay(portDescriptor);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ElectricRelay(String portDescriptor) throws Exception {
		port = SerialPort.getCommPort(portDescriptor);
		port.setComPortParameters(9600, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
		port.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
		if (!port.openPort()) {
			throw new Exception("服务器打开错误");
		}
	}

	static String read(InputStream in, ByteBuffer buf) throws EOFException, IOException {
		int result;
		while ((result = in.read()) != '~') {
			if (result == -1) throw new EOFException();
			buf.put((byte)result);
		}
		buf.put((byte)'~');
		String ret = new String(buf.array(), 0, buf.position());
		buf.clear();
		return ret;
	}

	/**
	 * port.setComPortParameters(9600, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
	 * 
	 * @param newBaudRate
	 * @param newDataBits
	 * @param newStopBits
	 * @param newParity
	 */
	public void setComPortParameters(int newBaudRate, int newDataBits, int newStopBits, int newParity) {
		port.setComPortParameters(newBaudRate, newDataBits, newStopBits, newParity);
	}
	
	synchronized String write(String req) throws IOException {
		port.getOutputStream().write(req.getBytes());
		return read(port.getInputStream(), this.buf);
	}
	
	

	
	public boolean updateAddress(Integer address, int newAddress) throws IOException {
		assert address == null || address >= 0 && address <= 255;
		assert newAddress >= 0 && newAddress <= 255;
		
		if (address == null) address = 254;
		
		StringBuilder buf = new StringBuilder()
				.append('!')
				.append(address)
				.append("$A")
				.append(newAddress)
				.append('~');
		
		String response = this.write(buf.toString());
		int begin = assertResponse(response, address);
		return begin > 0;
	}
	
	public boolean updateBaudRate(Integer address, int newBaudRate) throws IOException {
		assert address == null || address >= 0 && address <= 255;
		assert newBaudRate >= 1200 && newBaudRate <= 230400;
		
		if (address == null) address = 254;
		
		StringBuilder buf = new StringBuilder()
				.append('!')
				.append(address)
				.append("$P")
				.append(newBaudRate)
				.append('~');
		
		String response = this.write(buf.toString());
		int begin = assertResponse(response, address);
		return begin > 0;
	}
	
	public String[] queryType(Integer address) throws IOException {
		assert address == null || address >= 0 && address <= 255;
		
		if (address == null) address = 254;
		
		StringBuilder buf = new StringBuilder()
				.append('!')
				.append(address)
				.append("?Q")
				.append('~');
		
		String response = this.write(buf.toString());
		int begin = this.assertResponse(response, address);
		
		return response.substring(begin, response.length() - 1).split(":", 2);
	}
	
	public static enum InfoType {
		MODULE_NAME('M'), VERSION('V'), COMPANY_NAME('P'), PRODUCT_NAME('D'), SERIAL_NUMBER('S');
		char str;
		private InfoType(char str) {
			this.str = str;
		}
	}
	public String queryInfo(Integer address, InfoType type) throws IOException {
		assert address == null || address >= 0 && address <= 255;
		
		if (address == null) address = 254;
		
		StringBuilder buf = new StringBuilder()
				.append('!')
				.append(address)
				.append("?F")
				.append(type.str)
				.append('~');
		
		String response = this.write(buf.toString());
		int begin = this.assertResponse(response, address);
		
		return response.substring(begin, response.length() - 1);
	}
	
	public String queryState(Integer address) throws IOException {
		assert address == null || address >= 0 && address <= 255;
		
		if (address == null) address = 254;

		StringBuilder buf = new StringBuilder()
				.append('!')
				.append(address)
				.append("QU");
		buf.append('~');
		
		String response = write(buf.toString());
		int begin = assertResponse(response, address);
		return response.substring(begin, response.length() - 1);
	}
	
	private int assertResponse(String resp, int address) throws RuntimeException {
		String strAddress = ""+ address;
		int ret = 1 + strAddress.length();
		assert resp.charAt(0) == '!'
				&& resp.charAt(resp.length() - 1) == '~'
				&& strAddress.equals(resp.substring(1, ret));
		
		switch (resp.charAt(ret)) {
		case 'O':
			return ret + 1;
		case 'E':
			throw new IllegalArgumentException("参数错误");
		case 'U':
			throw new UnsupportedOperationException("未知命令");
		case 'D':
			throw new IllegalArgumentException("命令不完整");
		default:
			throw new RuntimeException("未知格式的返回");
		}
	}
	
	public boolean operate(boolean open, Integer address,
			Integer interval, Boolean intervalUnit,
			Integer delay, Boolean delayUnit,
			Boolean ignoreSync, Boolean parallel,
			int... channels) throws IllegalArgumentException, UnsupportedOperationException, IOException {
		assert address == null || address >= 0 && address <= 255;
		assert channels != null && channels.length > 0 && Arrays.stream(channels).allMatch(v -> v > 0 && v < 9);
		
		if (address == null) address = 254;
		
		StringBuilder buf = new StringBuilder()
				.append('!')
				.append(address)
				.append(open ? "ON" : "OF");
		for (int chan : channels) {
			buf.append(chan);
		}
		if (interval != null) {
			buf.append('T');
			if (intervalUnit == null || !intervalUnit) buf.append(0);
			buf.append(interval);
		}
		if (delay != null) {
			buf.append('D');
			if (delayUnit == null || !delayUnit) buf.append(0);
			buf.append(delay);
		}
		if (ignoreSync != null && ignoreSync) buf.append('I');
		if (parallel != null && parallel) buf.append('M');
		buf.append('~');
		
		String response = this.write(buf.toString());
		return assertResponse(response, address) > 0;
	}

	@Override
	public void close() throws Exception {
		try {
			this.port.getInputStream().close();
		} catch (Exception e) {
		}
		try {
			this.port.getOutputStream().close();
		} catch (Exception e) {
		}
		try {
			port.closePort();
		} catch (Exception e) {
		}
	}

	public static void main(String[] args) throws Exception {
		try (ElectricRelay server = new ElectricRelay("COM3")) {
			boolean queryState = server.operate(false, 1, 5, null, 5, null, null, null, 1);
			System.out.println(queryState);
			
			
//			if (!server.operate(true, null, 5, null, 5, false, null, null, 1, 2, 3, 4, 5, 6, 7, 8)) throw new Exception("协议错误");
//			
//			if (!"10000110".equals(server.queryState(null))) throw new Exception("协议错误");
//			
//			if (!"1.01.100.001187(115F)".equals(server.queryInfo(null, InfoType.VERSION))) throw new Exception("协议错误");
//			
//			String[] queryType = server.queryType(null);
//			if (!(queryType.length == 2 && "0x10".equals(queryType[0]) && "8 路 16A 独立继电器".equals(queryType[1]))) throw new Exception("协议错误");
//			
//			if (!server.updateBaudRate(null, 115200)) throw new Exception("协议错误");
//			
//			if (!server.updateAddress(null, 3)) throw new Exception("协议错误");
			
			System.out.println("lol");
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}
	

	public static void main2(String[] args) throws Exception {
		Executors.newSingleThreadExecutor().execute(() -> {
			SerialPort port = SerialPort.getCommPort("COM40");
			try {
				port.setComPortParameters(9600, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
				port.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 500, 500);
				if (!port.openPort()) throw new Exception("qs");
				
				ByteBuffer buf = ByteBuffer.allocate(1024);
				try (InputStream in = port.getInputStream();
						OutputStream out = port.getOutputStream()) {
					
					if (!"!254ON12345678T05D05~".equals(read(in, buf))) throw new Exception("协议错误");
					out.write("!254O~".getBytes());
					
					if (!"!254QU~".equals(read(in, buf))) throw new Exception("协议错误");
					out.write("!254O10000110~".getBytes());
					
					if (!"!254?FV~".equals(read(in, buf))) throw new Exception("协议错误");
					out.write("!254O1.01.100.001187(115F)~".getBytes());
					
					if (!"!254?Q~".equals(read(in, buf))) throw new Exception("协议错误");
					out.write("!254O0x10:8 路 16A 独立继电器~".getBytes());
					
					if (!"!254$P115200~".equals(read(in, buf))) throw new Exception("协议错误");
					out.write("!254O~".getBytes());
					
					if (!"!254$A3~".equals(read(in, buf))) throw new Exception("协议错误");
					out.write("!254O~".getBytes());
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (port != null) port.closePort();
				System.exit(0);
			}
		});
		
		try (ElectricRelay server = new ElectricRelay("COM30")) {
			if (!server.operate(true, null, 5, null, 5, false, null, null, 1, 2, 3, 4, 5, 6, 7, 8)) throw new Exception("协议错误");
			
			if (!"10000110".equals(server.queryState(null))) throw new Exception("协议错误");
			
			if (!"1.01.100.001187(115F)".equals(server.queryInfo(null, InfoType.VERSION))) throw new Exception("协议错误");
			
			String[] queryType = server.queryType(null);
			if (!(queryType.length == 2 && "0x10".equals(queryType[0]) && "8 路 16A 独立继电器".equals(queryType[1]))) throw new Exception("协议错误");
			
			if (!server.updateBaudRate(null, 115200)) throw new Exception("协议错误");
			
			if (!server.updateAddress(null, 3)) throw new Exception("协议错误");
			
			System.out.println("lol");
		} catch (Exception e2) {
			e2.printStackTrace();
		} finally {
			System.exit(0);
		}
	}
}
