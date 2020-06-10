package com.vibe.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.github.pagehelper.PageHelper;
import com.vibe.pojo.Implimentation;
import com.vibe.service.scene.ImplimentationService;
import com.vibe.util.Msg;
import com.vibe.util.PathTool;

@Controller
public class ImplimentationController {

	@Autowired
	private ImplimentationService implimentationService;
	
	@RequestMapping("/addImplimentation")	
	public String toAddImplimentation(){		
		return "polling/implimentation/addImplimentation";
	}
	
	@RequestMapping("/insert_implimentation")
	@ResponseBody
	public Msg insertImplimentation(Implimentation implimentation,
				@RequestParam(required=false)MultipartFile[] photoFile,
				HttpServletRequest request) throws IllegalStateException, IOException{
		if(photoFile!=null&&photoFile[0].getSize()>0){			
			String path = PathTool.getRelativePath(photoFile, request);
			implimentation.setPhoto(path);
			}
		
		implimentationService.insertImplimentation(implimentation);
		return Msg.success();
	}

	@RequestMapping("/delete_implimentation")
	@ResponseBody
	public Msg deleteImplimentation(String ids){
		try{
			if(ids.contains(",")){
				String[]id_arr = ids.split(",");
				for(String id:id_arr){
					implimentationService.deleteImplimentation(Integer.parseInt(id));
				}
				return Msg.success();
			}
			else{
				implimentationService.deleteImplimentation(Integer.parseInt(ids));
				return Msg.success();
			}
		}catch(Exception e){			
			e.printStackTrace();
			return Msg.fail();
		}
	}
	
	//响应界面删除请求，不跳转页面
	@RequestMapping("/delete2_implimentation")
	public void deleteImplimentation2(String ids) {

		implimentationService.deleteImplimentation(Integer.parseInt(ids));
	}

	//点击编辑，传来id，查出implimentation，跳转至editImplimentation.jsp
	@RequestMapping("/edit_implimentation")
	public String editImplimentation(int id,HttpServletRequest request){
		Implimentation implimentation = implimentationService.queryImplimentation(id);
		String prefixPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() 
		+ request.getContextPath() + "/upload/";

		//把文件的属性分开赋值为绝对路径
		implimentation.setPhoto(prefixPath + implimentation.getPhoto());
		
				
		request.setAttribute("implimentation", implimentation);
		return "polling/implimentation/editImplimentation";
	}
	
	@RequestMapping("/update_implimentation")
	@ResponseBody
	public Msg updateImplimentation(Implimentation implimentation,
			@RequestParam(required = false)String delphoto,
			@RequestParam(required = false)MultipartFile[] photoFile,
			HttpServletRequest request) throws IllegalStateException, IOException{
		
		Implimentation implimentation2 = implimentationService.queryImplimentation(implimentation.getId());
		String path = PathTool.alterFile(delphoto, photoFile, implimentation2.getPhoto(), request);
		if(!"".equals(path)){
			implimentation.setPhoto(path);
		}				
		implimentationService.updateImplimentation(implimentation);
		return Msg.success();		
	}
	
	@RequestMapping("/query_implimentation")
	@ResponseBody
	public Implimentation queryImplimentation(int id, HttpServletRequest request){
		
		Implimentation Implimentation = implimentationService.queryImplimentation(id);
		List<String>path = PathTool.getAbsolutePathList(Implimentation.getPhoto(), request);
		Implimentation.setPhotos(path);
		return Implimentation;
	}
		
	@RequestMapping("/android_list_implimentation")
	@ResponseBody
	public List<Implimentation> android_listImplimentation(int pageNum,
                                                           int pageCount, HttpServletRequest request){
		
		PageHelper.startPage(pageNum, pageCount);
		List<Implimentation>list = implimentationService.queryImplimentationList();
		
		for(Implimentation item:list){
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
	@RequestMapping("/list_implimentation")
	public String listImplimentation(){
		return "polling/implimentation/listImplimentation";
	}
}




