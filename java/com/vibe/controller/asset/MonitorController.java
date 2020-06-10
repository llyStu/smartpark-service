package com.vibe.controller.asset;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.vibe.util.constant.ResponseModel;
import com.vibe.util.constant.ResultCode;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ibm.icu.text.SimpleDateFormat;
import com.vibe.poiutil.ExportExcel;
import com.vibe.pojo.ProbeValue;
import com.vibe.pojo.historydata.HistoryDataCondition;
import com.vibe.service.asset.MonitorService;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MonitorController {
    @Autowired
    private MonitorService monitorService;

    // 获取不同类型的资产的详细信息
    @RequestMapping("/app_page/asset")
    @ResponseBody
    public Map<String, Object> getAssetByKind(@RequestParam(required = false) String kind, Integer id) {
        return monitorService.getAssetByKind(kind, id);
    }

    @RequestMapping("/monitor/init")
    public @ResponseBody
    Object initMonitorList(Integer catalog, @RequestParam(defaultValue = "0") Integer spaceId,
                           @RequestParam(required = false) Integer type, @RequestParam(required = false) Integer depth) {
        return monitorService.buildAssetList(catalog, spaceId, type, depth, null);

    }

    @RequestMapping("/monitor/init_tree")
    public @ResponseBody
    Object initMonitorTree(Integer catalog, @RequestParam(defaultValue = "0") Integer spaceId,
                           @RequestParam(required = false) Integer type) {
        return monitorService.initMonitorTree(catalog, spaceId, type);

    }

    @RequestMapping("/monitor/init_page")
    public @ResponseBody
    Map<String, Object> initMonitorListPage(@RequestParam(defaultValue = "0") int catalog,
                                            @RequestParam(defaultValue = "0") Integer spaceId, @RequestParam(required = false) Integer type,
                                            @RequestParam(required = false) String catalogIds, @RequestParam(required = false) String caption,
                                            @RequestParam(required = false) Integer depth, @RequestParam(defaultValue = "1") Integer page,
                                            @RequestParam(defaultValue = "30") Integer rows, @RequestParam(required = false) String searchStr) {
        return monitorService.initPage(catalog, catalogIds, spaceId, type, depth, page, rows, caption, searchStr);
    }

    @RequestMapping("/monitor/probeValue")
    public @ResponseBody
    List<ProbeValue> probeValue(String ids) {
        return monitorService.getProbeValue(ids);
    }

    @RequestMapping("/device_data/probeHistoryValue")
    public @ResponseBody
    Map<String, Object> probeHistoryValue(HistoryDataCondition hdc, @RequestParam(defaultValue = "1") Integer page,
                                          @RequestParam(defaultValue = "10") Integer rows) {
        return monitorService.probeHistoryValue(hdc, page, rows);
    }

    @RequestMapping("/monitor/code/avg")
    public ResponseModel getEnvironmentAvgByCode(@RequestParam Integer code) {
        return ResponseModel.success(monitorService.getEnvironmentAvgByCode(code)).code(ResultCode.SUCCESS);
    }

    /**
     * 导出
     *
     * @param response
     * @param hdc
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    @RequestMapping("/device_data/probesHistoryExportExcel")
    public void probesHistoryExportExcel(HttpServletResponse response, HistoryDataCondition hdc) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        Map<String, Object> probeHistoryValue = monitorService.probeHistoryValue(hdc, null, null);

        //表头
        List<String> title = (ArrayList<String>) probeHistoryValue.get("title");
        String[] headers = title.toArray(new String[title.size()]);
        //数据
        List<ArrayList<Object>> dataRows = (List<ArrayList<Object>>) probeHistoryValue.get("value");

        ExportExcel<ArrayList<Object>> ex = new ExportExcel<ArrayList<Object>>();
        OutputStream os = response.getOutputStream();// 取得输出流
        response.reset();// 清空输出流
        String fileName = format.format(System.currentTimeMillis()) + "下载·" + probeHistoryValue.get("fileName") + "数据.xls";
        // response.setHeader("transfer-encoding", "chunked");
        response.setContentType("application/x-msdownload");// 设定输出文件类型
        response.setHeader("Content-Disposition",
//				"attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1" )); //设定文件输出头
                URLEncoder.encode(fileName, "UTF-8"));
        HSSFWorkbook book = ex.onListexportExcel("sheet", headers, dataRows);
        book.write(os);
        os.close();

    }

    @RequestMapping("/monitor/codeName")
    public ResponseModel getMonitorCodeName() {
        try {
            return ResponseModel.success(monitorService.getMonitorCodeName()).code(ResultCode.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseModel.failure("").code(ResultCode.ERROR).errorMessage("查询失败");
        }
    }

}
