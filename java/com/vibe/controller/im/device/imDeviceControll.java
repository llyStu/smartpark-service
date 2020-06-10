package com.vibe.controller.im.device;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.vibe.poiutil.POIUtil;
import com.vibe.service.device.ImDeviceService;
import com.vibe.service.logAop.MethodLog;
import com.vibe.utils.FormJson;

@Controller
public class imDeviceControll {
    @Autowired
    private ImDeviceService imDeviceService;


    /**
     * 读取excel文件中的用户信息，保存在数据库中
     *
     * @param file
     */
    @RequestMapping("/im/deviceData")
    @MethodLog(remark = "input", option = "导入设备数据")
    public @ResponseBody
    List<FormJson> deviceData(@RequestParam(value = "file") MultipartFile file) {
        List<FormJson> form = new ArrayList<FormJson>();
        FormJson json = new FormJson();
        try {
            // 检查文件
            POIUtil.checkFile(file);
            // 获得Workbook工作薄对象
            Workbook workbook = POIUtil.getWorkBook(file);
            if (workbook != null) {
                imDeviceService.saveDeviceList(workbook);
            }
            // 封装父子结构的id

            json.setSuccess(true);
            json.setMessage("操作成功");
            form.add(json);
        } catch (Exception e) {
            /* Logger.info("读取excel文件失败", e); */
            e.printStackTrace();
            if (e instanceof DateTimeParseException) {
                json.setSuccess(false);
                json.setMessage("日期格式错误,操作失败");
                form.add(json);
            } else {
                json.setSuccess(false);
                json.setMessage("操作失败");
                form.add(json);
            }

        }

        return form;
    }


}
