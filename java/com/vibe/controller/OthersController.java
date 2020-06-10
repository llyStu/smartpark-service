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
import com.vibe.pojo.Others;
import com.vibe.service.scene.OthersService;
import com.vibe.util.Msg;
import com.vibe.util.PathTool;

@Controller
public class OthersController {

	@Autowired
	private OthersService othersService;
	
	@RequestMapping("/add_others")	
	public String toAddOthers(){		
		return "polling/others/addOthers";
	}
	
	@RequestMapping("/insert_others")
	
	public String insertOthers(Others others,
				@RequestParam(required=false)MultipartFile[] photoFile,
				HttpServletRequest request) throws IllegalStateException, IOException{
		if(photoFile!=null&&photoFile[0].getSize()>0){			
			String path = PathTool.getRelativePath(photoFile, request);
			others.setPhoto(path);
			}
		others.setDate(LocalDate.now().toString());
		othersService.insertOthers(others);
		return "polling/others/listOthers";
	}

	@RequestMapping("/delete_others")
	@ResponseBody
	public Msg deleteOthers(String ids){
		try{
			if(ids.contains(",")){
				String[]id_arr = ids.split(",");
				for(String id:id_arr){
					othersService.deleteOthers(Integer.parseInt(id));
				}
				return Msg.success();
			}
			else{
				othersService.deleteOthers(Integer.parseInt(ids));
				return Msg.success();
			}
		}catch(Exception e){			
			e.printStackTrace();
			return Msg.fail();
		}
	}
	
	//响应界面删除请求，不跳转页面
		@RequestMapping("/delete2_others")
		public void deleteOthers2(String ids) {		
				
			othersService.deleteOthers(Integer.parseInt(ids));
		}

	//点击编辑，传来id，查出Others，跳转至editOthers.jsp
		@RequestMapping("/edit_others")
		public String editOthers(int id,HttpServletRequest request){
			Others others = othersService.queryOthers(id);
			String prefixPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() 
			+ request.getContextPath() + "/upload/";

			//把文件的属性分开赋值为绝对路径
			others.setPhoto(prefixPath + others.getPhoto());
			
					
			request.setAttribute("others", others);
			return "polling/others/editOthers";
		}
	
	@RequestMapping("/update_others")
	@ResponseBody
	public Msg updateOthers(Others others,
			@RequestParam(required = false)String delphoto,
			@RequestParam(required = false)MultipartFile[] photoFile,
			HttpServletRequest request) throws IllegalStateException, IOException{
		
		Others others2 = othersService.queryOthers(others.getId());
		String path = PathTool.alterFile(delphoto, photoFile, others2.getPhoto(), request);
		if(!"".equals(path)){
			others.setPhoto(path);
		}				
		othersService.updateOthers(others);
		return Msg.success();		
	}
	
	@RequestMapping("/query_others")
	@ResponseBody
	public Others queryOthers(int id, HttpServletRequest request){
		
		Others others = othersService.queryOthers(id);
		List<String>path = PathTool.getAbsolutePathList(others.getPhoto(), request);
		others.setPhotos(path);
		return others;
	}
		
	@RequestMapping("/android_list_others")
	@ResponseBody
	public List<Others> android_listOthers(int pageNum,
                                           int pageCount, HttpServletRequest request){
		
		PageHelper.startPage(pageNum, pageCount);
		List<Others>list = othersService.queryOthersList();
		for(Others item:list){
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
	@RequestMapping("/list_others")
	public String listOthers(){
		return "polling/others/listOthers";
	}
}




