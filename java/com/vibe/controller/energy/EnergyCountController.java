package com.vibe.controller.energy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.JOptionPane;

import com.vibe.pojo.energy.*;
import com.vibe.util.DateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.NativeMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.vibe.mapper.energy.EnergyCountMapper;
import com.vibe.poiutil.ExportExcel;
import com.vibe.pojo.CommonMonitorDataVo;
import com.vibe.pojo.user.User;
import com.vibe.service.energy.EnergyCountService;
import com.vibe.util.ListSortUtil;
import com.vibe.utils.ExcelExportUtil;
import com.vibe.utils.PropertiesUtil;

@Controller
@RequestMapping("/energyCount")
public class EnergyCountController {

    @Autowired
    private EnergyCountService energyCountService;

    //	@Autowired
//	private EnergyCountMapper energyCountMapper;
//	
    private final Integer COUNTRY_STANDARD = 2;//国家标准
    private final Integer JIANZHUQUN = 1;


    @RequestMapping(value = "/energy/elec", method = RequestMethod.GET)
    @ResponseBody//parent表示传入的层 0表示一座楼全部楼层  standard 1参考标椎 2国家标准
    public Map<String, Object> queryEnergyCountByProbeId(Integer lou, String moment, Integer standard, Integer type, Integer parent,
                                                         int page, @RequestParam(required = false, defaultValue = "8") int size) {
        if (type == 46) {
            type = 34;
        }
        if (lou == JIANZHUQUN) {//a3楼
            String date = moment + "%";
            List<Integer> idParent = energyCountService.queryEnergyCountByProbeIdParent(parent, lou);
            Map<String, Object> map = new HashMap<>();
            //返回所有楼层 每天的数据
            List<List<Object>> list = null;
            list = energyCountService.queryEnergyCountByProbeId(type, date, idParent, standard);
            //排序分页 前端出入page 页码
            int total = 0;
            if (list != null && !list.isEmpty()) {
                map.put("white", list.get(0));
                list.remove(0);
                total = list.size();
                int pagesize = size; //每页条数
                int currentPage = page;//第几页 传参page
                int totalcount = list.size();
                int pagecount = 0; //总页数
                List<List<Object>> subList;
                int m = totalcount % pagesize;
                if (m > 0) {
                    pagecount = totalcount / pagesize + 1;
                } else {
                    pagecount = totalcount / pagesize;
                }
                if (m == 0) {
                    subList = list.subList((currentPage - 1) * pagesize, pagesize * (currentPage));
                } else {
                    if (currentPage == pagecount) {
                        subList = list.subList((currentPage - 1) * pagesize, totalcount);
                    } else {
                        subList = list.subList((currentPage - 1) * pagesize, pagesize * (currentPage));
                    }
                }
                map.put("total", total);
                map.put("value", subList);
                return map;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * @author lxx
     */
    //用电异常突增诊断 表格导出
    @RequestMapping(value = "/energy/elec/export", method = RequestMethod.GET)
    @ResponseBody
    public void elecExport(Integer lou, String moment, Integer standard, Integer type, Integer parent, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (type == 46) {
            type = 34;
        }
        if (lou == JIANZHUQUN) {
            String date = moment + "%";
            List<Integer> lightList = new ArrayList<>();
            List<Integer> idParent = energyCountService.queryEnergyCountByProbeIdParent(parent, lou);
            //返回所有楼层 每天的数据
            List<List<Object>> list = energyCountService.queryEnergyCountByProbeId(type, date, idParent, standard);
            String[] header = list.get(0).toArray(new String[list.get(0).size()]);
            list.remove(0);
            ExcelExportUtil ee = new ExcelExportUtil();
            ee.exportExcel(list, header, moment + "电量水量异常突增诊断表格", response);
            System.out.println("excel导出成功！");
        }


    }

    /**
     * 水电夜间诊断   当天晚上11:00到第二天 早6:00
     *
     * @param lou
     * @param moment
     * @param standard
     * @param type
     * @param parent
     * @return
     * @author lxx
     */
    @RequestMapping(value = "/energy/night", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> queryEnergyNight(Integer lou, String moment, Integer standard, Integer type, Integer parent,
                                                int page, @RequestParam(required = false, defaultValue = "8") int size) {
        if (type == 46) {
            type = 34;
        }
        if (lou == JIANZHUQUN) {//只查询A3楼
            List<Integer> idParent = energyCountService.queryEnergyCountByProbeIdParent(parent, lou);
            Map<String, Object> map = new HashMap<>();
            //返回所查楼层 一个月中每天的数据
            List<List<Object>> list = energyCountService.queryEnergyCountById(type, moment, idParent, standard);
            //排序分页   前端传入page 页码
            if (list != null && list.size() > 0) {
                map.put("white", list.get(0));
                list.remove(0);
                int pagesize = size;
                int currentPage = page;
                int totalcount = list.size();
                int pagecount = 0;
                List<List<Object>> subList;
                int m = totalcount % pagesize;
                if (m > 0) {
                    pagecount = totalcount / pagesize + 1;
                } else {
                    pagecount = totalcount / pagesize;
                }
                if (m == 0) {
                    subList = list.subList((currentPage - 1) * pagesize, pagesize * (currentPage));
                } else {
                    if (currentPage == pagecount) {
                        subList = list.subList((currentPage - 1) * pagesize, totalcount);
                    } else {
                        subList = list.subList((currentPage - 1) * pagesize, pagesize * (currentPage));
                    }
                }
                map.put("total", totalcount);
                map.put("value", subList);
                return map;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * @author lxx
     */
    //夜间数据导出
    @RequestMapping(value = "/energy/night/export", method = RequestMethod.GET)
    @ResponseBody
    public void queryEnergyNightExport(Integer lou, String moment, Integer standard, Integer type, Integer parent, HttpServletResponse response) {
        if (type == 46) {
            type = 34;
        }
        if (lou == JIANZHUQUN) {//只查询A3楼
            List<Integer> lightList = new ArrayList<>();
            List<Integer> idParent = energyCountService.queryEnergyCountByProbeIdParent(parent, lou);
            //返回所查楼层 一个月中每天的数据
            List<List<Object>> list = energyCountService.queryEnergyCountById(type, moment, idParent, standard);
            String[] header = list.get(0).toArray(new String[list.get(0).size()]);
            list.remove(0);
            ExcelExportUtil ee = new ExcelExportUtil();
            ee.exportExcel(list, header, moment + "夜间电量水量表格", response);
            System.out.println("excel导出成功！");
        }
    }


    @RequestMapping(value = "/energy/water", method = RequestMethod.GET)
    @ResponseBody
    public Map queryEnergyCountByProbeIdWater(Integer lou, String moment, Integer standard, Integer type, Integer parent) {
        if (lou == JIANZHUQUN) {
            String date = moment + "%";
            List<Integer> lightList = new ArrayList<>();

            return null;
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/energy/waternight", method = RequestMethod.GET)
    @ResponseBody
    public Map queryEnergyCountByProbeIdWaterNight(Integer lou, String moment, Integer standard, Integer type, Integer parent) throws ParseException {
        if (lou == JIANZHUQUN) {
            String time = moment + "%";
            String startTime = moment + " 07:00:00";

            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sf.parse(moment);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(calendar.DATE, 1);
            Date endDate = calendar.getTime();

            String endTime = sf.format(endDate) + " 08:00:00";
            System.out.println("calendar: " + calendar.getTime());
            System.out.println("stime: " + startTime + "  " + "endtime: " + endTime);
            List<Integer> idList = energyCountService.queryEnergyCountByMonitorId(type);
            PageInfo<EnergyCountOne> pageInfo = energyCountService.queryNightEnergyCountByProbeId(idList, startTime, endTime, parent);
            List<EnergyCountOne> list = pageInfo.getList();
            List<EnergyCountOne> avgList = energyCountService.queryNightEnergyCountByProbeIdAvg(idList, startTime, endTime, parent);//应该是夜间的平均值
//////Could not write JSON: Cannot format given Object as a Number; nested excepti
//////无法写入JSON:无法将给定对象格式化为数字；嵌套异常
            Map<Integer, Double> avgMap = avgList.stream().collect(Collectors.toMap(EnergyCountOne::getMonitor, EnergyCountOne::getAvgNumNight));
            list = list.stream().map(p -> {
                p.setAvgNumNight(avgMap.get(p.getMonitor()));
                return p;
            }).sorted((p1, p2) -> p1.getMonitor().compareTo(p2.getMonitor())).collect(Collectors.toList());
            Map<Integer, List<EnergyCountOne>> collect1 = list.stream().collect(Collectors.groupingBy(EnergyCountOne::getParent));
            Map<Integer, List<EnergyCountOne>> treeMap = new TreeMap<>(collect1);

            System.out.println("currentPage: " + pageInfo.getPageNum());
            System.out.println("size: " + pageInfo.getPageSize());
            List<String> nameList = new ArrayList<>();
            List<String> valueList = new ArrayList<>();
            nameList.add("countryStandardA3");
            if (standard == COUNTRY_STANDARD) { //如果是国家标准
                valueList = PropertiesUtil.getPropertiesValueList(nameList);


            }
            return treeMap;
        } else {
            return null;
        }
    }

    @RequestMapping(value = "/energy/elecnight", method = RequestMethod.GET)
    @ResponseBody
    public Map queryEnergyCountByProbeIdElecNight(Integer lou, String moment, Integer standard, Integer type, Integer parent) throws ParseException {
        if (lou == JIANZHUQUN) {
            String time = moment + "%";
            String startTime = moment + " 07:00:00";

            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sf.parse(moment);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(calendar.DATE, 1);
            Date endDate = calendar.getTime();

            String endTime = sf.format(endDate) + " 08:00:00";
            System.out.println("calendar: " + calendar.getTime());
            System.out.println("stime: " + startTime + "  " + "endtime: " + endTime);
            List<Integer> idList = energyCountService.queryEnergyCountByMonitorId(type);
            //lxx根据传入的id去查询楼层
            //List<Integer> idParent = energyCountService.queryEnergyCountByProbeIdParent(parent);

            PageInfo<EnergyCountOne> pageInfo = energyCountService.queryNightEnergyCountByProbeId(idList, startTime, endTime, parent);
            List<EnergyCountOne> list = pageInfo.getList();
            List<EnergyCountOne> avgList = energyCountService.queryNightEnergyCountByProbeIdAvg(idList, startTime, endTime, parent);//应该是夜间的平均值
//////
////
//////Could not write JSON: Cannot format given Object as a Number; nested excepti
//////无法写入JSON:无法将给定对象格式化为数字；嵌套异常
            Map<Integer, Double> avgMap = avgList.stream().collect(Collectors.toMap(EnergyCountOne::getMonitor, EnergyCountOne::getAvgNumNight));
//		list = list.stream().map(p -> {
//			
//		p.setAvgNumNight(avgMap.get(p.getMonitor()));
//			return p;
//		}).collect(Collectors.toList());
//		List<String> nameList = new ArrayList<>();                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              
//		
//		List<String> valueList = new ArrayList<>();
//		nameList.add("countryStandardA3");
//		if(standard == COUNTRY_STANDARD){ //如果是国家标准
//			valueList = PropertiesUtil.getPropertiesValueList(nameList);
//		}
//		Map map = new HashMap<>();
//		map.put("list", list);
//		map.put("countryStandard", valueList);
//		return map;
            list = list.stream().map(p -> {
                p.setAvgNumNight(avgMap.get(p.getMonitor()));
                return p;
            }).sorted((p1, p2) -> p1.getMonitor().compareTo(p2.getMonitor())).collect(Collectors.toList());
            Map<Integer, List<EnergyCountOne>> collect1 = list.stream().collect(Collectors.groupingBy(EnergyCountOne::getParent));
            Map<Integer, List<EnergyCountOne>> treeMap = new TreeMap<>(collect1);

            System.out.println("currentPage: " + pageInfo.getPageNum());
            System.out.println("size: " + pageInfo.getPageSize());
            List<String> nameList = new ArrayList<>();
            List<String> valueList = new ArrayList<>();
            nameList.add("countryStandardA3");
            if (standard == COUNTRY_STANDARD) { //如果是国家标准
                valueList = PropertiesUtil.getPropertiesValueList(nameList);


            }
            return treeMap;
        } else {
            return null;
        }
    }


    //能耗对比分析————空间对比   图
    @RequestMapping("/energy/energy_speceTu")
    public @ResponseBody
    List<List<Energy>> getEnergySpaceTu(CommonMonitorDataVo vo, String ids) {
        List<List<Energy>> energy = energyCountService.getEnergySpaceTu(vo, ids);

        return energy;

    }

    /**
     * lxx
     *
     * @param vo
     * @param ids
     * @param size
     * @param page
     * @return
     */
    //能耗对比分析————空间对比   表,    ids表示楼层     selectType;//年，月，日，时  1,2,3,4    redioType;//0单个，1范围
    @RequestMapping("/energy/energy_speceBiao")
    public @ResponseBody
    Map<String, Object> getEnergySpaceBiao(CommonMonitorDataVo vo, String ids, @RequestParam(required = false, defaultValue = "8") int size, int page) {
        Map<String, Object> energy = energyCountService.getEnergySpaceBiao(vo, ids, size, page);

        return energy;
    }

    /**
     * lxx
     *
     * @param vo
     * @param ids
     * @param response
     */

    @RequestMapping("/energy/energy_speceExcle")
    public void getEnergySpaceExcle(CommonMonitorDataVo vo, String ids, HttpServletResponse response) {
        String moment = vo.getStartTime();
        Map<String, Object> map = energyCountService.getEnergySpaceExcle(vo, ids);
        if (!map.isEmpty()) {
            List<List<Object>> list = (List<List<Object>>) map.get("value");
            List<String> titelList = (List<String>) map.get("titel");

            String[] header = titelList.toArray(new String[titelList.size()]);
            ExcelExportUtil ee = new ExcelExportUtil();
            ee.exportExcel(list, header, "楼层总能耗排名表格", response);
            System.out.println("excel导出成功！");
        }

    }

    //查询A3楼层 所有楼层
    @RequestMapping("/energy/energy_A3floor")
    public @ResponseBody
    List<Energy> getEnergyA3Floorid(HttpServletRequest request) {
        List<Energy> floorId = energyCountService.getEnergyA3Floorid();
//    	User user = (User) request.getSession().getAttribute("loginUser");
//    	String name = user.getLogin_id();
        return floorId;
    }


    //能耗对比分析————同比环比对比  
    @RequestMapping("/energy/energy_comseqTu")
    //selectType 2只传月份  redioType 范围 catalog  水 37 电 34 floorId 表示楼层   parent 默认1 表示A3  时间
    @ResponseBody
    public Map<String, Object> getEnergyComAndSeq(CommonMonitorDataVo vo, Integer floorId) {

        Map<String, Object> map = energyCountService.getEnergyComAndSeq(vo, floorId);
        return map;
    }

    //能耗对比分析————同比环比对比   数据表格
    @RequestMapping("/energy/energy_comseqBiao")
    //selectType 2只传月份  redioType 范围 catalog  水 37 电 34 floorId 表示楼层   parent 默认1 表示A3  时间
    @ResponseBody
    public Map<String, Object> getEnergyComAndSeqBiao(CommonMonitorDataVo vo, String rankType, String rank, Integer floorId, int page, @RequestParam(required = false, defaultValue = "8") int size) {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> vuMap = energyCountService.getEnergyComAndSeq(vo, floorId);
        List<ComSeqEnergy> rowList = (List<ComSeqEnergy>) vuMap.get("value");
        //排序
        List<ComSeqEnergy> list = ListSortUtil.sort(rowList, rankType, rank);
        //保留两位小数

        //分页
        int total = 0;
        if (list != null && !list.isEmpty()) {
            int xulie = 1;
            for (ComSeqEnergy ls : list) {
                ls.setXulie(xulie);
                xulie++;
            }
            total = list.size();
            int pagesize = size; //每页条数
            int currentPage = page;//第几页 传参page
            int totalcount = list.size();
            int pagecount = 0; //总页数
            List<ComSeqEnergy> subList;
            int m = totalcount % pagesize;
            if (m > 0) {
                pagecount = totalcount / pagesize + 1;
            } else {
                pagecount = totalcount / pagesize;
            }
            if (m == 0) {
                subList = list.subList((currentPage - 1) * pagesize, pagesize * (currentPage));
            } else {
                if (currentPage == pagecount) {
                    subList = list.subList((currentPage - 1) * pagesize, totalcount);
                } else {
                    subList = list.subList((currentPage - 1) * pagesize, pagesize * (currentPage));
                }
            }
            if (subList != null && !subList.isEmpty()) {
                DecimalFormat df = new DecimalFormat("0.00");
                for (ComSeqEnergy sub : subList) {
                    sub.setDqzh(df.format(Double.parseDouble(sub.getDqzh().toString())));
                    sub.setSqzh(df.format(Double.parseDouble(sub.getSqzh().toString())));
                    sub.setTqzh(df.format(Double.parseDouble(sub.getTqzh().toString())));
                }
            }
            map.put("unit", vuMap.get("unit"));
            map.put("total", total);
            map.put("value", subList);
            return map;
        } else {
            return null;
        }
    }

    //能耗对比分析————同比环比对比    表格导出
    @RequestMapping("/energy/energy_comseqExcel")
    //selectType 2只传月份  redioType 范围 catalog  水 37 电 34 floorId 表示楼层   parent 默认1 表示A3  时间
    @ResponseBody
    public void getEnergyComAndSeqExcel(CommonMonitorDataVo vo, String rankType, String rank, Integer floorId, HttpServletResponse response) throws Exception {

        Map<String, Object> vuMap = energyCountService.getEnergyComAndSeq(vo, floorId);
        List<ComSeqEnergy> rowList = (List<ComSeqEnergy>) vuMap.get("value");
        //排序
        List<ComSeqEnergy> list = ListSortUtil.sort(rowList, rankType, rank);
        if (list != null && !list.isEmpty()) {
            DecimalFormat df = new DecimalFormat("0.00");
            for (ComSeqEnergy sub : list) {
                sub.setDqzh(df.format(Double.parseDouble(sub.getDqzh().toString())));
                sub.setSqzh(df.format(Double.parseDouble(sub.getSqzh().toString())));
                sub.setTqzh(df.format(Double.parseDouble(sub.getTqzh().toString())));
            }
        }
        int xulie = 1;
        for (ComSeqEnergy ls : list) {
            ls.setXulie(xulie);
            xulie++;
        }
        String[] title = {"排名", "时间 ", "当期综合能耗", "同期综合能耗", "上期综合能耗", "综合能耗同比增长(%)", "综合能耗环比增长(%)"};
        ExportExcel<ComSeqEnergy> ex = new ExportExcel<ComSeqEnergy>();
        OutputStream os = response.getOutputStream();// 取得输出流
        response.reset();// 清空输出流                     
        String fileName = "总用量排名表格" + ".xls";
        response.setContentType("application/x-msdownload");// 设定输出文件类型
        response.setHeader("Content-Disposition",
                URLEncoder.encode(fileName, "UTF-8"));
        HSSFWorkbook book = ex.exportExcel("总用量排名表格", title, rowList);
        book.write(os);
        os.close();
        //JOptionPane.showMessageDialog(null, "导出成功!");

    }

    //能耗对比分析————能耗类型对比    图

    /**
     * lxx    ids表示传的能源类型
     */
    @RequestMapping("/energy/energy_typeTu")   //44 45   parent 1 表示A3楼id 
    public @ResponseBody
    List<List<Energy>> getEnergyTypeTu(CommonMonitorDataVo vo, String ids, Integer floorId) {
        List<List<Energy>> list = energyCountService.getEnergyType(vo, ids, floorId);

        return list;

    }

    //能耗对比分析————能耗类型对比    表格

    /**
     * lxx    ids表示传的能源类型
     */
    @RequestMapping("/energy/energy_typeBiao")   //44 45   表示A3楼id   catlog 总类型 水或者电
    public @ResponseBody
    Map<String, Object> getEnergyTypeBiao(CommonMonitorDataVo vo, String ids, Integer floorId
            , int page, @RequestParam(required = false, defaultValue = "8") int size) {
        Map<String, Object> map = energyCountService.getEnergyTypeSort(vo, ids, floorId, page, size);

        return map;

    }
    //能耗对比分析————能耗类型对比    表格导出

    /**
     * lxx    ids表示传的能源类型
     */
    @RequestMapping("/energy/energy_typeBiaoExcel")   //44 45  表示A3楼id   catlog 总类型 水或者电
    public void getEnergyTypeBiaoExcel(CommonMonitorDataVo vo, String ids, Integer floorId, HttpServletResponse response) {
        List<List<Energy>> list = energyCountService.getEnergyTypeSortExcel(vo, ids, floorId);
        List<List<Object>> columnList = new ArrayList<>();
        List<String> titelList = new ArrayList<>();
        if (list != null && !list.isEmpty()) {
            int lsize = 0;
            for (List<Energy> ls : list) {
                if (ls != null && ls.size() > 0) {
                    titelList.add(ls.get(0).getName() + ls.get(0).getUnit());
                    titelList.add("占比%");
                    lsize = ls.size();
                }
            }
            for (int i = 0; i < lsize; i++) {
                List<Object> rowList = new ArrayList<>();
                for (List<Energy> ls : list) {
                    //rowList.add(ls.get(i).getXulie());
                    if (ls != null && ls.size() > 0) {
                        if (Double.parseDouble(ls.get(i).getValue().toString()) < 0) {
                            rowList.add("故障");
                            rowList.add("未知");
                        } else {
                            rowList.add(ls.get(i).getValue());
                            rowList.add(ls.get(i).getZhanbi());
                        }
                    }
                }
                if (list.get(0) != null && !list.get(0).isEmpty()) {
                    rowList.add(0, list.get(0).get(i).getXulie());
                    rowList.add(1, list.get(0).get(i).getTime());
                } else {
                    rowList.add(0, list.get(1).get(i).getXulie());
                    rowList.add(1, list.get(1).get(i).getTime());
                }

                columnList.add(rowList);
            }

            titelList.add(0, "排名");
            titelList.add(1, "时间");
        }
        String[] header = titelList.toArray(new String[titelList.size()]);
        ExcelExportUtil ee = new ExcelExportUtil();
        ee.exportExcel(columnList, header, "能耗对比排名", response);
        System.out.println("excel导出成功！");


    }

    /**
     * lxx
     * 分时能耗   图
     */
    @RequestMapping("/energy/energy_fenshiTu")   //   catalog 总类型 水或者电
    public @ResponseBody
    List<EnergyCount> getEnergyFenShiTu(CommonMonitorDataVo vo, Integer floor) {
        List<EnergyCount> list = energyCountService.getEnergyFenShiTu(vo, floor);

        return list;

    }

    //  分时能耗   表格
    @RequestMapping("/energy/energy_fenshiBiao")   //seq排序 0表示时间1 表示 数值  catalog 总类型 水或者电
    public @ResponseBody
    Map<String, Object> getEnergyFenShiBiao(CommonMonitorDataVo vo, Integer floor
            , int page, @RequestParam(required = false, defaultValue = "8") int size, int seq, String rank) {
        Map<String, Object> map = energyCountService.getEnergyFenShiBiao(vo, floor, page, size, seq, rank);

        return map;

    }

    //  分时能耗   表格
    @RequestMapping("/energy/energy_fenshiBiaoExcel")   //seq排序 0表示时间 1 表示 数值  catalog 总类型 水或者电
    public void getEnergyFenShiBiaoExcel(CommonMonitorDataVo vo, Integer floor, int seq, String rank, HttpServletResponse response) throws Exception {
        List<EnergyBiao> rowList = energyCountService.getEnergyFenShiBiaoExcel(vo, floor, seq, rank);
        String[] title = {"排名", "时间 ", "综合能耗"};
        ExportExcel<EnergyBiao> ex = new ExportExcel<EnergyBiao>();
        OutputStream os = response.getOutputStream();// 取得输出流
        response.reset();// 清空输出流                     
        String fileName = "能耗用量排名表格" + ".xls";
        response.setContentType("application/x-msdownload");// 设定输出文件类型
        response.setHeader("Content-Disposition",
//                "attachment;filename=" + new String( fileName.getBytes("gb2312"), "ISO8859-1" )); //设定文件输出头
                URLEncoder.encode(fileName, "UTF-8")); //设定文件输出头
        HSSFWorkbook book = ex.exportExcel("sheet", title, rowList);
        book.write(os);
        os.close();
    }

    /**
     * lxx
     * 分项能耗   图
     */
    @RequestMapping("/energy/energy_fenxiangTu")   //   catalog 总类型 水或者电
    public @ResponseBody
    List<Energy> getEnergyFenXiangTu(CommonMonitorDataVo vo, Integer floor) {
        List<Energy> list = energyCountService.getEnergyFenXiangTu(vo, floor);

        return list;

    }

    @RequestMapping("/energy/energy_fenxiangBiao")   //   catalog 总类型 水或者电
    public @ResponseBody
    Map<String, Object> getEnergyFenXiangBiao(CommonMonitorDataVo vo, Integer floor
            , int page, @RequestParam(required = false, defaultValue = "8") int size) {
        Map<String, Object> map = energyCountService.getEnergyFenXiangBiao(vo, floor, page, size);

        return map;

    }

    @RequestMapping("/energy/energy_fenxiangBiaoExcel")   //   catalog 总类型 水或者电
    public void getEnergyFenXiangBiaoExcel(CommonMonitorDataVo vo, Integer floor, HttpServletResponse response) throws Exception {
        List<EnergySub> rowList = energyCountService.getEnergyFenXiangBiaoExcel(vo, floor);
        String[] title = {"排名", "能耗类型", "数值", "占比"};
        ExportExcel<EnergySub> ex = new ExportExcel<EnergySub>();
        OutputStream os = response.getOutputStream();// 取得输出流
        response.reset();// 清空输出流                     
        String fileName = "分项展示能耗排名表格" + ".xls";
        response.setContentType("application/x-msdownload");// 设定输出文件类型
        response.setHeader("Content-Disposition",
//                "attachment;filename=" + new String( fileName.getBytes("gb2312"), "ISO8859-1" )); //设定文件输出头
                URLEncoder.encode(fileName, "UTF-8"));
        HSSFWorkbook book = ex.exportExcel("sheet", title, rowList);
        book.write(os);
        os.close();

    }

    /*
     * 展示底表的示数
     * @author lxx
     */
    @RequestMapping("/energy/getBaseRepresentationNumber")
    @ResponseBody
    public Map<String, Object> getBaseRepresentationNumber(@RequestParam(required = true) int page,
                                                           @RequestParam(required = false, defaultValue = "12") int size,
//    													@RequestParam(required = true)int floor,
                                                           @RequestParam(required = true) int catalog,
//    													@RequestParam(required = true)int type,//周  年月日  0.1.2.3
//    													@RequestParam(required = true)int week) {//0 表示周一，1 表示周二，……，6 表示周日 
                                                           @RequestParam(required = true) String endTime,
                                                           @RequestParam(required = true) String startTime) {
        Map<String, Object> map = energyCountService.getBaseRepresentationNumber(page, size, catalog, endTime, startTime);

        return map;
    }

    /**
     * 导出
     *
     * @param catalog
     * @param endTime
     * @param startTime
     * @throws IOException
     */
    @RequestMapping("/energy/getBaseRepresentationNumberExcel")
    public void getBaseRepresentationNumberExcel(HttpServletResponse response,
                                                 @RequestParam(required = true) int catalog,
                                                 @RequestParam(required = true) String endTime,
                                                 @RequestParam(required = true) String startTime) throws IOException {
        List<BaseRepresentationNumber> list = energyCountService.getBaseRepresentationNumberExcel(catalog, endTime, startTime);
        String[] title = {"序号", "时间", "表名称", "数值", "所属空间"};
        ExportExcel<BaseRepresentationNumber> ex = new ExportExcel<BaseRepresentationNumber>();
        OutputStream os = response.getOutputStream();// 取得输出流
        response.reset();// 清空输出流                     
        String fileName = "表底数" + ".xls";
        response.setContentType("application/x-msdownload");// 设定输出文件类型
        response.setHeader("Content-Disposition",
//                "attachment;filename=" + new String( fileName.getBytes("gb2312"), "ISO8859-1" )); //设定文件输出头
                URLEncoder.encode(fileName, "UTF-8"));
        HSSFWorkbook book = ex.exportExcel1("sheet", title, list);
        book.write(os);
        os.close();

    }

    /*
     * 按月份查出表底数，上月份最后一时刻表数
     * @author lxx
     */
    @RequestMapping("/energy/getLastNumber")
    @ResponseBody
    public Map<String, Object> getLastNumber(@RequestParam(required = true) int page,
                                             @RequestParam(required = false, defaultValue = "12") int size,
                                             @RequestParam(required = true) int catalog) {
        Map<String, Object> map = energyCountService.getLastNumber(page, size, catalog);

        return map;
    }

    /**
     * 导出
     *
     * @param catalog
     * @throws IOException
     */
    @RequestMapping("/energy/getLastNumberExcel")
    public void getLastNumberExcel(HttpServletResponse response,
                                   @RequestParam(required = true) int catalog) throws IOException {
        List<BaseNumber> list = energyCountService.getLastNumberExcel(catalog);
        String[] title = {"年度", "月份", "电表号", "电表名称", "本次表示数"};
        ExportExcel<BaseNumber> ex = new ExportExcel<BaseNumber>();
        OutputStream os = response.getOutputStream();// 取得输出流energy/night
        response.reset();// 清空输出流
        String fileName = DateUtil.getLastMonthOfDay() + "表底数" + ".xls";
        response.setContentType("application/x-msdownload");// 设定输出文件类型
        response.setHeader("Content-Disposition",
                URLEncoder.encode(fileName, "UTF-8"));
        HSSFWorkbook book = ex.exportExcel1("sheet", title, list);
        book.write(os);
        os.close();

    }

}