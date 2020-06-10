package com.vibe.controller.energy;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.vibe.pojo.HandInputData;
import com.vibe.pojo.HandInputProbe;
import com.vibe.pojo.PageResult;
import com.vibe.pojo.Response;
import com.vibe.pojo.energy.ProbeMeter;
import com.vibe.pojo.user.User;
import com.vibe.service.energy.HandInputService;
import com.vibe.service.logAop.MethodLog;
import com.vibe.util.ResponseResult;
import com.vibe.util.constant.ResponseModel;
import com.vibe.util.constant.ResultCode;

@Controller
public class HandInputController {
    @Autowired
    private HandInputService service;


    @RequestMapping("/insertHandInput")
    @MethodLog(remark = "add", option = "手动录入表的数据")
    public @ResponseBody
    Response insertHandInput(HttpServletRequest request, @RequestBody List<HandInputData> datas) {

        try {
            User user = (User) request.getSession().getAttribute("loginUser");
            for (HandInputData handInputData : datas) {
                handInputData.setPerson(user.getId());
            }
            service.handInputData(datas);
            return ResponseResult.getANewResponse(true);
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseResult.getANewResponse(false);
        }

    }

    @RequestMapping("/updateHandInput")
    @MethodLog(remark = "edit", option = "修改录入数据")
    public @ResponseBody
    Response updateHandInput(@RequestBody HandInputData data) {


        try {
            service.updateHandInput(data);
            return ResponseResult.getANewResponse(true);
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseResult.getANewResponse(false);
        }

    }

    @RequestMapping("/findHandInput")
    public @ResponseBody
    PageResult<HandInputData> findHandInput(
            @RequestParam("start") String start, @RequestParam("end") String end, @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int rows) {
        PageHelper.startPage(page, rows);

        List<HandInputData> list = service.findHandInput(start, end);
        PageResult<HandInputData> pageResult = new PageResult<>();
        pageResult.setData(list);
        pageResult.setTotal(((Page<HandInputData>) list).getTotal());
        return pageResult;
    }

    @RequestMapping("/deleteHandInput")
    @MethodLog(remark = "delete", option = "手动删除表数据")
    public @ResponseBody
    Response deleteHandInput(@RequestParam("idStr") String idStr) {
        try {
            service.deleteHandInput(idStr);
            return ResponseResult.getANewResponse(true);
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseResult.getANewResponse(false);
        }

    }

    @RequestMapping("/getHandInputProbe")
    public @ResponseBody
    List<HandInputProbe> getHandInputProbe() {
        return service.getHandInputProbe();
    }

    @RequestMapping("/getProbe")
    @ResponseBody
    public ResponseModel<List<ProbeMeter>> getProbe(@RequestParam(required = true) int energyType,
                                                    @RequestParam(required = false, defaultValue = "0") int subitemType,
                                                    @RequestParam(required = true) int type) {//0智能表，1非 表
        try {
            return ResponseModel.success(service.getProbe(energyType, subitemType, type)).code(ResultCode.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseModel.failure("错误" + e.getMessage()).code(ResultCode.ERROR);
        }
    }

    @RequestMapping("/findHandInputById")
    @ResponseBody
    public ResponseModel<HandInputData> findHandInputById(@RequestParam("id") Integer id) {
        try {
            return ResponseModel.success(service.findHandInputById(id)).code(ResultCode.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseModel.failure("错误" + e.getMessage()).code(ResultCode.ERROR);
        }
    }
}
