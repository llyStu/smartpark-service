package com.vibe.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.vibe.config.ConfUtil;
import org.springframework.web.multipart.MultipartFile;


/*
 * 需求：1、前台上传图片，后台将它保存至服务器本地文件夹并返回文件名
 * 	结果类型：空串 或者 文件名组成的串
 * 		2、photo属性在数据库中以xxx.jpg,yyy.jpg保存，向前台回显时需要拿到
 * 			绝对路径的list
 * 	结果类型：空集合 或者 文件绝对路径的集合
 * 		3、前台的修改页面要删除图片、上传新图片
 * 			传来要删除图片的url，后台要把原来的图片路径改变，拼接成新的串保存
 * */
public class PathTool {

	public static String getRelativePath(MultipartFile file, HttpServletRequest request) {

		String relative_path = "";

		// 获取上传路径 webapp/upload
//		String uploadPath = request.getSession().getServletContext().getRealPath("/upload/");
//		String uploadPath = "C:\\img_upload_test\\";
//		String uploadPath = "/home/file/";
		String uploadPath = ConfUtil.getPushSelectPrefix();
		if (!(new File(uploadPath)).exists()) {
			(new File(uploadPath)).mkdirs();
		}
		if (file.getSize() > 1) {

			String originalName = file.getOriginalFilename();
			String fileName = UUID.randomUUID() + originalName.substring(originalName.lastIndexOf("."));
			File newFile = new File(uploadPath + fileName);
			try {
				// 上传文件转存
				file.transferTo(newFile);
				relative_path = "/image/"+ fileName;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return relative_path;
	}

	public static String getRelativePath(MultipartFile file, HttpServletRequest request,String url) {

		String relative_path = "";

		// 获取上传路径 webapp/upload
		String uploadPath = request.getSession().getServletContext().getRealPath(url);
		//System.out.println(uploadPath);
		if (!(new File(uploadPath)).exists()) {
			(new File(uploadPath)).mkdirs();
		}
		if (file.getSize() > 1) {

			String originalName = file.getOriginalFilename();
			String fileName = UUID.randomUUID() + originalName.substring(originalName.lastIndexOf("."));
			File newFile = new File(uploadPath + fileName);
			try {
				// 上传文件转存
				file.transferTo(newFile);
				relative_path = fileName;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return relative_path;
	}

	public static String getRelativePath(MultipartFile[] file, HttpServletRequest request) {

		StringBuffer relative_path = new StringBuffer("");

		if (file.length != 0 && file[0].getSize() > 1) {

			for (int i = 0; i < file.length; i++) {
				String path = getRelativePath(file[i], request);
				relative_path.append(path);
				if (i < file.length - 1) {
					relative_path.append(",");
				}
			}
		}
		return relative_path.toString();
	}

	public static String getAbsolutePath(String fileName, HttpServletRequest request) {

		// 获取服务器ip地址，端口号等
		String pathPrefix = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath() + "/upload/";

		return pathPrefix + fileName;
	}

	/*参数类型：由,分隔的地址字符串
	 * 结果类型：绝对路径的集合
	 * */
	public static List<String> getAbsolutePathList(String fileNameColl, HttpServletRequest request) {

		List<String> list = new ArrayList<String>();
		if(fileNameColl!=null){
			String[] path = fileNameColl.split(",");

			for (String p : path) {
				String absolute_path = getAbsolutePath(p, request);
				list.add(absolute_path);
			}
		}
		return list;
	}

	public static String alterFile(String delPath, // 要删除的文件的url
								   MultipartFile[] newFile, // 新添加的多个文件
								   String fileNames, // 原来的文件名串
								   HttpServletRequest request) {

		StringBuffer pathSB = new StringBuffer("");

		// 前台传来delPath格式为"[XXXX,XXXX]"
		if (delPath!=null&&delPath.length() > 0) {


			String[] del_path = (delPath.substring(1, delPath.length() - 1)).split(",");

			List<String> temp = new ArrayList<String>();
			List<String> list = new ArrayList<String>();

			String[] path = fileNames.split(",");

			for (String p : path) {
				list.add(p);
			}
			for (String del : del_path) {
				for (String p : path) {
					if (del.contains(p)) {
						temp.add(p);
					}
				}
			}
			list.removeAll(temp);
			for (int i = 0; i < list.size(); i++) {
				pathSB.append(list.get(i));
				pathSB.append(",");
			}
		}

		// 对前台传来的照片的保存和对象属性名的更改
		if (newFile!=null&&newFile.length!=0&&newFile[0].getSize() > 0) {

			for (int i = 0; i < newFile.length; i++) {
				String relative_path = getRelativePath(newFile[i], request);
				pathSB.append(relative_path);
				if (i < newFile.length - 1) {
					pathSB.append(",");
				}
			}
		}

		if (pathSB.length() > 1 && pathSB.lastIndexOf(",") == pathSB.length() - 1) {
			pathSB = new StringBuffer(pathSB.substring(0, pathSB.length() - 1));
		}
		return pathSB.toString();
	}

	//接收遗产评估doc，保存之，并返回相对路径(时间毫秒值)
	public static String getDocRelativePath(MultipartFile file,
											HttpServletRequest request){

		String relative_path="";
		if(file!=null&&file.getSize()>0){

			//为文件重命名
			//获取当前系统时间毫秒值作为文件的名字relative_path
			Calendar calendar = Calendar.getInstance();
			String millis = calendar.getTimeInMillis() + "";

			String dir = request.getSession().getServletContext().getRealPath("/doc/");

			//创建上传路径
			if(!new File(dir).exists()){
				new File(dir).mkdirs();
			}
			//截取文件类型串，如.txt
			String originName = file.getOriginalFilename();

			String fileName = millis + originName.substring(originName.lastIndexOf("."));

			File desFile = new File(dir + fileName);
			try {

				file.transferTo(desFile);
				relative_path = fileName;
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
		}
		return 	relative_path;

	}

	// 根据名字中的毫秒值获得上传时间
	public static String getRelativePathAsDate(String fileName){
		System.out.println(fileName);
		String 	millis = fileName.substring(0, fileName.lastIndexOf("."));
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTimeInMillis(Long.parseLong(millis));
		Date date = calendar2.getTime();
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String dateFormat = formater.format(date);
		return dateFormat;

	}

	public static String getDocAbsolutePath(String fileName, HttpServletRequest request) {

		// 获取服务器ip地址，端口号等
		String pathPrefix = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath() + "/doc/";

		return pathPrefix + fileName;
	}

	public static String getFileName(String fileName) {
		if(fileName != null && !"".equals(fileName) && fileName.contains(".")){
			int lastIndexOf = fileName.lastIndexOf(".");
			return fileName.substring(0,lastIndexOf);
		}
		return null;
	}
}
