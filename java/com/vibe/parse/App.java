package com.vibe.parse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {
	
	
	public static boolean TEST = false;

	
	/*public static final String INPUT_FILE_PATH = "D:\\work\\excelparse\\input\\b3点表.xlsx";
	public static final String OUPUT_FILE_PATH = "D:\\work\\excelparse\\output\\";*/
	
	public static final String GENERIC_DEVICE_TYPE = "GenericDevice";
	public static String CAMERA_DEVICE_TYPE = "DFWLCamera";
	
	public static final String AI = "AI";
	public static final String AO = "AO";
	public static final String DI = "DI";
	public static final String DO = "DO";
	
	public static final String PROBE_TABLE_NAME = "t_probe";
	public static final String CONTROL_TABLE_NAME = "t_control";
	
	public static final String BACNET_AI_TYPE = "BACnetFloatProbe";
	public static final String BACNET_AO_TYPE = "BACnetFloatControl";
	public static final String BACNET_DI_TYPE = "BACnetBoolProbe";
	public static final String BACNET_DO_TYPE = "BACnetBoolControl";
	
	public static final String OPC_AI_TYPE = "OpcUtgardFloatProbe";
	public static final String OPC_AO_TYPE = "OpcUtgardFloatControl";
	public static final String OPC_DI_TYPE = "OpcUtgardBoolProbe";
	public static final String OPC_DO_TYPE = "OpcUtgardBoolControl";

	
	public static final String AI_TYPE_TEST = "SimulateFloat";
	public static final String AO_TYPE_TEST = "SimulateFloat";
	public static final String DI_TYPE_TEST = "SimulateBool";
	public static final String DO_TYPE_TEST = "SimulateBool";
	
	public static final String IEC104_AI_TYPE = "IEC104FloatProbe";
	public static final String IEC104_AO_TYPE = "IEC104FloatControl";
	public static final String IEC104_DI_TYPE = "IEC104BoolProbe";
	public static final String IEC104_DO_TYPE = "IEC104BoolControl";
	
	public static final String ATZGB_AI_TYPE = "AtzgbProbe";
	public static final String ATZGB_DI_TYPE = "AtzgbLSJCProbe";

	public static BacnetServiceParser bacnetServiceParser;
	public static OpcServiceParser opcServiceParser;
	public static ModbusServiceParser modbusServiceParser;
	public static IEC104ServiceParser iec104ServiceParser;
	public static AtzgbServiceParser atzgbServiceParser;
	
	public static final String CONFIG_FILE_NAME = "config.txt";
	public static final String EXCEL_LIST_FILE_NAME = "excelList.txt";
	public static final String CONFIG_KEY_SPLIT = "=";
	public static final String EXCEL_FILE_PATH = "excel\\";
	
	private static final String kongjian = "空间";
	private static final String fenlei = "分类";
	private static final String shebei = "设备";
	private static final String fuwu = "服务";
	private static final String bacnet = "Bacnet协议";
	private static final String bacnetServer = "Bacnet服务";
	private static final String opc = "Opc协议";
	private static final String opcServer = "Opc服务";
	private static final String modbus = "Modbus协议";
	private static final String modbusServer = "Modbus服务";
	private static final String simulate = "模拟";
	private static final String camera = "摄像机";
	private static final String yuntianchuang = "云天创协议";
	private static final String yuntianchuangServer = "云天创服务";
	private static final String iec104 = "104协议";
	private static final String iec104Server = "104服务";
	
    public static void main( String[] args ) throws Exception{
    	
    	BufferedReader br = null;
    	BufferedReader br1 = null;
    	try {
    		File directory = new File("");
        	File configFile = new File(directory, CONFIG_FILE_NAME);
        	if(configFile.exists()){
        		br = new BufferedReader(new FileReader(configFile));
            	String testStr = br.readLine();
            	String cameraTypeStr = br.readLine();
            	String test = testStr.split(CONFIG_KEY_SPLIT)[1];
            	String cameraType = cameraTypeStr.split(CONFIG_KEY_SPLIT)[1];
            	CAMERA_DEVICE_TYPE = cameraType;
            	if("true".equals(test)){
            		App.TEST = true;
            	}
        	}	
        	File excelFileList = new File(directory, EXCEL_LIST_FILE_NAME);
        	br1 = new BufferedReader(new FileReader(excelFileList));
        	String str = null;
        	Map<String, ExcelSheetPO> dataMap = new HashMap<>();
        	while((str = br1.readLine()) != null) {
        		File excelFile = new File(directory, EXCEL_FILE_PATH + str);
        		ExcelSheetPO data = ExcelUtil.readExcel(excelFile.getAbsolutePath()).get(0);
        		dataMap.put(str, data);
        	}
        	List<String> hasDo = new ArrayList<>();
        	
        	for(String key:dataMap.keySet()){
        		if(kongjian.equals(key)){
        			parseExcel(key,dataMap.get(key));
        			hasDo.add(key);
        		}
        	}
        	for(String key:dataMap.keySet()){
        		if(fenlei.equals(key)){
        			parseExcel(key,dataMap.get(key));
        			hasDo.add(key);
        		}
        	}
        	for(String key:dataMap.keySet()){
        		if(shebei.equals(key)){
        			parseExcel(key,dataMap.get(key));
        			hasDo.add(key);
        		}
        	}
        	for(String key:dataMap.keySet()){
        		if(key.contains(fuwu)){
        			parseExcel(key,dataMap.get(key));
        			hasDo.add(key);
        		}
        	}
        	List<String> keys = new ArrayList<>(dataMap.keySet());
        	keys.removeAll(hasDo);
        	for (String key : keys) {
        		parseExcel(key,dataMap.get(key));
			}
        	
        	
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			if(br != null){
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(br1 != null){
				try {
					br1.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
    	
    }
    
    private static void parseExcel(String name,ExcelSheetPO data) throws Exception {
		switch (name) {
		case kongjian:
			new SpaceParser().parse(data);
			break;
		case fenlei:
			new CodeParser().parse(data);
			break;
		case shebei:
			new GenericDeviceParser().parse(data);
			break;
		case bacnet:
			new BacnetMonitorParser().parse(data);
			break;
		case bacnetServer:
			if(!TEST){
	    		bacnetServiceParser = new BacnetServiceParser();
	    		bacnetServiceParser.parse(data);
	    	}
			break;
		case opc:
			new OpcMonitorParser().parse(data);
			break;
		case opcServer:
			if(!TEST){
	    		opcServiceParser = new OpcServiceParser();
	    		opcServiceParser.parse(data);
	    	}
			break;
		case modbus:
			new ModbusMonitorParser().parse(data);
			break;
		case modbusServer:
			if(!TEST){
	    		modbusServiceParser = new ModbusServiceParser();
	    		modbusServiceParser.parse(data);
	    	}
			break;
		case camera:
			new CameraDeviceParser().parse(data);
			break;
		case simulate:
			new SimulateMonitorParser().parse(data);
			break;
		case iec104Server:
			if(!TEST){
	    		iec104ServiceParser = new IEC104ServiceParser();
	    		iec104ServiceParser.parse(data);
	    	}
			break;
		case iec104:
			new IEC104MonitorParser().parse(data);
			break;
		case yuntianchuangServer:
			if(!TEST){
	    		atzgbServiceParser = new AtzgbServiceParser();
	    		atzgbServiceParser.parse(data);
	    	}
			break;
		case yuntianchuang:
			new AtzgbMonitorParser().parse(data);
			break;
		}
	}
}
