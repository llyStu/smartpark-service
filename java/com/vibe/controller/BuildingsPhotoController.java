package com.vibe.controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.github.pagehelper.PageHelper;
import com.vibe.pojo.BuildingsPhoto;
import com.vibe.service.scene.BuildingsPhotoService;
import com.vibe.util.Msg;
import com.vibe.util.PathTool;

@Controller
public class BuildingsPhotoController {

	@Autowired
	private BuildingsPhotoService buildingsPhotoService;

// list
	// 点击列表按钮，跳转至listBuildingsPhoto.jsp页面,实际完成方法在PageController.java中
	@RequestMapping("/to_listBuildingsPhoto")
	public String to_listBuildingsPhoto() {
		return "polling/buildingsPhoto/listBuildingsPhoto";
	}

// query
	// 点击详细按钮，跳转至detailBuildingsPhoto.jsp页面
	@RequestMapping("/query_BuildingsPhoto")
	public String to_detailBuildingsPhoto(int id, HttpServletRequest request) {
		BuildingsPhoto buildingsPhoto = buildingsPhotoService.queryBuildingsPhoto(id);
		//设置图片属性
		/*String pathPrefix = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() 
		+ request.getContextPath() + "/upload/";
		String photos = buildingsPhoto.getPhoto();
		List<String> list = new ArrayList<String>();
		String[] path = photos.split(",");
		for(int i=0;i<path.length;i++){
			list.add(pathPrefix + path[i]);
		}*/
		List<String> list = PathTool.getAbsolutePathList(buildingsPhoto.getPhoto(), request);
		buildingsPhoto.setPhotos(list);
		
		request.setAttribute("buildingsPhoto", buildingsPhoto);		
		return "polling/buildingsPhoto/detailBuildingsPhoto";
	}

// add
	// 点击增加，跳转至addBuildingsPhoto.jsp
	@RequestMapping("/add_BuildingsPhoto")
	public String addBuildingsPhoto() {
		return "polling/buildingsPhoto/addBuildingsPhoto";
	}

	@RequestMapping(value = "/insertBuildingsPhoto", method = RequestMethod.POST)
	public String insertBuildingsPhoto(BuildingsPhoto buildingsPhoto,
			@RequestParam(required = false) MultipartFile[] photoFile, HttpServletRequest request)
			throws IllegalStateException, IOException {

		/*StringBuffer pics = new StringBuffer("");
		List<String> list = new ArrayList<String>();
		String pathPrefix = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() 
		+ request.getContextPath() + "/upload/";
		for (int i = 0; i < photoFile.length; i++) {
			String relativePath = this.getRelativePath(photoFile[i], request);
			list.add(pathPrefix + relativePath);
			pics.append(relativePath);
			if (i < photoFile.length - 1) {
				pics.append(",");
			}
		}
		buildingsPhoto.setPhotos(list);
		System.out.println(buildingsPhoto.getPhotos());
		buildingsPhoto.setPhoto(pics.toString());
		System.out.println(buildingsPhoto.getPhoto());*/
		String pics = PathTool.getRelativePath(photoFile, request);
		buildingsPhoto.setPhoto(pics);
		// 以上，把用户录入的数据都封装到了对象中，还缺少需要系统提供的属性数值
		// buildingsPhoto.setId(id);id是自增字段，不用系统插入
		buildingsPhoto.setDate(LocalDate.now().toString());
		buildingsPhotoService.insertBuildingsPhoto(buildingsPhoto);

		return "polling/buildingsPhoto/listBuildingsPhoto";
	}

	// 截取上传图片后缀名并加上UUID
	public String subPath(String originalName) {

		String path = originalName.substring(originalName.lastIndexOf("."));
		return UUID.randomUUID() + path;

	}

	// 将上传的文件保存在upload夹下并返回相对路径
	public String getRelativePath(MultipartFile file, HttpServletRequest request)
			throws IllegalStateException, IOException {
		String relativePath = "";
		if (file != null) {
			String uploadPath = request.getSession().getServletContext().getRealPath("/upload/");
			String photo_absolutePath = this.subPath(file.getOriginalFilename());
			if (!(new File(uploadPath)).exists()) {
				(new File(uploadPath)).mkdirs();
			}
			File newPhotoFile = new File(uploadPath + photo_absolutePath);
			file.transferTo(newPhotoFile);
			relativePath =  photo_absolutePath;
		}
		return relativePath;
	}

// delete
	@RequestMapping("/delete_BuildingsPhoto")
	public String deleteBuildingsPhoto(String ids) {
		if (ids.contains(",")) {
			String[] ids_arr = ids.split(",");
			for (String id : ids_arr) {
				buildingsPhotoService.deleteBuildingsPhoto(Integer.parseInt(id));
			}
			return "polling/buildingsPhoto/listBuildingsPhoto";
		} else {
			buildingsPhotoService.deleteBuildingsPhoto(Integer.parseInt(ids));
			return "polling/buildingsPhoto/listBuildingsPhoto";
		}
	}
	//响应界面删除请求，不跳转页面
	@RequestMapping("/delete2_BuildingsPhoto")
	@ResponseBody
	public String deleteBuildingsPhoto2(String ids) {	
		
		buildingsPhotoService.deleteBuildingsPhoto(Integer.parseInt(ids));
		return"success";
	}
	

// update
	@RequestMapping("/edit_BuildingsPhoto")
	public String editBuildingsPhoto(int id, 					
					HttpServletRequest request
					) {
		BuildingsPhoto buildingsPhoto = buildingsPhotoService.queryBuildingsPhoto(id);
	
		/*//设置图片属性
				String pathPrefix = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() 
				+ request.getContextPath() + "/upload/";
				String photos = buildingsPhoto.getPhoto();
				List<String> list = new ArrayList<String>();
				String[] path = photos.split(",");
				for(int i=0;i<path.length;i++){
					list.add(pathPrefix + path[i]);
				}
				buildingsPhoto.setPhotos(list);*/
		String fileNames = buildingsPhoto.getPhoto();
		List<String> list = PathTool.getAbsolutePathList(fileNames, request);
		buildingsPhoto.setPhotos(list);
		request.setAttribute("buildingsPhoto", buildingsPhoto);
		return "polling/buildingsPhoto/editBuildingsPhoto";
	}

	@RequestMapping(value = "/update_BuildingsPhoto", method = RequestMethod.POST)
	public String updateBuildingsPhoto(BuildingsPhoto buildingsPhoto,
			@RequestParam(required = false)String delphoto,
			@RequestParam(required = false) MultipartFile[] photoFile, 
			HttpServletRequest request)
			throws IllegalStateException, IOException {
		
//		StringBuffer pics = new StringBuffer("");
//		List<String> list = new ArrayList<String>();
		//System.out.println(photos[0].getSize());
		//photos有一个元素，为0
		/*if(photoFile[0].getSize()>0){
		for (int i = 0; i < photoFile.length; i++) {
			
			String absolutePath = this.getRelativePath(photoFile[i], request);
			list.add(absolutePath);
			pics.append(absolutePath);
			if (i < photoFile.length - 1) {
				pics.append(",");
			}
		*/
		//System.out.println(list);
//		buildingsPhoto.setPhotos(list);				
//		}
//		}
		BuildingsPhoto buildingsPhoto2 = buildingsPhotoService.queryBuildingsPhoto(buildingsPhoto.getId());
		String fileNames = buildingsPhoto2.getPhoto();
		String pics = PathTool.alterFile(delphoto, photoFile, fileNames, request);
		buildingsPhoto.setPhoto(pics);
		buildingsPhotoService.updateBuildingsPhoto(buildingsPhoto);
		//System.out.println(buildingsPhoto.getPhotos());
		return "polling/buildingsPhoto/listBuildingsPhoto";
	}

//android_list
	@RequestMapping("/android_listBuildingsPhoto")
	@ResponseBody
	public List<BuildingsPhoto> pageDiv(
			@RequestParam("pageNum") int pageNum,
			@RequestParam("pageCount") int pageCount,
			HttpServletRequest request){
			PageHelper.startPage(pageNum, pageCount);	
			List<BuildingsPhoto>list = buildingsPhotoService.listBuildingsPhoto();

			for(BuildingsPhoto item:list){
				if(item.getPhoto().contains(",")){
					List<String>path = PathTool.getAbsolutePathList(item.getPhoto(), request);
					item.setPhotos(path);
					item.setPhoto(null);
				}
				else{
					String absolutePath = PathTool.getAbsolutePath(item.getPhoto(), request);
					item.setPhoto(absolutePath);
				}
			}
			
			return list;	
			
	}

//android_query
	@RequestMapping("/android_detailBuildingsPhoto")
	@ResponseBody
	public BuildingsPhoto detailBuildingsPhoto(int id, HttpServletRequest request) {
		BuildingsPhoto buildingsPhoto = buildingsPhotoService.queryBuildingsPhoto(id);
		String pathPrefix = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() 
		+ request.getContextPath() + "/upload/";
		
		String photos = buildingsPhoto.getPhoto();
		List<String> list = new ArrayList<String>();
		String[] path = photos.split(",");
		for(int i=0;i<path.length;i++){
			list.add(pathPrefix + path[i]);
		}
		buildingsPhoto.setPhotos(list);
		return buildingsPhoto;
	}	
//android_add	
	@RequestMapping(value = "/android_insertBuildingsPhoto", method = RequestMethod.POST)
	@ResponseBody
	public Msg insertBuildingsPhotos(BuildingsPhoto buildingsPhoto,
			@RequestParam(required = false) MultipartFile[] photos, HttpServletRequest request) {

		StringBuffer pics = new StringBuffer("");
		List<String> list = new ArrayList<String>();
		String pathPrefix = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() 
		+ request.getContextPath() + "/upload/";
		try {
			for (int i = 0; i < photos.length; i++) {

				String relativePath = this.getRelativePath(photos[i], request);
				list.add(pathPrefix + relativePath);
				pics.append(relativePath);
				if (i < photos.length - 1) {
					pics.append(",");
				}
			}
			buildingsPhoto.setPhotos(list);
			buildingsPhoto.setPhoto(pics.toString());

			buildingsPhoto.setDate(LocalDate.now().toString());
			buildingsPhotoService.insertBuildingsPhoto(buildingsPhoto);
		} catch (Exception e) {
			return Msg.fail();
		}
		return Msg.success();
	}		

//android_update
	@RequestMapping(value = "/android_editBuildingsPhoto", method = RequestMethod.POST)
	@ResponseBody
	public Msg updateBuildingsPhotos(
			String delphotos,
			BuildingsPhoto buildingsPhoto,
			@RequestParam(required = false) MultipartFile[] photos,
			HttpServletRequest request) {
		
		StringBuffer sb = new StringBuffer("");
		//如果前台没有删除照片，传过来"[]"长度是2
		if(delphotos.length()>2){
		
		String delp = delphotos.substring(1, delphotos.length()-1);
		
		//删除传回url的照片
		String[] delPath = delp.split(",");
//		System.out.println(delphotos);	
//		System.out.println(delPath);
//		System.out.println(delp);
			BuildingsPhoto buildingsPhotos = buildingsPhotoService.queryBuildingsPhoto(buildingsPhoto.getId());
			
			String[] path2 = buildingsPhotos.getPhoto().split(",");
			List<String>list = new ArrayList<String>();
			List<String>temp = new ArrayList<String>();
			for(int i=0;i<path2.length;i++){
				list.add(path2[i]);
			}
		
			for(int i=0;i<delPath.length;i++){
				for(String p:list){
					
					String path = delPath[i].substring(delPath[i].lastIndexOf("/")+1,delPath[i].length()-1);
					if(p.equals(path)){
							temp.add(p);
							continue;
					}
				}
			}
			list.removeAll(temp);
			for(int k=0;k<list.size();k++){
				sb.append(list.get(k));
				if(k<list.size()-1){
					sb.append(",");
				}
			}			
		}
		//处理新上传的照片	
		try {
			//System.out.println(photos.length);
			if(photos[0].getSize()>0){
			for (int i = 0; i < photos.length; i++) {
				String relativePath = this.getRelativePath(photos[i], request);
				sb.append(relativePath);
				if (i < photos.length - 1) {
					sb.append(",");
				}
			}
			}
			buildingsPhoto.setPhoto(sb.toString());
			buildingsPhotoService.updateBuildingsPhoto(buildingsPhoto);
		} catch (Exception e) {
			e.printStackTrace();
			return Msg.fail();
		}
		return Msg.success();
	}

//android_delete
	@RequestMapping("/android_deleteBuildingsPhoto")
	@ResponseBody
	public Msg deleteBuildingsPhotos(String ids) {
		if (ids.contains(",")) {
			String[] ids_arr = ids.split(",");
			for (String id : ids_arr) {
				try {
					buildingsPhotoService.deleteBuildingsPhoto(Integer.parseInt(id));
				} catch (Exception e) {
					return Msg.fail();
				}
			}
			return Msg.success();
		} else {
			try {
				buildingsPhotoService.deleteBuildingsPhoto(Integer.parseInt(ids));
			} catch (Exception e) {
				return Msg.fail();
			}
			return Msg.success();
		}
		}
}

