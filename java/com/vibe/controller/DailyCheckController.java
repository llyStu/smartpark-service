package com.vibe.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.vibe.util.constant.ResponseModel;
import com.vibe.util.constant.ResultCode;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageHelper;
import com.vibe.common.Application;
import com.vibe.common.id.IdGenerator;
import com.vibe.pojo.DailyCheck;
import com.vibe.service.dailycheck.DailyCheckService;
import com.vibe.service.logAop.MethodLog;
import com.vibe.util.Msg;
import com.vibe.util.PathTool;

@RestController
public class DailyCheckController {

    @Autowired
    private DailyCheckService dailyCheckService;

    @Autowired
    private Application application;

    @RequestMapping("/addDailyCheck")
    public String toAddDailyCheck() {

        return "polling/dailyCheck/addDailyCheck";
    }

    @RequestMapping("/insert_dailyCheck")
    @MethodLog(remark = "add", option = "添加巡查记录")
    public ResponseModel insertDailyCheck(DailyCheck dailyCheck,
                                          @RequestParam(required = false) MultipartFile[] photoFile,
                                          HttpServletRequest request) throws IllegalStateException, IOException {
        if (photoFile != null && photoFile[0].getSize() > 0) {

			/*StringBuffer pics = new StringBuffer("");

			for (int i = 0; i < photoFile.length; i++) {
				String relativePath = this.getRelativePath(photoFile[i], request);

				pics.append(relativePath);
				if (i < photoFile.length - 1) {
					pics.append(",");
				}
			}*/
            String pics = PathTool.getRelativePath(photoFile, request);
            dailyCheck.setPhoto(pics);
        }
        IdGenerator<Integer> gen = application.getIntIdGenerator("task");
        dailyCheck.setId(gen.next());
        dailyCheck.setDate(LocalDate.now().toString());
        dailyCheckService.insertDailyCheck(dailyCheck);
        return ResponseModel.success("新增成功").code(ResultCode.SUCCESS);
    }

/*	// 截取上传图片后缀名并加上UUID
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
	}*/

    @RequestMapping("/delete_dailyCheck")
    @MethodLog(remark = "delete", option = "删除巡查记录")
    public ResponseModel deleteDailyCheck(String ids) {
        try {
            if (ids.contains(",")) {
                String[] id_arr = ids.split(",");
                for (String id : id_arr) {
                    dailyCheckService.deleteDailyCheck(Integer.parseInt(id));
                }
                return ResponseModel.success("删除成功").code(ResultCode.SUCCESS);
            } else {
                dailyCheckService.deleteDailyCheck(Integer.parseInt(ids));
                return ResponseModel.success("删除成功").code(ResultCode.SUCCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseModel.failure("删除失败").code(ResultCode.ERROR);
        }
    }

    @RequestMapping("/edit_dailyCheck")
    public String editDailyCheck(int id, HttpServletRequest request) {

        DailyCheck dailyCheck = dailyCheckService.queryDailyCheck(id);
        System.out.println(dailyCheck);
        this.setAbsolutePath(dailyCheck, request);
        request.setAttribute("dailyCheck", dailyCheck);
        return "polling/dailyCheck/editDailyCheck";
    }

    @RequestMapping("/update_dailyCheck")
    @MethodLog(remark = "edit", option = "编辑巡查记录")
    public ResponseModel updateDailyCheck(DailyCheck dailyCheck,
                                          @RequestParam(required = false) String delphoto,
                                          @RequestParam(required = false) MultipartFile[] photoFile,
                                          HttpServletRequest request) throws IllegalStateException, IOException {

//		StringBuffer pathSB = new StringBuffer("");
/*		if(delphoto!=null&&delphoto.length()>0){
			
			//前台传来delphoto格式为"[XXXX,XXXX]"
			String[] delPath = (delphoto.substring(1, delphoto.length()-1)).split(",");
			int id = dailyCheck.getId();
			DailyCheck dailyCheck2 = dailyCheckService.queryDailyCheck(id);
		
			List<String>temp = new ArrayList<String>();
			List<String> list = new ArrayList<String>();	
			
			String[] path = dailyCheck2.getPhoto().split(",");
			for(String p:path){
				list.add(p);
			}
			for(String del:delPath){
				for(String p:path){
					if(del.equals(p)){
						temp.add(p);
					}
				}
			}
			list.removeAll(temp);
			for(int i=0;i<list.size();i++){
				pathSB.append(list.get(i));				
				pathSB.append(",");
			}
		}
		
		//对前台传来的照片的保存和对象属性名的更改
		if(photoFile[0].getSize()>0){
			
			for(int i=0;i<photoFile.length;i++){
				String relative_path = this.getRelativePath(photoFile[i], request);
				pathSB.append(relative_path);
				if(i<photoFile.length-1){
					pathSB.append(",");
				}
			}
		}

		if(pathSB.length()>1&&pathSB.lastIndexOf(",")==pathSB.length()-1){
			pathSB = new StringBuffer(pathSB.substring(0, pathSB.length()-1));			
		}
		if(pathSB.length()>1){
			dailyCheck.setPhoto(pathSB.toString());
		}*/
        int id = dailyCheck.getId();
        DailyCheck dailyCheck2 = dailyCheckService.queryDailyCheck(id);
        String fileNames = dailyCheck2.getPhoto();
        String pics = PathTool.alterFile(delphoto, photoFile, fileNames, request);

        if (!"".equals(pics)) {
            dailyCheck.setPhoto(pics);
        }

        dailyCheckService.updateDailyCheck(dailyCheck);
        return ResponseModel.success(dailyCheck).code(ResultCode.SUCCESS);
    }

    @RequestMapping("/query_dailyCheck")
    public ResponseModel queryDailyCheck(int id, HttpServletRequest request) {

        DailyCheck dailyCheck = dailyCheckService.queryDailyCheck(id);
//		this.setAbsolutePath(dailyCheck, request);
        List<String> list = PathTool.getAbsolutePathList(dailyCheck.getPhoto(), request);
        dailyCheck.setPhotos(list);
        return ResponseModel.success(dailyCheck).code(ResultCode.SUCCESS);
    }


    //给photos属性赋值为绝对路径数组，便于前台图片回显
    public void setAbsolutePath(DailyCheck dailyCheck, HttpServletRequest request) {

        String[] path = dailyCheck.getPhoto().split(",");
        List<String> list = new ArrayList<String>();

        String pathPrefix = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath() + "/upload/";
        for (String p : path) {
            list.add(pathPrefix + p);
        }

        dailyCheck.setPhotos(list);
    }

    @RequestMapping("/android_list_dailyCheck")
    public List<DailyCheck> android_listDailyCheck(int pageNum, int pageCount, HttpServletRequest request) {

        PageHelper.startPage(pageNum, pageCount);
        List<DailyCheck> list = dailyCheckService.queryDailyCheckList();
		/*for(DailyCheck d:list){
			
			//避免报空指针异常
			if(d.getPhoto()!=null){
			String pics = d.getPhoto();
			List<String> list2 = PathTool.getAbsolutePathList(pics, request);
			d.setPhoto(list2.get(0));
			}
		}*/

        return list;
    }

    @RequestMapping("/list_dailyCheck")
    public String listDailyCheck() {
        return "polling/dailyCheck/listDailyCheck";
    }

    //手机端查询详情
    @RequestMapping("/android_query_dailyCheck")
    public DailyCheck queryDailyCheck2(int id, HttpServletRequest request) {

        DailyCheck dailyCheck = dailyCheckService.queryDailyCheck(id);
//		this.setAbsolutePath(dailyCheck, request);
//		List<String>list = PathTool.getAbsolutePathList(dailyCheck.getPhoto(), request);
//		dailyCheck.setPhotos(list);
//		dailyCheck.setPhoto(list.get(0));
        //request.setAttribute("dailyCheck", dailyCheck);
        return dailyCheck;
    }

    //手机端删除
    @RequestMapping("/android_delete_dailyCheck")
    @MethodLog(remark = "delete", option = "移动端删除巡查记录")
    public Msg deleteDailyCheck2(String ids) {
        try {
            if (ids.contains(",")) {
                String[] id_arr = ids.split(",");
                for (String id : id_arr) {
                    dailyCheckService.deleteDailyCheck(Integer.parseInt(id));
                }
                return Msg.success();
            } else {
                dailyCheckService.deleteDailyCheck(Integer.parseInt(ids));
                return Msg.success();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Msg.fail();
        }
    }
}




