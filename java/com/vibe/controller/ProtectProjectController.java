package com.vibe.controller;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.vibe.pojo.ProtectProject;
import com.vibe.service.scene.ProtectProjectService;
import com.vibe.common.Application;
import com.vibe.common.id.IdGenerator;


@Controller
public class ProtectProjectController {

	@Autowired
	private ProtectProjectService protectProjectService;
	
	@Autowired
	private Application application;
	
//list	
	//点击列表按钮，跳转至listProtectProject.jsp页面
	@RequestMapping("/to_listProtectProject")
	public String to_listProtectProject(){		
		return "polling/protectProject/listProtectProject";
	}
	
//query
	//点击详细按钮，跳转至detailProtectProject.jsp页面
	@RequestMapping("/to_detailProtectProject")

	public String to_detailProtectProject(int id,HttpServletRequest request){	
	
		String prefixPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() 
					+ request.getContextPath() + "/upload/";
		ProtectProject protectProject = protectProjectService.queryProtectProject(id);
		
		//把文件的属性分开赋值为绝对路径
		protectProject.setPicture(prefixPath + protectProject.getPicture());
		protectProject.setProjectReport(prefixPath + protectProject.getProjectReport());
		protectProject.setPlanReport(prefixPath + protectProject.getPlanReport());
		protectProject.setOtherReport(prefixPath + protectProject.getOtherReport());
		protectProject.setFilePath(prefixPath + protectProject.getFilePath());
		protectProject.setSupervisionReport(prefixPath + protectProject.getSupervisionReport());
		protectProject.setEndReport(prefixPath + protectProject.getEndReport());
		protectProject.setOtherFiles(prefixPath + protectProject.getOtherFiles());
		
		request.setAttribute("protectProject", protectProject);
		//return "polling/protectProject/detailProtectProject";
		return "polling/protectProject/detailPP";
	}	
	
//add
	//点击增加，跳转至addBuildingsPhoto.jsp
	@RequestMapping("/add_ProtectProject")
	public String addProtectProject(){		
		return "polling/protectProject/insertPP";
	}
	@RequestMapping(value="/insertWithFile_ProtectProject",method=RequestMethod.POST) 
	public String insertProtectProject(ProtectProject protectProject,
							MultipartFile[] picture2,
							MultipartFile[] projectReport2,
							MultipartFile[] planReport2,
							MultipartFile[] otherReport2,
							MultipartFile[] filePath2,
							MultipartFile[] supervisionReport2,
							MultipartFile[] endReport2,
							MultipartFile[] otherFiles2,
							HttpServletRequest request) throws IllegalStateException, IOException{
		StringBuffer checkBoxSB = new StringBuffer("");
		String[] checkBoxVals = request.getParameterValues("relateElement");
		System.out.println("111"+checkBoxVals);
		if(checkBoxSB != null && checkBoxSB.length()>0){
			for(int i=0;i<checkBoxVals.length;i++){
				checkBoxSB.append(checkBoxVals[i]);
				if(i<checkBoxVals.length-1){
					checkBoxSB.append(",");
				}
		     }
			protectProject.setRelateElement(checkBoxSB.toString());
		}
	
		IdGenerator<Integer> gen = application.getIntIdGenerator("user");
		protectProject.setId(gen.next());

		String picturePath = this.getArrayFilePath(picture2,request);	
		String projectReportPath = this.getArrayFilePath(projectReport2,request);
		String planReportPath = this.getArrayFilePath(planReport2,request);
		String otherReportPath = this.getArrayFilePath(otherReport2,request);
		String filePathPath = this.getArrayFilePath(filePath2,request);
		String supervisionReportPath = this.getArrayFilePath(supervisionReport2,request);
		String endReportPath = this.getArrayFilePath(endReport2,request);
		String otherFilesPath = this.getArrayFilePath(otherFiles2,request);
		
		protectProject.setPicture(picturePath);
		protectProject.setProjectReport(projectReportPath);
		protectProject.setPlanReport(planReportPath);
		protectProject.setOtherReport(otherReportPath);
		protectProject.setFilePath(filePathPath);
		protectProject.setSupervisionReport(supervisionReportPath);
		protectProject.setEndReport(endReportPath);
		protectProject.setOtherFiles(otherFilesPath);
				
		protectProjectService.insertProtectProject(protectProject);
		return "polling/protectProject/listProtectProject";
	}
	
	// 截取上传图片后缀名并加上UUID
	public String subPath(String originalName) {
		String path = originalName.substring(originalName.lastIndexOf("."));
		return UUID.randomUUID() + path;
	}

	// 将上传的文件保存在upload夹下并返回相对路径
	public String getAbsolutePath(MultipartFile file, HttpServletRequest request)
			throws IllegalStateException, IOException {
		String absolutePath = "";
		if (file != null) {
			String uploadPath = request.getSession().getServletContext().getRealPath("/upload/");
			String photo_absolutePath = this.subPath(file.getOriginalFilename());
			if (!(new File(uploadPath)).exists()) {
				(new File(uploadPath)).mkdirs();
			}
			File newPhotoFile = new File(uploadPath + photo_absolutePath);
			file.transferTo(newPhotoFile);
			absolutePath =  photo_absolutePath;
		}
		return absolutePath;
	}
	//处理多个文件名字获取
	public String getArrayFilePath(MultipartFile[] files,
				HttpServletRequest request) throws IllegalStateException, IOException{
		StringBuffer filePathSB = new StringBuffer("");
		for(int i=0;i<files.length;i++){
			String filePath = this.getAbsolutePath(files[i],request);
			filePathSB.append(filePath);
			if(i<files.length-1){
				filePathSB.append(",");
			}
		}
		return filePathSB.toString();
	}
//edit	
	//点击编辑，传来id，查出protectProject，跳转至editProtectProject.jsp
	@RequestMapping("/edit_ProtectProject")
	public String editProtectProject(int id,HttpServletRequest request){
		ProtectProject protectProject = protectProjectService.queryProtectProject(id);
		String prefixPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() 
		+ request.getContextPath() + "/upload/";

		//把文件的属性分开赋值为绝对路径
		protectProject.setPicture(prefixPath + protectProject.getPicture());
		protectProject.setProjectReport(prefixPath + protectProject.getProjectReport());
		protectProject.setPlanReport(prefixPath + protectProject.getPlanReport());
		protectProject.setOtherReport(prefixPath + protectProject.getOtherReport());
		protectProject.setFilePath(prefixPath + protectProject.getFilePath());
		protectProject.setSupervisionReport(prefixPath + protectProject.getSupervisionReport());
		protectProject.setEndReport(prefixPath + protectProject.getEndReport());
		protectProject.setOtherFiles(prefixPath + protectProject.getOtherFiles());
				
		request.setAttribute("protectProject", protectProject);
		return "polling/protectProject/editPP";
	}
	@RequestMapping(value="/update_ProtectProject",method=RequestMethod.POST)
	public String updateProtectProject(ProtectProject protectProject,		
			@RequestParam(required = false) MultipartFile[] picture2,
			@RequestParam(required = false) MultipartFile[] projectReport2,
			@RequestParam(required = false) MultipartFile[] planReport2,
			@RequestParam(required = false) MultipartFile[] otherReport2,
			@RequestParam(required = false) MultipartFile[] filePath2,
			@RequestParam(required = false) MultipartFile[] supervisionReport2,
			@RequestParam(required = false) MultipartFile[] endReport2,
			@RequestParam(required = false) MultipartFile[] otherFiles2,
										HttpServletRequest request) throws IllegalStateException, IOException{
//			System.out.println(picture2);
//			System.out.println(picture2.length);
//			System.out.println(picture2[0].getSize());
		if(picture2!=null&&picture2[0].getSize()>0){
			String drawingAbsolutePath = this.getArrayFilePath(picture2,request);
			protectProject.setPicture(drawingAbsolutePath);
		}
		if(projectReport2!=null&&projectReport2[0].getSize()>0){
			String projectReport2Path = this.getArrayFilePath(projectReport2,request);
			protectProject.setProjectReport(projectReport2Path);
		}
		if(planReport2!=null&&planReport2[0].getSize()>0){
			String planReport2Path = this.getArrayFilePath(planReport2,request);
			protectProject.setPlanReport(planReport2Path);
		}
		if(otherReport2!=null&&otherReport2[0].getSize()>0){
			String otherReport2Path = this.getArrayFilePath(otherReport2,request);
			protectProject.setOtherReport(otherReport2Path);
		}
		if(filePath2!=null&&filePath2[0].getSize()>0){
			String filePath2Path = this.getArrayFilePath(filePath2,request);
			protectProject.setFilePath(filePath2Path);
		}
		if(supervisionReport2!=null&&supervisionReport2[0].getSize()>0){
			String supervisionReport2Path = this.getArrayFilePath(supervisionReport2,request);
			protectProject.setSupervisionReport(supervisionReport2Path);
		}
		if(endReport2!=null&&endReport2[0].getSize()>0){
			String endReport2Path = this.getArrayFilePath(endReport2,request);
			protectProject.setEndReport(endReport2Path);
		}
		if(otherFiles2!=null&&otherFiles2[0].getSize()>0){
			String otherFiles2Path = this.getArrayFilePath(otherFiles2,request);
			protectProject.setOtherFiles(otherFiles2Path);
		}
		
		protectProjectService.updateProtectProject(protectProject);
		return "polling/protectProject/listProtectProject";
	}	
	
	
	/*
	 * “删除”方法
	 * 根据参数特征判断单个删除还是批量删除
	 */
	@RequestMapping("/delete_ProtectProject")
	public String deleteProtectProject(String ids){
		if(ids.contains(",")){
			String[] ids_arr = ids.split(",");
			for(String id:ids_arr){
				protectProjectService.deleteProtectProject(Integer.parseInt(id));
			}
			return "polling/protectProject/listProtectProject";
		}
		else{
			protectProjectService.deleteProtectProject(Integer.parseInt(ids));		
			return "polling/protectProject/listProtectProject";	
		}		
	}
	

	

}
