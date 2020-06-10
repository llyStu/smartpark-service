package com.vibe.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.github.pagehelper.PageHelper;
import com.vibe.pojo.Tourist;
import com.vibe.service.scene.TouristService;
import com.vibe.util.PathTool;

@Controller
public class TouristController {

	@Autowired
	private TouristService touristService;
	
	@RequestMapping("/addTourist")	
	public String toAddTourist(){		
		return "polling/tourist/addTourist";
	}
	
	@RequestMapping("/insert_tourist")
	public String insertTourist(Tourist tourist,
				@RequestParam(required=false)MultipartFile[] photoFile,
				HttpServletRequest request) throws IllegalStateException, IOException{
		if(photoFile!=null&&photoFile[0].getSize()>0){			
			String path = PathTool.getRelativePath(photoFile, request);
			tourist.setPhoto(path);
			}
		tourist.setDate(LocalDate.now().toString());
		touristService.insertTourist(tourist);
		return "polling/tourist/listTourist";
	}

	@RequestMapping("/delete_tourist")	
	public String deleteTourist(String ids){
		try{
			if(ids.contains(",")){
				String[]id_arr = ids.split(",");
				for(String id:id_arr){
					touristService.deleteTourist(Integer.parseInt(id));
				}
				return "polling/tourist/listTourist";
			}
			else{
				touristService.deleteTourist(Integer.parseInt(ids));
				return "polling/tourist/listTourist";
			}
		}catch(Exception e){			
			e.printStackTrace();
			return null;
		}
	}

	//响应界面删除请求，不跳转页面
	@RequestMapping("/delete2_tourist")
	public String deleteTourist2(String ids) {

		System.out.println(ids);
		touristService.deleteTourist(Integer.parseInt(ids));
		return "success";
	}
	
	// 点击编辑，传来id，查出tourist，跳转至editTourist.jsp
	@RequestMapping("/edit_tourist")
	public String editTourist(int id, HttpServletRequest request) {
		Tourist tourist = touristService.queryTourist(id);
		String prefixPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
				+ request.getContextPath() + "/upload/";

		// 把文件的属性分开赋值为绝对路径
		tourist.setPhoto(prefixPath + tourist.getPhoto());

		request.setAttribute("tourist", tourist);
		return "polling/tourist/editTourist";
	}
	
	@RequestMapping("/update_tourist")
	public String updateTourist(Tourist tourist,
			@RequestParam(required = false)String delphoto,
			@RequestParam(required = false)MultipartFile[] photoFile,
			HttpServletRequest request) throws IllegalStateException, IOException{
		
		Tourist tourist2 = touristService.queryTourist(tourist.getId());		
		String path = PathTool.alterFile(delphoto, photoFile, tourist2.getPhoto(), request);
		if(!"".equals(path)){
			tourist.setPhoto(path);
		}				
		touristService.updateTourist(tourist);
		return "polling/tourist/listTourist";	
	}
	
	@RequestMapping("/query_tourist")	
	public String queryTourist(int id,HttpServletRequest request){
		
		Tourist tourist = touristService.queryTourist(id);
		List<String>path = PathTool.getAbsolutePathList(tourist.getPhoto(), request);
		tourist.setPhotos(path);
		request.setAttribute("tourist", tourist);
		return "polling/tourist/detailTourist";
	}
		
	@RequestMapping("/android_list_tourist")
	@ResponseBody
	public List<Tourist> android_listTourist(int pageNum,
			int pageCount, HttpServletRequest request){
		
		PageHelper.startPage(pageNum, pageCount);
		List<Tourist>list = touristService.queryTouristList();
		for(Tourist item:list){
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
	@RequestMapping("/list_tourist")
	public String listTourist(){
		return "polling/tourist/listTourist";
	}
}




