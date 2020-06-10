package com.vibe.controller.energy;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;

import com.vibe.common.config.SystemConfigManager;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.github.pagehelper.PageInfo;
import com.vibe.poiutil.ExportExcel;
import com.vibe.pojo.CommonMonitorDataVo;
import com.vibe.pojo.energy.Energy;
import com.vibe.pojo.energy.EnergyBiao;
import com.vibe.pojo.energy.EnergyCount;
import com.vibe.pojo.energy.EnergyCountOne;
import com.vibe.pojo.energy.EnergyShowBiao;
import com.vibe.pojo.energy.EnergyType;
import com.vibe.pojo.energy.RankEnergy;
import com.vibe.service.energy.EnergyStatisticsService;
import com.vibe.util.ListSortUtil;
import com.vibe.utils.TreeNode;

@Controller
public class HistoryController {

    @Autowired
    private SystemConfigManager configManager;

    @Autowired
    private EnergyStatisticsService service;

    //建筑对比
    @RequestMapping(value = "/history/buding_contrast")
    public @ResponseBody
    List<CommonMonitorDataVo> getBudingContrast(CommonMonitorDataVo vo, String ids) {
        List<Integer> idList = new ArrayList<Integer>();
        if (ids != null) {
            String[] split = ids.split(",");
            for (String id : split) {
                idList.add(Integer.parseInt(id));
            }
        }
        List<CommonMonitorDataVo> budings = service.getEnergyByBudingsTotal(vo, idList);
        return budings;
    }

    //分项对比
    @RequestMapping("/history/redio_contrast")
    public @ResponseBody
    List<TreeNode> getRedioData(CommonMonitorDataVo vo) {
        return service.getRedioData(vo);
    }

    //历史对比
    @RequestMapping("/history/history_contrast")
    public @ResponseBody
    List<Map<String, Object>> getHistoryData(CommonMonitorDataVo vo) {
        return service.getHistoryData(vo);
    }

    //设备对比，设备数据
    @RequestMapping("/history/device_data")
    public @ResponseBody
    List<TreeNode> getDeviceData(int catalog, int sapceId) {
        return service.getDeviceData(catalog, sapceId);
    }

    //分时能耗_仅可选一个
    @RequestMapping("/history/history_time")
    public @ResponseBody
    Map<String, Object> getHistoryTimeDataTwo(CommonMonitorDataVo vo, int ids) {
//   	Integer id = Integer.parseInt(ids);
        return service.getHistoryTimeData(vo, ids);
    }

    @RequestMapping("/energy_show")
    public @ResponseBody
    Map<String, Object> getHistoryTimeData(CommonMonitorDataVo vo, int ids) {

        return service.getHistoryTimeDataTwo(vo, ids);
    }

    @RequestMapping("/energy_show_tu")
    public @ResponseBody
    Map<String, Object> getHistoryTimeDataOne(CommonMonitorDataVo vo, int ids) {

        return service.getHistoryTimeDataOne(vo, ids);
    }

    @RequestMapping("/energy_show_seq")
    //分时能耗_仅可选一个_并按照不同的维度进行排序——时间
    public @ResponseBody
    Map<String, Object> getHistoryTimeSeqValue(CommonMonitorDataVo vo, int ids, int seq, int page, int size) {
        List<Double> avg = new ArrayList<Double>();
        List<String> nameList = new ArrayList<String>();
        List<Integer> xulie = new ArrayList<Integer>();
        List<String> baifenbi = new ArrayList<String>();
        String name = null;
        Double value = 0.0d;
        Double shuzhi = null;
        Double energySum = 0.0d;
        Double energyZong = 0.0d;

        Map<String, Object> map = new HashMap<>();
        int i = 0 + size * (page - 1);
        if (seq == 1) {//按数值排序
            PageInfo<EnergyCountOne> ec = service.getHistoryTimeSeqValueXiang(vo, ids, seq, page, size);
            List<EnergyCountOne> list = ec.getList();
            for (EnergyCountOne energyCount : list) {
                value = (double) energyCount.getAvg();
                energySum = (double) (energySum + value);
                avg.add(value);
                name = energyCount.getName();
                nameList.add(name);
                i++;
                xulie.add(i);
            }
            for (int j = 0; j < list.size(); j++) {
                shuzhi = (double) avg.get(j);
//				 yy =(double) service.queryEnergyZongLiang(vo, ids);
                double ee = (double) (shuzhi / energySum * 100);
                energyZong = energyZong + ee;
                String t = String.format("%.1f", ee);
                String tt = t + "%";
                baifenbi.add(tt);
            }
            String energyZ = String.format("%.1f", energyZong);
            String energyz1 = energyZ + "%";
            baifenbi.add(energyz1);
            if (vo.getCatalog() == 34) {
                nameList.add("总用电");
            }
            if (vo.getCatalog() == 37) {
                nameList.add("总用水");
            }
            avg.add(energySum);
            map.put("value", avg);
            map.put("xAxis", nameList);
            map.put("rank", xulie);
            map.put("percentage", baifenbi);
            map.put("total", ec.getTotal());
        }
        return map;
    }

    @RequestMapping("/energyshow/export")
    public void exportTwo(CommonMonitorDataVo vo, int ids, int seq, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<EnergyCountOne> fenshi = service.getHistoryTimeSeqValueExXiang(vo, ids, seq);
        int i = 0;
        Double shuzhi = null;
        List<Double> avg = new ArrayList<Double>();
        List<String> zhanbi = new ArrayList<String>();
        List<Integer> xulie = new ArrayList<Integer>();
        List<String> liexing = new ArrayList<String>();
        Double value = 0.0d;
        String name = null;
        Double energySum = 0.0d;
        Double sumPercentage = 0.0d;
        String[] title = {"排名", "能源类型 ", "数值", "占比"};
        ExportExcel<EnergyShowBiao> ex = new ExportExcel<EnergyShowBiao>();
        List<EnergyShowBiao> dataset = new ArrayList<EnergyShowBiao>();
        for (EnergyCountOne energyCount : fenshi) {
            value = energyCount.getAvg();
            avg.add(value);
            energySum = energySum + value;
            name = energyCount.getName();
            liexing.add(name);
            i++;
            xulie.add(i);
        }
        xulie.add(i + 1);
        liexing.add(null);
        avg.add(energySum);
        for (int j = 0; j < fenshi.size(); j++) {
            shuzhi = (double) avg.get(j);
            Double percentage = (double) (shuzhi / energySum * 100);
            sumPercentage = sumPercentage + percentage;
            String percentageString = String.format("%.1f", percentage);
            String tt = percentageString + "%";
            zhanbi.add(tt);
        }
        String t = String.format("%.1f", sumPercentage);
        String tt = t + "%";
        zhanbi.add(tt);
        int size = fenshi.size();
        int length = size + 1;
        for (int j = 0; j < length; j++) {
            dataset.add(new EnergyShowBiao(xulie.get(j), liexing.get(j), avg.get(j), zhanbi.get(j)));
        }

        FileOutputStream out = new FileOutputStream(new File("E://用电分项能耗排名表格.xls"));
        HSSFWorkbook book = ex.exportExcel("能耗排名表格", title, dataset);
        book.write(out);
        out.close();
        JOptionPane.showMessageDialog(null, "导出成功!");
        System.out.println("excel导出成功！");
    }

    //能耗排名——总能耗排名
    @RequestMapping("/energy/energy_rank")
    public @ResponseBody
    Map<String, Object> getEnergyRank(CommonMonitorDataVo vo) {
        return service.getEnergyRank(vo);
    }

    //    能耗排名——总能耗分项展示
    @RequestMapping("/energy/energy_fenxiang")
    public @ResponseBody
    Map<String, Object> getEnergyFenXiang(CommonMonitorDataVo vo) {
        return service.getEnergyFenXiang(vo);
    }

    //    分时能耗展示
    @RequestMapping("/energy/energy_Type")
    public @ResponseBody
    List<EnergyCountOne> getEnergyType() {
        return service.queryEnergyType();
    }

    //能耗排名——总水量 总电量   耗水耗电占比
    @RequestMapping("/energy/energy_elecandwater")
    public @ResponseBody
    Map<String, Object> getEnergyElecAndWater(CommonMonitorDataVo vo) {
        String areaStr = configManager.getValue("energy.area");
        String personStr = configManager.getValue("energy.person");
        return service.getEnergyElecAndWater(vo, areaStr, personStr);
    }


    //能耗排名——总水量 总电量   耗水耗电占比  排名   两种
    @RequestMapping("/energy/energy_elecandwaterrank")
    public @ResponseBody
    Map<String, Object> getEnergyElecAndWaterRank(CommonMonitorDataVo vo, int page, @RequestParam(required = false, defaultValue = "8") int size) {

        return service.getEnergyElecAndWaterRank(vo, page, size);
    }

    //能耗排名——能耗展示排名
    @RequestMapping("/energy/energy_allRank")
    public @ResponseBody
    Map<String, Object> getEnergyAllRank(CommonMonitorDataVo vo, String rankType, String rank, int page, @RequestParam(required = false, defaultValue = "8") int size) {
        List<RankEnergy> cloumnList = service.getEnergyAllRank(vo, rankType, rank);
        if (cloumnList.size() > 0) {
            int xulie = 1;
            for (RankEnergy list : cloumnList) {
                list.setXulie(xulie);
                xulie++;
            }
        }
        //分页
        Map<String, Object> map = new HashMap<>();
        int total = 0;
        if (cloumnList != null && cloumnList.size() > 0) {
            total = cloumnList.size();
            int pagesize = size; //每页条数
            int currentPage = page;//第几页 传参page
            int totalcount = cloumnList.size();
            int pagecount = 0; //总页数
            List<RankEnergy> subList;
            int m = totalcount % pagesize;
            if (m > 0) {
                pagecount = totalcount / pagesize + 1;
            } else {
                pagecount = totalcount / pagesize;
            }
            if (m == 0) {
                subList = cloumnList.subList((currentPage - 1) * pagesize, pagesize * (currentPage));
            } else {
                if (currentPage == pagecount) {
                    subList = cloumnList.subList((currentPage - 1) * pagesize, totalcount);
                } else {
                    subList = cloumnList.subList((currentPage - 1) * pagesize, pagesize * (currentPage));
                }
            }
            map.put("total", cloumnList.size());
            map.put("value", subList);
        }
        return map;
    }

    @RequestMapping("/energy/energy_elecandwaterexcel")
    @ResponseBody
    public void getEnergyElecAndWaterRankExcel(CommonMonitorDataVo vo, String rankType, String rank, HttpServletResponse response) throws Exception {

        List<RankEnergy> list = service.getEnergyElecAndWaterRankExcle(vo);
        List<RankEnergy> rowList = ListSortUtil.sort(list, rankType, rank);
        if (rowList.size() > 0) {
            int xulie = 1;
            for (RankEnergy ls : rowList) {
                ls.setXulie(xulie);
                xulie++;
            }
        }
        String[] title = {"排名", "建筑楼层 ", "综合耗能", "总用电量", "照明用电", "空调用电", "特殊用电", "其他用电", "动力用电", "总用水量", "生活用水", "生活污水", "空调用水", "消防用水", "其他用水"};
        ExportExcel<RankEnergy> ex = new ExportExcel<RankEnergy>();
        FileOutputStream out = new FileOutputStream(new File("E://能耗排名表格.xls"));
//    	 OutputStream os = response.getOutputStream();// 取得输出流   
//    	 response.reset();// 清空输出流   
//    	 response.setHeader("Content-disposition", "attachment; filename="+new String("能耗排名表格".getBytes("GB2312"),"8859_1")+".xls");// 设定输出文件头   
//    	 response.setContentType("application/msexcel");// 定义输出类型 
//    	 
        HSSFWorkbook book = ex.exportExcel("能耗排名表格", title, rowList);
        book.write(out);
        out.close();
        JOptionPane.showMessageDialog(null, "导出成功!");
        System.out.println("excel导出成功！");
    }

    /**
     * lxx 返回水电类型  父子关系
     */
    @RequestMapping("/energy/energy_getType")
    public @ResponseBody
    List<EnergyType> getTypeList() {
        return service.getTypeList();
    }

    /**
     * lxx 返回水电类型  父子关系
     */
    @RequestMapping("/energy/energyProportion")
    public @ResponseBody
    Map<String, Object> energyProportion() {

        return service.energyProportion();
    }


}
