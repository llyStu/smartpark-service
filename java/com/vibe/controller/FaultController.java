package com.vibe.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.vibe.util.constant.ResponseModel;
import com.vibe.util.constant.ResultCode;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageHelper;
import com.vibe.common.Application;
import com.vibe.common.id.IdGenerator;
import com.vibe.pojo.AssetVo;
import com.vibe.pojo.DailyCheck;
import com.vibe.pojo.Response;
import com.vibe.pojo.TaskCount;
import com.vibe.pojo.user.User;
import com.vibe.service.asset.AssetService;
import com.vibe.service.fault.OldFaultService;
import com.vibe.service.logAop.MethodLog;
import com.vibe.service.user.UserService;
import com.vibe.util.Msg;
import com.vibe.util.PathTool;


@Controller
public class FaultController {
    @Autowired
    private UserService userService;

    @Autowired
    private AssetService assetService;

    @Autowired
    private OldFaultService faultService;

    @Autowired
    Application application;

    @RequestMapping("/addFault")
    public String toAddFault() {
        return "fault/addFault";
    }

    @RequestMapping("/insert_fault")
    @MethodLog(remark = "add", option = "添加设备故障信息")
    @ResponseBody
    public ResponseModel<String> insertFault(DailyCheck dailyCheck,
                                             @RequestParam(required = false) MultipartFile[] photoFile,
                                             HttpServletRequest request) throws IllegalStateException, IOException {

        IdGenerator<Integer> gen = application.getIntIdGenerator("task");
        if (photoFile != null && photoFile[0].getSize() > 0) {
            String pics = PathTool.getRelativePath(photoFile, request);
            //System.out.println("上传路径================="+pics);
            dailyCheck.setPhoto(pics);
            dailyCheck.setId(gen.next());
        }
        dailyCheck.setDate(LocalDate.now().toString());
        faultService.insertFault(dailyCheck);
        return ResponseModel.success("设备添加成功!").code(ResultCode.SUCCESS);
    }


    @RequestMapping("/delete_fault")
    @MethodLog(remark = "delete", option = "删除设备故障信息")
    @ResponseBody
    public ResponseModel<String> deleteFault(String ids) {
        try {
            String[] id_arr = ids.split(",");
            for (String id : id_arr) {
                faultService.deleteFault(Integer.parseInt(id));
            }
            return ResponseModel.success("设备删除成功!").code(ResultCode.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping("/edit_fault")
    @ResponseBody
    public ResponseModel<Map<String, Object>> editFault(int id, HttpServletRequest request) {

        DailyCheck dailyCheck = faultService.queryFault(id);
        AssetVo vo = new AssetVo();
        vo.setId(dailyCheck.getDeviceId());
        AssetVo device = assetService.findDevice(vo);
//		this.setAbsolutePath(dailyCheck, request);
        Map<String, Object> dailyMap = new HashMap<String, Object>();
        dailyMap.put("dailyCheck", dailyCheck);
        dailyMap.put("device", device);
//		request.setAttribute("dailyCheck", dailyCheck);
//		request.setAttribute("device", device);
//		return "fault/editFault";
        return ResponseModel.success(dailyMap).code(ResultCode.SUCCESS);
    }

    @RequestMapping("/update_fault")
    @MethodLog(remark = "edit", option = "修改设备故障信息")
    @ResponseBody
    public ResponseModel<String> updateFault(DailyCheck dailyCheck,
                                             @RequestParam(required = false) String delphoto,
                                             @RequestParam(required = false) MultipartFile[] photoFile,
                                             HttpServletRequest request) throws IllegalStateException, IOException {
        int id = dailyCheck.getId();
        DailyCheck dailyCheck2 = faultService.queryFault(id);
        String fileNames = dailyCheck2.getPhoto();

        if (photoFile.length > 0 && photoFile[0].getSize() != 0) {
            String pics = PathTool.alterFile(delphoto, photoFile, fileNames, request);
            if (!"".equals(pics)) {
                dailyCheck2.setPhoto(pics);
            }
        } else {
            dailyCheck2.setPhoto(fileNames);
        }
        dailyCheck2.setDescription(dailyCheck.getDescription());
        dailyCheck2.setPerson(dailyCheck.getPerson());
        dailyCheck2.setCheckType(dailyCheck.getCheckType());

        faultService.updateFault(dailyCheck2);
        return ResponseModel.success("设备修改成功!").code(ResultCode.SUCCESS);
//			return "fault/listFault";

    }

    @RequestMapping("/update_fault_state")
    @MethodLog(remark = "check", option = "审核故障申请")
    @ResponseBody
    public ResponseModel<String> updateState(int state, int id, HttpServletRequest request) {
        DailyCheck dailyCheck = faultService.queryFault(id);
        dailyCheck.setState(state);
        faultService.updateFault(dailyCheck);
//		request.setAttribute("dailyCheck", dailyCheck);
        return ResponseModel.success("故障审核成功!").code(ResultCode.SUCCESS);
//		return "fault/listFault";
    }

    @RequestMapping("/query_fault")
    @ResponseBody
    public ResponseModel<Map<String, Object>> queryFault(int id, HttpServletRequest request) {

        DailyCheck dailyCheck = faultService.queryFault(id);
        AssetVo vo = new AssetVo();
        vo.setId(dailyCheck.getDeviceId());
        AssetVo device = assetService.findDevice(vo);
        if (dailyCheck != null) {
            int person = dailyCheck.getPerson();
            User user = userService.queryUserById(person);
            if (user != null) {
                dailyCheck.setPersonName(user.getName());
            }
        }
//		this.setAbsolutePath(dailyCheck, request);
//		List<String>list = PathTool.getAbsolutePathList(dailyCheck.getPhoto(), request);
//		dailyCheck.setPhotos(list);
        Map<String, Object> dailyMap = new HashMap<String, Object>();
        dailyMap.put("dailyCheck", dailyCheck);
        dailyMap.put("device", device);
//		request.setAttribute("dailyCheck", dailyCheck);
//		request.setAttribute("device", device);
//		return "fault/detailFault";
        return ResponseModel.success(dailyMap).code(ResultCode.SUCCESS);
    }


    //给photos属性赋值为绝对路径数组，便于前台图片回显
    public void setAbsolutePath(DailyCheck dailyCheck, HttpServletRequest request) {

        String[] path = dailyCheck.getPhoto().split(",");
        List<String> list = new ArrayList<String>();

        String pathPrefix = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath() + "/upload/";
        for (String p : path) {
            list.add(pathPrefix + p);
            System.out.println("/////////" + pathPrefix + p);
        }

        dailyCheck.setPhotos(list);
    }

    /*跳转至审核页面
     * */
    @RequestMapping("/to_Check_fault")
    @ResponseBody
    public ResponseModel<Map<String, Object>> toCheckFault(int id, HttpServletRequest request) {

        DailyCheck dailyCheck = faultService.queryFault(id);
        AssetVo vo = new AssetVo();
        vo.setId(dailyCheck.getDeviceId());
        AssetVo device = assetService.findDevice(vo);
        if (dailyCheck != null) {

            int person = dailyCheck.getPerson();
            User user = userService.queryUserById(person);
            if (user != null) {
                dailyCheck.setPersonName(user.getName());
            }
        }
//		List<String>list = PathTool.getAbsolutePathList(dailyCheck.getPhoto(), request);
//		dailyCheck.setPhotos(list);
//
//		request.setAttribute("dailyCheck", dailyCheck);
//		request.setAttribute("device", device);
        Map<String, Object> dailyMap = new HashMap<String, Object>();
        dailyMap.put("dailyCheck", dailyCheck);
        dailyMap.put("device", device);
        return ResponseModel.success(dailyMap).code(ResultCode.SUCCESS);
    }


    @RequestMapping("/android_list_fault")
    @ResponseBody
    public List<DailyCheck> android_listFault(int pageNum,
                                              int pageCount, HttpServletRequest request) {

        PageHelper.startPage(pageNum, pageCount);
        List<DailyCheck> list = faultService.queryFaultList();
        for (DailyCheck d : list) {
            if (d != null) {
                d.setType(8);
                int person = d.getPerson();
                User user = userService.queryUserById(person);
                if (user != null) {
                    d.setPersonName(user.getName());
                }
            }
            //避免报空指针异常
            if (d.getPhoto() != null) {
                String pics = d.getPhoto();
                List<String> list2 = PathTool.getAbsolutePathList(pics, request);
                d.setPhoto(list2.get(0));
            }
        }
        return list;
    }

    @RequestMapping("/android_listDespatchFault")
    @ResponseBody
    public List<DailyCheck> android_listDespatchFault(int pageNum,
                                                      int pageCount, HttpServletRequest request) {

        PageHelper.startPage(pageNum, pageCount);
        String username = ((User) request.getSession().getAttribute("loginUser")).getName();
        List<DailyCheck> list = faultService.queryDespatchList(username);
        for (DailyCheck d : list) {
            if (d != null) {
                d.setType(9);
                int person = d.getPerson();
                User user = userService.queryUserById(person);
                if (user != null) {
                    d.setPersonName(user.getName());
                }
            }
            //避免报空指针异常
            if (d.getPhoto() != null) {
                String pics = d.getPhoto();
                List<String> list2 = PathTool.getAbsolutePathList(pics, request);
                d.setPhoto(list2.get(0));
            }
        }
        return list;
    }

    @RequestMapping("/list_fault")
    public String listFault() {
        return "fault/listFault";
    }

    //手机端查询详情,日常巡查与故障分析字段相同，故都用dailyCheck封装
    @RequestMapping("/android_query_fault")
    @ResponseBody
    public DailyCheck androidQueryFault(int id, HttpServletRequest request) {

        DailyCheck dailyCheck = faultService.queryFault(id);
//		this.setAbsolutePath(dailyCheck, request);
        List<String> list = PathTool.getAbsolutePathList(dailyCheck.getPhoto(), request);
        dailyCheck.setPhotos(list);
        dailyCheck.setPhoto(list.get(0));
        //request.setAttribute("dailyCheck", dailyCheck);
        return dailyCheck;
    }


    //手机端删除
    @RequestMapping("/android_delete_fault")
    @ResponseBody
    public Msg deleteFault2(String ids) {
        try {
            if (ids.contains(",")) {
                String[] id_arr = ids.split(",");
                for (String id : id_arr) {
                    faultService.deleteFault(Integer.parseInt(id));
                }
                return Msg.success();
            } else {
                faultService.deleteFault(Integer.parseInt(ids));
                return Msg.success();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Msg.fail();
        }
    }

    /*新版
    @RequestMapping("/despatchTo")
    @ResponseBody
    public Response despatchTo(@RequestParam("id")  int id,@RequestParam("personId") int personId,@RequestParam("taskName") String taskName,@RequestParam("taskDesc") String taskDesc,@RequestParam("requestFinishTime") String requestFinishTime){

        DailyCheck dailyCheck = faultService.queryFault(id);
        faultService.despatchTo(dailyCheck, personId,taskName,taskDesc,requestFinishTime);
        Response response = new Response();
        response.setResult(true);
        return response;
    }*/
    @RequestMapping("/despatchTo")
    @MethodLog(remark = "despatch", option = "分配故障任务")
    @ResponseBody
    public Response despatchTo(@RequestParam("id") int id, @RequestParam("personId") int personId) {

        faultService.despatchTo(id, personId);
        DailyCheck dailyCheck = faultService.queryFault(id);
        dailyCheck.setState(4);
        faultService.updateFault(dailyCheck);
        Response response = new Response();
        response.setResult(true);
        return response;
    }

    @RequestMapping("/finishTask")
    @ResponseBody
    public Response finishTask(@RequestParam("id") int id) {

        DailyCheck dailyCheck = faultService.queryFault(id);
        dailyCheck.setState(5);
        dailyCheck.setDate(LocalDate.now().toString());
        faultService.updateFault(dailyCheck);
        Response response = new Response();
        response.setResult(true);
        return response;
    }

    @RequestMapping("/findTaskCount")
    @ResponseBody
    public TaskCount findTaskCount(HttpServletRequest request) {

        return faultService.findTaskCount(((User) request.getSession().getAttribute("loginUser")).getId());
    }
}




