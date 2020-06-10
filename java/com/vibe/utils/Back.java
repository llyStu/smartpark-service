package com.vibe.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class Back {

	/**
	 * 数据库表备份
	 * 
	 * @throws Exception
	 */
	public static void tableBackup(String dbUser, String dbPass, String dbHost, String dbPort, String dbName,
			String savePath, String tableName) throws Exception {

		Runtime runtime = Runtime.getRuntime();
		// -u后面是用户名，-p是密码-p后面最好不要有空格，-family是数据库的名字
		Process process = runtime.exec("mysqldump -h " + dbHost + " -P " + dbPort + " -u " + dbUser + " -p" + dbPass
				+ " " + dbName + " " + tableName);
		InputStream inputStream = process.getInputStream();// 得到输入流，写成.sql文件
		InputStreamReader reader = new InputStreamReader(inputStream);
		BufferedReader br = new BufferedReader(reader);
		String s = null;
		StringBuffer sb = new StringBuffer();
		while ((s = br.readLine()) != null) {
			sb.append(s + "\r\n");
		}
		s = sb.toString();
		File file = new File(savePath);
		file.getParentFile().mkdirs();
		FileOutputStream fileOutputStream = new FileOutputStream(file);
		fileOutputStream.write(s.getBytes());
		fileOutputStream.close();
		br.close();
		reader.close();
		inputStream.close();
	}

	/**
	 * 备份数据库
	 * 
	 * @param savePath
	 * @throws Exception
	 */
	public static void dbBackup(String dbUser, String dbPass, String dbHost, String dbPort, String dbName,
			String savePath) throws Exception {
		BufferedReader br = null;
        BufferedWriter bw = null;

		Runtime runtime = Runtime.getRuntime();
		// -u后面是用户名，-p是密码-p后面最好不要有空格，-family是数据库的名字
		Process process = runtime
				.exec("mysqldump -h " + dbHost + " -P " + dbPort + " -u " + dbUser + " -p" + dbPass + " " + dbName);
		InputStream inputStream = process.getInputStream();// 得到输入流，写成.sql文件
		InputStreamReader reader = new InputStreamReader(inputStream,"utf-8");
		br = new BufferedReader(reader);
		String s = null;
		StringBuffer sb = new StringBuffer();  
		while ((s = br.readLine()) != null) {
			sb.append(s + "\r\n");
		}
		s = sb.toString();
		File file = new File(savePath);
		file.getParentFile().mkdirs();
		FileOutputStream fos = new FileOutputStream(file);
		OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");
		bw = new BufferedWriter(osw);
        bw.write(s);
        bw.flush();
		br.close();
		bw.close();
		reader.close();
		inputStream.close();
	}

	/**
	 * 执行sql文件
	 * 
	 * @param savePath
	 * @throws Exception
	 */
	public static void dbRecover(String dbUser, String dbPass, String dbHost, String dbPort, String dbName,
			String savePath) throws Exception {
		// 获取操作数据库的相关属性
		BufferedWriter bw = null;
		Runtime runtime = Runtime.getRuntime();
		Process process = runtime.exec("mysql -h" + dbHost + " -P " + dbPort + " -u " + dbUser + " -p" + dbPass
				+ " --default-character-set=utf8 " + dbName);//" --default-character-set=utf8 "
		OutputStream outputStream = process.getOutputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(savePath),"utf-8"));
		String str = null;
		StringBuffer sb = new StringBuffer();
		while ((str = br.readLine()) != null) {
			sb.append(str + "\r\n");
		}
		str = sb.toString();
		OutputStreamWriter writer = new OutputStreamWriter(outputStream,"utf-8");
		bw = new BufferedWriter(writer);
		bw.write(str);
        bw.flush();
		outputStream.close();
		br.close();
		writer.close();
	}

	public static void main(String[] args) {
		/*try {
			dbBackup("root", "123456", "localhost", "3306", "db_vibe_monitor",
					"D://back.sql");
			System.out.println("完成！");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		// "D:\\Backup\\20190226-174529\\sql\\db_vibe_monitor.sql"
		 try { 
				 dbRecover("vibeuser", "I*O(P)", "127.0.0.1","3306", "db_vibe_monitor",
						 "D:/backup/20190227-094938/sql/db_vibe_monitor.sql"); System.out.println("完成！"); 
						
		  } catch (Exception e) { 
			  e.printStackTrace(); 
			  
		  }
		 
	}

}
