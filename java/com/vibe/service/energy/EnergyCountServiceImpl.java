package com.vibe.service.energy;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.enterprise.inject.New;

import com.vibe.pojo.energy.*;
import com.vibe.util.DataStatisticsUtils;
import com.vibe.util.ScaleUtil;
import com.vibe.util.StringUtils;
import com.vibe.utils.EnergyTreeNode;
import org.apache.commons.math3.analysis.function.Floor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.serotonin.bacnet4j.type.primitive.Time;
import com.vibe.mapper.energy.EnergyCountMapper;
import com.vibe.pojo.CommonMonitorDataVo;
import com.vibe.pojo.Data;
import com.vibe.util.DateUtil;
import com.vibe.utils.PropertiesUtil;

@Service
public class EnergyCountServiceImpl implements EnergyCountService {
    @Autowired
    private EnergyCountMapper energyCountMapper;


    @Override
    public List<List<Object>> queryEnergyCountByProbeId(Integer type, String date, List idParentList, Integer standard) {
        List<List<Object>> floorList = new ArrayList<>();
//		 DecimalFormat df = new DecimalFormat("0.00");
        boolean isFirst = true;
        //查询出每个楼层的数据   每天
        for (int i = 0; i < idParentList.size(); i++) {
            List<Object> rowList = new ArrayList<>();
            Integer parent = (Integer) idParentList.get(i);
            List<EnergyCountOne> list = energyCountMapper.queryEnergyCountByProbeId(type, date, parent);//按日期排序
            if (isFirst && list.size() > 0 && !list.isEmpty()) {
                List<Object> rowFirst = new ArrayList<>();
                rowFirst.add("建筑楼层");
                rowFirst.add("参考指标");
                for (int f = 0; f < list.size(); f++) {
                    rowFirst.add(list.get(f).getMoment());
                }
                floorList.add(rowFirst);
                isFirst = false;
            }
            //参考指标
            //根据传过来的standard类型添加参考指标的值
            String num = "未定义";
            if (standard == 1 && !list.isEmpty() && list.size() > 0) {
                List<Double> arrList = new ArrayList();
                try {
                    for (EnergyCountOne l : list) {
                        if (!DateUtil.isWeekend(l.getMoment())) {
                            arrList.add(l.getAvg());
                        }
                    }
                    double[] arr = new double[arrList.size()];
                    for (int a = 0; a < arrList.size(); a++) {
                        arr[a] = arrList.get(a);
                    }
                    num = ScaleUtil.getRate((Math.round(DataStatisticsUtils.getTrimmedMean(arr, 20) * 100) / 100) * 1.182);
                } catch (Exception e) {
                    e.printStackTrace();
                }
//				Double avgNum = energyCountMapper.queryEnergyCountByProbeIdAvg(type, date,parent);//计算平均值   参考指标
//				if(avgNum == null) {
//					avgNum = 0.0;
//				}
//				num = ScaleUtil.getRate(((double) Math.round(avgNum * 100) / 100)*1.182);
            } else {
                //国家标准80瓦每平方米

            }
            //循环 同一楼层
            if (list != null && !list.isEmpty()) {
                rowList.add(list.get(0).getCaption());
                rowList.add(num);
                bgm:
                for (int ls = 2; ls < floorList.get(0).size(); ls++) {
                    for (int row = 0; row < list.size(); row++) {
                        if (list.get(row).getMoment().equals(floorList.get(0).get(ls))) {
                            rowList.add(ScaleUtil.getRate(list.get(row).getAvg()));
                            continue bgm;
                        }
                    }
                    rowList.add(ScaleUtil.getRate(0.0));
                }
                floorList.add(rowList);
            }
        }
        if (floorList != null && !floorList.isEmpty()) {
            for (int d = 2; d < floorList.get(0).size(); d++) {
                try {
                    if (DateUtil.isWeekend(floorList.get(0).get(d).toString())) {
                        floorList.get(0).set(d, floorList.get(0).get(d) + "(周末)");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return floorList;
    }


    @Override
    public PageInfo<EnergyCountOne> queryNightEnergyCountByProbeId(List lightList, String sTime, String eTime, Integer parent) {
        PageHelper.startPage(1, 15 * 8);
        List<EnergyCountOne> list = energyCountMapper.queryNightEnergyCountByProbeId(lightList, sTime, eTime, parent);
        PageInfo<EnergyCountOne> pageInfo = new PageInfo<EnergyCountOne>(list);
        return pageInfo;
    }


    @Override
    public List<Integer> queryEnergyCountByMonitorId(Integer type) {
        return energyCountMapper.queryEnergyCountByMonitorId(type);
    }


    @Override
    public List<EnergyCountOne> queryNightEnergyCountByProbeIdAvg(List lightList, String sTime, String eTime, Integer parent) {
        List<EnergyCountOne> list = energyCountMapper.queryNightEnergyCountByProbeIdAvg(lightList, sTime, eTime, parent);
        return list;
    }


    @Override
    public String queryEnergyCountByProbeIdUnit(List lightList, String date, List idParentList) {

        String unit = energyCountMapper.queryEnergyCountByProbeIdUnit(lightList, date, idParentList);
        return unit;
    }


    @Override
    public String queryEnergyCountByProbeIdFloor(List lightList, String date, List idParentList) {
        String floor = energyCountMapper.queryEnergyCountByProbeIdFloor(lightList, date, idParentList);
        return floor;
    }


    @Override
    public List<Integer> queryEnergyCountByProbeIdParent(Integer parent, Integer lou) {
        List<Integer> parentOne = energyCountMapper.queryEnergyCountByProbeIdParent(parent, lou);
        return parentOne;
    }

    ////返回所查楼层 一个月中每天的数据及下月第一天的数据    计算当天晚上11:00到第二天 早6:00
    @Override
    public List<List<Object>> queryEnergyCountById(Integer type, String date, List idParentList, Integer standard) {
        List<List<Object>> floorList = new ArrayList<>();
        List<Object> rowFirst = new ArrayList<>();
        boolean isFirst = true;
        //获取本月第一天日期
        String firstDay = DateUtil.getFirstDay(date);
        //获取本月最后一天日期
        String lastDay = DateUtil.getLastDayOfMonth(date);
        //获取下月第一天日期
        String firstDayNext = DateUtil.getFirstDayOfNextMonth(lastDay);
//		DecimalFormat df = new DecimalFormat("0.00");
        //查询出每个楼层的数据   每天
        for (int i = 0; i < idParentList.size(); i++) {
            List<Object> rowList = new ArrayList();
            Integer parent = (Integer) idParentList.get(i);
            String startTime = firstDay + " 22:00:00.0";
            firstDayNext = firstDayNext + " 09:00:00.0";
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = new GregorianCalendar();
            try {
                Date dt = formatter.parse(firstDay);
                calendar.setTime(dt);
                calendar.add(calendar.DATE, 1);//把日期往后增加一天.整数往后推,负数往前移动
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            List<EnergyCountOne> list = energyCountMapper.queryEnergyCountByIdOfHour(type, startTime, parent, firstDayNext);//按日期排序   2019-06 查询当月和下月第一天数据
            if (list.size() > 0) {
                rowList.add(list.get(0).getCaption());
            }
            Double sumMonth = 0.0;
            if (list != null && !list.isEmpty()) {//判断本月数据数据是否为空
                for (int count = 0; count < list.size() - 1; count++) {
                    if (list.get(count).getMoment().contains(" 22:00:00.0")) {
                        if (isFirst) {
                            rowFirst.add(list.get(count).getMoment().split(" ")[0]);
                        }
                        //定义数据 sun 存储值
                        Double sumday = 0.0;
                        while (!list.get(count).getMoment().contains(" 06:00:00.0") && count + 1 < list.size() - 1) {
                            sumday = list.get(count).getAvg() + list.get(count + 1).getAvg();//每天的和
                            count++;
                            if (count + 1 >= list.size() - 1) {
                                continue;
                            }
                        }
                        sumMonth = sumMonth + sumday;
                        rowList.add(ScaleUtil.getRate(sumday));
                        //更改时间 下一天
                        try {
                            Date dt = formatter.parse(formatter.format(calendar.getTime()));
                            calendar.setTime(dt);
                            calendar.add(calendar.DATE, 1);//把日期往后增加一天.整数往后推,负数往前移动
                        } catch (ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
                if (isFirst) {
                    rowFirst.add(0, "建筑楼层");
                    rowFirst.add(1, "参考指标");
                    floorList.add(rowFirst);
                }
                isFirst = false;
                //参考指标为平均值
                if (standard == 1 || standard == 2) {
                    if (rowList.size() <= 1) {
                        continue;
                    }
                    Double avg = sumMonth / (rowList.size() - 1) * 1.9;
                    rowList.add(1, ScaleUtil.getRate(avg));//将平均值插入行数组第二位置
                }
                floorList.add(rowList);
            }
        }
        return floorList;
    }


    @Override
    public List<List<Energy>> getEnergySpaceTu(CommonMonitorDataVo vo, String ids) {
        List<Integer> idList = new ArrayList<Integer>();
        List<List<Energy>> floorList = new ArrayList<>();
        int catalog = vo.getCatalog();
        int redioType = vo.getRedioType();
        String lastTime = vo.getLastTime();
        String startTime = vo.getStartTime();
        String tableName = vo.getTableName();
        if (redioType == 1 && vo.getSelectType() == 2) {
            lastTime = lastTime + "-01";
            startTime = startTime + "-01";
        }
        if (redioType == 1 && vo.getSelectType() == 1) {  //范围 月份 拼接
            lastTime = lastTime + "-01-01";
            startTime = startTime + "-01-01";
        }
        if (ids != null) {//ids表示楼层
            String[] split = ids.split(",");
            for (String id : split) {
                idList.add(Integer.parseInt(id));
            }
        }
        //总用电量 34电  37 水
        int parentCatalog;
        double sum = 0.0;
        String zhanbi;
        if (catalog != 34 && catalog != 37) {//去查询父类型
            parentCatalog = energyCountMapper.getParentCatalog(catalog);
            //计算总用电量 A3楼
            sum = energyCountMapper.getEnergySum(parentCatalog, redioType, startTime, lastTime, tableName);

        } else {
            parentCatalog = catalog;
            sum = energyCountMapper.getEnergySum(parentCatalog, redioType, startTime, lastTime, tableName);
        }


        DecimalFormat df = new DecimalFormat("0.00%");
        if (idList != null && !idList.isEmpty()) {
            for (Integer id : idList) {//遍历楼层
                List<Energy> list = energyCountMapper.getEnergySpaceBiao(redioType, catalog, startTime, lastTime, tableName, id);
                //遍历出序列号
                if (!list.isEmpty() && list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        //计算百分比  楼层/整楼
                        double result = Double.parseDouble(list.get(i).getValue().toString()) / sum;
                        zhanbi = df.format(result);
                        list.get(i).setZhanbi(zhanbi);
                    }
                }
                floorList.add(list);
            }
            return floorList;
        } else {
            return null;
        }
    }


    @Override
    public Map<String, Object> getEnergySpaceBiao(CommonMonitorDataVo vo, String ids, int size, int page) {
        List<Integer> idList = new ArrayList<Integer>();
        List<List<Energy>> floorList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        int catalog = vo.getCatalog();
        int redioType = vo.getRedioType();
        String lastTime = vo.getLastTime();
        String startTime = vo.getStartTime();
        String tableName = vo.getTableName();
        if (redioType == 1 && vo.getSelectType() == 2) {
            lastTime = lastTime + "-01";
            startTime = startTime + "-01";
        }
        if (redioType == 1 && vo.getSelectType() == 1) {  //范围 月份 拼接
            lastTime = lastTime + "-01-01";
            startTime = startTime + "-01-01";
        }
        if (ids != null) {//ids表示楼层
            String[] split = ids.split(",");
            for (String id : split) {
                idList.add(Integer.parseInt(id));
            }
        }
        //总用电量 34电  37 水
        int parentCatalog;
        double sum = 0.0;
        String zhanbi = "";
        double result = 0.0;
        if (catalog != 34 && catalog != 37) {//去查询父类型
            parentCatalog = energyCountMapper.getParentCatalog(catalog);
            //计算总用电量 A3楼
            sum = energyCountMapper.getEnergySum(parentCatalog, redioType, startTime, lastTime, tableName);

        } else {
            parentCatalog = catalog;
            sum = energyCountMapper.getEnergySum(parentCatalog, redioType, startTime, lastTime, tableName);
        }


//    	DecimalFormat df = new DecimalFormat("0.00");
        if (idList != null && !idList.isEmpty()) {
            for (Integer id : idList) {//遍历楼层
                int num = 1 + size * (page - 1);   //排序数
                PageHelper.startPage(page, size);
                List<Energy> energy = energyCountMapper.getEnergySpaceBiao(redioType, catalog, startTime, lastTime, tableName, id);
                PageInfo<Energy> pageInfo = new PageInfo<Energy>(energy);
                List<Energy> list = pageInfo.getList();
                //遍历出序列号
                if (list != null && !list.isEmpty()) {
                    for (int i = 0; i < list.size(); i++) {
                        //计算百分比  楼层/整楼
                        list.get(i).setValue(ScaleUtil.getRate(Double.parseDouble(list.get(i).getValue().toString())));
                        if (sum != 0.0) {
                            result = Double.parseDouble(list.get(i).getValue().toString()) * 100 / sum;
                        }
                        zhanbi = ScaleUtil.getRate(result);
                        list.get(i).setZhanbi(zhanbi + "%");
                        list.get(i).setXulie(num);
                        num++;
                    }
                }
                floorList.add(list);
                if (pageInfo.getTotal() > 0) {
                    map.put("total", pageInfo.getTotal());
                }
            }
            //map.put("value", floorList);
            //return map;
        }
        //转换格式
        List<List<Object>> columnList = new ArrayList<>();
        List<String> titelList = new ArrayList<>();
        if (floorList != null && !floorList.isEmpty()) {
            for (int fl = 0; fl < floorList.size(); fl++) {
                if (floorList.get(fl).size() > 0 && floorList.get(fl) != null && !floorList.get(fl).isEmpty()) {
                    for (int f = 0; f < floorList.get(fl).size(); f++) {//外层循环日期list
                        List<Object> rowList = new ArrayList<>();
                        for (int dl = 0; dl < floorList.size(); dl++) {//内层楼层list
                            if (floorList.get(dl).size() > 0) {
                                rowList.add(floorList.get(dl).get(f).getValue());
                                rowList.add(floorList.get(dl).get(f).getZhanbi());
                            }
                        }
                        rowList.add(0, floorList.get(fl).get(f).getXulie());
                        rowList.add(1, floorList.get(fl).get(f).getTime());
                        columnList.add(rowList);
                    }
                    for (int dl = 0; dl < floorList.size(); dl++) {//楼层list
                        if (floorList.get(dl).size() > 0) {
                            titelList.add(floorList.get(dl).get(fl).getFloor() + "" + floorList.get(dl).get(fl).getUnit());
                            titelList.add("占比(%)");
                        }
                    }
                    titelList.add(0, "排序");
                    titelList.add(1, "时间");
                    map.put("title", titelList);
                    map.put("value", columnList);
                    return map;
                }
            }

            return null;
        }
        return null;

    }


    @Override
    public Map<String, Object> getEnergySpaceExcle(CommonMonitorDataVo vo, String ids) {
        List<Integer> idList = new ArrayList<Integer>();
        List<List<Energy>> floorList = new ArrayList<>();
        int catalog = vo.getCatalog();
        int redioType = vo.getRedioType();
        String lastTime = vo.getLastTime();
        String startTime = vo.getStartTime();
        String tableName = vo.getTableName();
        if (redioType == 1 && vo.getSelectType() == 2) {
            lastTime = lastTime + "-01";
            startTime = startTime + "-01";
        }
        if (redioType == 1 && vo.getSelectType() == 1) {  //范围 月份 拼接
            lastTime = lastTime + "-01-01";
            startTime = startTime + "-01-01";
        }
        int num = 1;   //排序数
        if (ids != null) {//ids表示楼层
            String[] split = ids.split(",");
            for (String id : split) {
                idList.add(Integer.parseInt(id));
            }
        }
        //总用电量 34电  37 水
        int parentCatalog;
        double sum = 0.0;
        String zhanbi;
        double result = 0.0;
        if (catalog != 34 && catalog != 37) {//去查询父类型
            parentCatalog = energyCountMapper.getParentCatalog(catalog);
            //计算总用电量 A3楼
            sum = energyCountMapper.getEnergySum(parentCatalog, redioType, startTime, lastTime, tableName);
        } else {
            parentCatalog = catalog;
            sum = energyCountMapper.getEnergySum(parentCatalog, redioType, startTime, lastTime, tableName);
        }
//    	DecimalFormat df = new DecimalFormat("0.00");
        if (idList != null && !idList.isEmpty()) {
            for (Integer id : idList) {
                List<Energy> list = energyCountMapper.getEnergySpaceBiao(redioType, catalog, startTime, lastTime, tableName, id);
                //遍历出序列号
                if (list != null && !list.isEmpty()) {
                    for (int i = 0; i < list.size(); i++) {
                        //计算百分比  楼层/整楼
                        list.get(i).setValue(ScaleUtil.getRate(Double.parseDouble(list.get(i).getValue().toString())));
                        if (sum != 0.0) {
                            result = Double.parseDouble(list.get(i).getValue().toString()) * 100 / sum;
                        }
                        zhanbi = ScaleUtil.getRate(result);
                        list.get(i).setZhanbi(zhanbi + "%");
                        list.get(i).setXulie(num);
                        num++;
                    }
                    //list<<list<实体>>>
                }
                if (list != null && !list.isEmpty()) {
                    floorList.add(list);
                }
            }
        }
        //数据导出转换格式
        Map<String, Object> map = new HashMap<>();
        List<List<Object>> columnList = new ArrayList<>();
        List<String> titelList = new ArrayList<>();
        if (floorList != null && !floorList.isEmpty()) {
            if (floorList.get(0).size() > 0 && floorList.get(0) != null && !floorList.get(0).isEmpty()) {
                for (int f = 0; f < floorList.get(0).size(); f++) {//外层循环日期list
                    List<Object> rowList = new ArrayList<>();
                    for (int dl = 0; dl < floorList.size(); dl++) {//内层楼层list
                        rowList.add(floorList.get(dl).get(f).getValue());
                        rowList.add(floorList.get(dl).get(f).getZhanbi());
                    }
                    rowList.add(0, floorList.get(0).get(f).getXulie());
                    rowList.add(1, floorList.get(0).get(f).getTime());
                    columnList.add(rowList);
                }
                for (int dl = 0; dl < floorList.size(); dl++) {//楼层list
                    titelList.add(floorList.get(dl).get(0).getFloor() + "" + floorList.get(dl).get(0).getUnit());
                    titelList.add("占比(%)");
                }
                titelList.add(0, "排序");
                titelList.add(1, "时间");
                map.put("titel", titelList);
                map.put("value", columnList);
                return map;
            }
            return null;
        }
        return null;
    }


    @Override
    public List<Energy> getEnergyA3Floorid() {
        List<Energy> floorId = energyCountMapper.getEnergyA3Floorid();
        floorId = EnergyTreeNode.build(floorId);
        return floorId;
    }


    @Override
    public Map<String, Object> getEnergyComAndSeq(CommonMonitorDataVo vo, Integer floorId) {
        int catalog = vo.getCatalog();//能耗类型  水37 电 34
        int redioType = vo.getRedioType();//0 单个 1 范围
        int selectType = vo.getSelectType();//传2 月份
        String lastTime = vo.getLastTime();
        String startTime = vo.getStartTime();
        String tableName = vo.getTableName();
        if (redioType == 1 && vo.getSelectType() == 2) {
            lastTime = lastTime + "-01";
            startTime = startTime + "-01";
        }
        if (redioType == 1 && vo.getSelectType() == 1) {  //范围 月份 拼接
            lastTime = lastTime + "-01-01";
            startTime = startTime + "-01-01";
        }
        Map<String, Object> map = new HashMap<>();
        //当期综合能耗
        List<ComSeqEnergy> dqList = energyCountMapper.getEnergyComAndSeq(redioType, catalog, startTime, lastTime, tableName, floorId);
        //同期综合能耗   去年本月
        //获去上年开始和结束日期
        if (startTime != "" && startTime != null) {
            startTime = DateUtil.getLastYear(startTime);
        }
        if (lastTime != "" && lastTime != null) {
            lastTime = DateUtil.getLastYear(lastTime);
        }
        List<ComSeqEnergy> tqList = energyCountMapper.getEnergyComAndSeq(redioType, catalog, startTime, lastTime, tableName, floorId);

        //获去上月开始和结束日期
        if (startTime != "" && startTime != null) {
            startTime = DateUtil.getLastMonth(vo.getStartTime());
        }
        if (lastTime != "" && lastTime != null) {
            lastTime = DateUtil.getLastMonth(vo.getLastTime());
        }
        List<ComSeqEnergy> sqList = energyCountMapper.getEnergyComAndSeq(redioType, catalog, startTime, lastTime, tableName, floorId);
        //单位
        String unit = energyCountMapper.getUnit(catalog);
        //计算比例  同时加入到当期实体中
        List<ComSeqEnergy> list = new ArrayList<>();
//         DecimalFormat df = new DecimalFormat("0.00");
        String lastYear;
        String lastMouth;
        double zb;
        if (dqList != null) {
            for (int dq = 0; dq < dqList.size(); dq++) {
                ComSeqEnergy num = new ComSeqEnergy();
                num.setDate(dqList.get(dq).getDate());
                num.setDqzh(ScaleUtil.roundObject(dqList.get(dq).getDqzh()));
                if (!tqList.isEmpty() && tqList != null) {//同期
                    for (ComSeqEnergy tq : tqList) {
                        lastYear = DateUtil.getLastYear(dqList.get(dq).getDate());
                        if (tq.getDate().equals(lastYear)) {
                            //比例
                            if (!tq.getDqzh().toString().equals("0.0") && !dqList.get(dq).getDqzh().toString().equals("0.0")) {//分母为0或者转化double报错
                                zb = ScaleUtil.roundObject((Double.parseDouble(dqList.get(dq).getDqzh().toString()) - Double.parseDouble(tq.getDqzh().toString())) / Double.parseDouble(tq.getDqzh().toString()) * 100);
                                num.setTqzh(ScaleUtil.roundObject(tq.getDqzh()));
                                num.setTbzz(zb);
                                break;
                            } else {
                                break;
                            }

                        } else {
                            num.setTqzh(0.00);
                            num.setTbzz(0.00);
                        }
                    }
                } else {
                    num.setTqzh(0.00);
                    num.setTbzz(0.00);
                }
                if (sqList != null && !sqList.isEmpty()) {//上期
                    for (ComSeqEnergy sq : sqList) {
                        lastMouth = DateUtil.getLastMonth(dqList.get(dq).getDate());
                        if (lastMouth.equals(sq.getDate())) {
                            //比例
                            System.out.println("+++++++++" + sq.getDqzh().toString() + "===" + dqList.get(dq).getDqzh().toString());
                            if (!sq.getDqzh().toString().equals("0.0") && !dqList.get(dq).getDqzh().toString().equals("0.0")) {
                                zb = ScaleUtil.roundObject((Double.parseDouble(dqList.get(dq).getDqzh().toString()) - Double.parseDouble(sq.getDqzh().toString())) / Double.parseDouble(sq.getDqzh().toString()) * 100);
                                // zb = Double.parseDouble(df.format((dqList.get(dq).getDqzh()-sq.getDqzh())/sq.getDqzh()*100));
                                num.setSqzh(ScaleUtil.roundObject(sq.getDqzh()));
                                num.setHbzz(zb);
                                break;
                            } else {
                                break;
                            }
                        } else {
                            num.setSqzh(0.00);
                            num.setHbzz(0.00);
                        }
                    }
                } else {
                    num.setSqzh(0.00);
                    num.setHbzz(0.00);
                }
                list.add(num);
            }
        }
        if (list.size() == 0) {
            list.add(new ComSeqEnergy(0.0, 0.0, 0.0));
        }
        map.put("unit", unit);
        map.put("value", list);
        return map;
    }


    @Override
    public List<List<Energy>> getEnergyType(CommonMonitorDataVo vo, String ids, Integer floorId) {
        // 分别查询每个类别的数据
        //int catalog = vo.getCatalog();//能耗类型  水37 电 34
        int redioType = vo.getRedioType();//0 单个 1 范围
        int selectType = vo.getSelectType();//传2 月份
        String lastTime = vo.getLastTime();
        String startTime = vo.getStartTime();
        String tableName = vo.getTableName();
        if (redioType == 1 && selectType == 2) {
            lastTime = lastTime + "-01";
            startTime = startTime + "-01";
        }
        if (redioType == 1 && selectType == 1) {  //范围 月份 拼接
            lastTime = lastTime + "-01-01";
            startTime = startTime + "-01-01";
        }
        List<Integer> catalogList = new ArrayList<>();
        List<List<Energy>> energyList = new ArrayList<>();
        if (ids != null) {
            String[] split = ids.split(",");
            for (String id : split) {
                catalogList.add(Integer.parseInt(id));
            }
        }
        if (catalogList != null && !catalogList.isEmpty()) {
            for (Integer catalog : catalogList) {
                List<Energy> energy = energyCountMapper.getEnergyType(redioType, catalog, startTime, lastTime, tableName, floorId);
                energyList.add(energy);
            }
        }

        return energyList;
    }


    @Override
    public Map<String, Object> getEnergyTypeSort(CommonMonitorDataVo vo, String ids, Integer floorId,
                                                 int page, int size) {
        int itemize_type = vo.getCatalog();//能耗类型  水37 电 34
        int redioType = vo.getRedioType();//0 单个 1 范围
        int selectType = vo.getSelectType();
        String lastTime = vo.getLastTime();
        String startTime = vo.getStartTime();
        String tableName = vo.getTableName();
        if (redioType == 1 && selectType == 2) {
            lastTime = lastTime + "-01";
            startTime = startTime + "-01";
        }
        if (redioType == 1 && selectType == 1) {  //范围 月份 拼接
            lastTime = lastTime + "-01-01";
            startTime = startTime + "-01-01";
        }
        //获取能耗类型的\总和
        PageHelper.startPage(page, size);
        List<Double> sum = energyCountMapper.getEnergyTypeSum(redioType, itemize_type, startTime, lastTime, tableName, floorId);
        PageInfo<Double> pageInfo = new PageInfo<Double>(sum);
        List<Double> sumList = pageInfo.getList();
        //获取到列表信息
        List<Integer> catalogList = new ArrayList<>();
        List<List<Energy>> energyList = new ArrayList<>();
        if (ids != null) {//ids表示楼层
            String[] split = ids.split(",");
            for (String id : split) {
                catalogList.add(Integer.parseInt(id));
            }
        }
//		DecimalFormat df = new DecimalFormat("0.00");
        String zhanbi = "";
        int num = 1 + size * (page - 1);   //排序数
        if (catalogList != null && !catalogList.isEmpty()) {
            for (Integer catalog : catalogList) {
                PageHelper.startPage(page, size);
                List<Energy> energyType = energyCountMapper.getEnergyType(redioType, catalog, startTime, lastTime, tableName, floorId);
                PageInfo<Energy> pageEnergy = new PageInfo<Energy>(energyType);
                List<Energy> energy = pageEnergy.getList();
                if (energy != null && !energy.isEmpty() && sumList != null && !sumList.isEmpty()) {
                    for (int i = 0; i < energy.size(); i++) {
                        energy.get(i).setValue(ScaleUtil.getRate(energy.get(i).getValue().toString()));
                        if (sumList.get(i).doubleValue() != 0.0) {
                            zhanbi = ScaleUtil.getRate(Double.parseDouble(energy.get(i).getValue().toString()) / sumList.get(i).doubleValue() * 100) + "%";
                        } else {
                            zhanbi = "0.00%";
                        }
                        energy.get(i).setZhanbi(zhanbi);
                        energy.get(i).setXulie(num);
                        num++;
                    }
                }
                energyList.add(energy);
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("total", pageInfo.getTotal());
        //map.put("value", energyList);
        List<List<Object>> columnList = new ArrayList<>();
        List<String> titelList = new ArrayList<>();
        if (energyList != null && !energyList.isEmpty()) {
            int lsize = 0;
            for (List<Energy> ls : energyList) {
                if (ls != null && ls.size() > 0) {
                    titelList.add(ls.get(0).getName() + ls.get(0).getUnit());
                    titelList.add("占比(%)");
                    lsize = ls.size();
                }
            }
            for (int i = 0; i < lsize; i++) {
                List<Object> rowList = new ArrayList<>();
                for (List<Energy> ls : energyList) {
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
                if (energyList.get(0) != null && !energyList.get(0).isEmpty()) {
                    rowList.add(0, energyList.get(0).get(i).getXulie());
                    rowList.add(1, energyList.get(0).get(i).getTime());
                } else {
                    rowList.add(0, energyList.get(1).get(i).getXulie());
                    rowList.add(1, energyList.get(1).get(i).getTime());
                }

                columnList.add(rowList);
            }

            titelList.add(0, "排名");
            titelList.add(1, "时间");
        }
        map.put("title", titelList);
        map.put("value", columnList);
        return map;
    }


    @Override
    public List<List<Energy>> getEnergyTypeSortExcel(CommonMonitorDataVo vo, String ids, Integer floorId) {
        int itemize_type = vo.getCatalog();//能耗类型  水37 电 34
        int redioType = vo.getRedioType();//0 单个 1 范围
        int selectType = vo.getSelectType();//传2 月份
        String lastTime = vo.getLastTime();
        String startTime = vo.getStartTime();
        String tableName = vo.getTableName();
        if (redioType == 1 && vo.getSelectType() == 2) {
            lastTime = lastTime + "-01";
            startTime = startTime + "-01";
        }
        if (redioType == 1 && vo.getSelectType() == 1) {  //范围 月份 拼接
            lastTime = lastTime + "-01-01";
            startTime = startTime + "-01-01";
        }
        //获取能耗类型的\总和
        List<Double> sumList = energyCountMapper.getEnergyTypeSum(redioType, itemize_type, startTime, lastTime, tableName, floorId);
        //获取到列表信息
        List<Integer> catalogList = new ArrayList<>();
        List<List<Energy>> energyList = new ArrayList<>();
        if (ids != null) {//ids表示楼层
            String[] split = ids.split(",");
            for (String id : split) {
                catalogList.add(Integer.parseInt(id));
            }
        }
//		DecimalFormat df = new DecimalFormat("0.00");
        String zhanbi = "";
        if (catalogList != null && !catalogList.isEmpty()) {
            for (Integer catalog : catalogList) {
                List<Energy> energy = energyCountMapper.getEnergyType(redioType, catalog, startTime, lastTime, tableName, floorId);
                if (energy != null && !energy.isEmpty() && sumList != null && !sumList.isEmpty()) {
                    for (int i = 0; i < energy.size(); i++) {
                        energy.get(i).setValue(ScaleUtil.getRate(energy.get(i).getValue().toString()));
                        if (sumList.get(i).doubleValue() != 0.0) {
                            zhanbi = ScaleUtil.getRate(Double.parseDouble(energy.get(i).getValue().toString()) / sumList.get(i).doubleValue() * 100) + "%";
                        } else {
                            zhanbi = "0%";
                        }
                        energy.get(i).setZhanbi(zhanbi);
                        energy.get(i).setXulie(i + 1);
                    }
                }
                energyList.add(energy);
            }
        }
        return energyList;
    }


    @Override
    public List<EnergyCount> getEnergyFenShiTu(CommonMonitorDataVo vo, Integer floor) {
        int catalog = vo.getCatalog();//能耗类型  水37 电 34
        int redioType = vo.getRedioType();//0 单个 1 范围
        int selectType = vo.getSelectType();//传2 月份
        String lastTime = vo.getLastTime();
        String startTime = vo.getStartTime();
        String tableName = vo.getTableName();
        if (redioType == 1 && selectType == 2) {
            lastTime = lastTime + "-01";
            startTime = startTime + "-01";
        }
        if (redioType == 1 && selectType == 1) {  //范围 月份 拼接
            lastTime = lastTime + "-01-01";
            startTime = startTime + "-01-01";
        }
        List<EnergyCount> energyCount = energyCountMapper.getEnergyFenShiTu(redioType, catalog, startTime, lastTime, tableName, floor);
        if (redioType == 0 && selectType == 2) {
            List<EnergyCount> monthCount = getDayListOfMonth(startTime);
            if (null != monthCount && monthCount.size() > 0) {
                for (int i = 0; i < monthCount.size(); i++) {
                    EnergyCount monthEnergy = monthCount.get(i);
                    for (int j = 0; j < energyCount.size(); j++) {
                        EnergyCount energyCon = energyCount.get(j);
                        monthEnergy.setUnit(energyCon.getUnit());
                        if (StringUtils.equals(String.valueOf(monthEnergy.getTime()), String.valueOf(energyCon.getTime()))) {
                            monthEnergy.setValue(energyCon.getValue());
                        }
                    }
                }
            }
            return monthCount;
        } else {
            return energyCount;
        }
    }

    /**
     * java获取 当月所有的日期集合
     *
     * @return
     */
    public static List<EnergyCount> getDayListOfMonth(String startTime) {
        List<EnergyCount> list = new ArrayList();
        Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
        String[] split = startTime.split("-");
        int year = Integer.parseInt(split[0]);//年份
        int month = Integer.parseInt(split[1]);//月份
//		int day = aCalendar.getActualMaximum(Calendar.DATE);
        aCalendar.set(Calendar.YEAR, year);
        aCalendar.set(Calendar.MONTH, month - 1);
        aCalendar.set(Calendar.DATE, 1);
        aCalendar.roll(Calendar.DATE, -1);
        int day = aCalendar.get(Calendar.DATE);
        for (int i = 1; i <= day; i++) {
            String days = "0";
//			if(i<10){
//				days="0"+i;
//			}else {
            days = String.valueOf(i);
//			}
            String aDate = String.valueOf(year) + "-" + month + "-" + days;
            SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd");
            try {
                EnergyCount energyLose = new EnergyCount();
//				energyLose.setUnit();
                energyLose.setValue(0.0);
                Timestamp nousedate = new Timestamp(sp.parse(aDate).getTime());
                energyLose.setTime(nousedate);
//				Date date = sp.parse(aDate);
                list.add(energyLose);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /*public static List<EnergyCount> getDayListOfMonth(List<EnergyCount> energyCount) {
//		List<EnergyCount> list = new ArrayList<EnergyCount>();
        Calendar aCalendar = Calendar.getInstance(Locale.CHINA);
        int year = aCalendar.get(Calendar.YEAR);//年份
        int month = aCalendar.get(Calendar.MONTH) + 1;//月份
        int day = aCalendar.getActualMaximum(Calendar.DATE);
        String monthStr="0";
        if(month<10){
            monthStr="0"+month;
        }else{
            monthStr=String.valueOf(month);
        }
        for (int i = 1; i <= day; i++) {
            String days="0";
            if(i<10){
                days="0"+i;
            }else {
                days=String.valueOf(i);
            }
            String aDate = String.valueOf(year)+"-"+monthStr+"-"+days;
            SimpleDateFormat sp=new SimpleDateFormat("yyyy-MM-dd");
            try {
                String date = sp.parse(aDate).toString();
                if (null != energyCount && energyCount.size() > 0){
                    for (int j = 0; j < energyCount.size(); j++){
                        EnergyCount energy = energyCount.get(j);
                        if (!StringUtils.equals(String.valueOf(energy.getTime()),date)){
                            EnergyCount energyLose = new EnergyCount();
                            energyLose.setUnit(energy.getUnit());
                            energyLose.setValue(0.0);
                            Timestamp nousedate = new Timestamp(sp.parse(aDate).getTime());
                            energyLose.setTime(nousedate);
                        }
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        return energyCount;
    }*/
    @Override
    public Map<String, Object> getEnergyFenShiBiao(CommonMonitorDataVo vo, Integer floor, int page, int size, int seq, String rank) {
        Map<String, Object> map = new HashMap<>();
        int catalog = vo.getCatalog();//能耗类型  水37 电 34
        int redioType = vo.getRedioType();//0 单个 1 范围
        int selectType = vo.getSelectType();//传2 月份
        String lastTime = vo.getLastTime();
        String startTime = vo.getStartTime();
        String tableName = vo.getTableName();
        if (redioType == 1 && selectType == 2) {
            lastTime = lastTime + "-01";
            startTime = startTime + "-01";
        }
        if (redioType == 1 && selectType == 1) {  //范围 月份 拼接
            lastTime = lastTime + "-01-01";
            startTime = startTime + "-01-01";
        }
//		DecimalFormat df = new DecimalFormat("0.00");
        int num = 1 + size * (page - 1);   //排序数
        PageHelper.startPage(page, size);
        List<EnergyBiao> energyBiao = energyCountMapper.getEnergyFenShiBiao(redioType, catalog, startTime, lastTime, tableName, floor, seq, rank);
        PageInfo<EnergyBiao> pageEnergy = new PageInfo<EnergyBiao>(energyBiao);
        if (energyBiao != null && !energyBiao.isEmpty()) {
            for (EnergyBiao eb : energyBiao) {
                eb.setYongdianlaing(ScaleUtil.getRate(eb.getYongdianlaing()));
                eb.setXuhao(num);
                num++;
            }
        }
        map.put("total", pageEnergy.getTotal());
        map.put("list", energyBiao);
        return map;
    }


    @Override
    public List<EnergyBiao> getEnergyFenShiBiaoExcel(CommonMonitorDataVo vo, Integer floor, int seq, String rank) {
        int catalog = vo.getCatalog();//能耗类型  水37 电 34
        int redioType = vo.getRedioType();//0 单个 1 范围
        int selectType = vo.getSelectType();//传2 月份
        String lastTime = vo.getLastTime();
        String startTime = vo.getStartTime();
        String tableName = vo.getTableName();
        if (redioType == 1 && selectType == 2) {
            lastTime = lastTime + "-01";
            startTime = startTime + "-01";
        }
        if (redioType == 1 && selectType == 1) {  //范围 月份 拼接
            lastTime = lastTime + "-01-01";
            startTime = startTime + "-01-01";
        }
//		DecimalFormat df = new DecimalFormat("0.00");
        int num = 1;   //排序数
        List<EnergyBiao> energyBiao = energyCountMapper.getEnergyFenShiBiao(redioType, catalog, startTime, lastTime, tableName, floor, seq, rank);
        if (energyBiao != null && !energyBiao.isEmpty()) {
            for (EnergyBiao eb : energyBiao) {
                eb.setYongdianlaing(ScaleUtil.getRate(eb.getYongdianlaing()));
                eb.setXuhao(num);
                num++;
            }
        }
        return energyBiao;
    }


    @Override
    public List<Energy> getEnergyFenXiangTu(CommonMonitorDataVo vo, Integer floor) {
        int catalog = vo.getCatalog();//能耗类型  水37 电 34
        int redioType = vo.getRedioType();//0 单个 1 范围
        int selectType = vo.getSelectType();//传2 月份
        String lastTime = vo.getLastTime();
        String startTime = vo.getStartTime();
        String tableName = vo.getTableName();
        if (redioType == 1 && selectType == 2) {
            lastTime = lastTime + "-01";
            startTime = startTime + "-01";
        }
        if (redioType == 1 && selectType == 1) {  //范围 月份 拼接
            lastTime = lastTime + "-01-01";
            startTime = startTime + "-01-01";
        }
        List<Energy> energy = energyCountMapper.getEnergyFenXiangTu(redioType, catalog, startTime, lastTime, tableName, floor);
        //计算占比
        if (energy != null) {
            Energy delec = new Energy();
            Energy zelec = new Energy();
            Energy telec = new Energy();
            for (Energy en : energy) {
                if (en.getName() == "动力用电") {
                } else {
                    delec.setName("动力用电");
                    delec.setValue(0.0f);
                }
                if (en.getName() == "特殊用电") {
                } else {
                    telec.setName("特殊用电");
                    telec.setValue(0.0f);
                }
            }
            if (delec.getName() != null) energy.add(delec);
            if (zelec.getName() != null) energy.add(zelec);
            if (telec.getName() != null) energy.add(telec);
        }
        double sum = 0.0;
        String zhanbi = "";
//		DecimalFormat df = new DecimalFormat("0.00");
        if (energy != null && !energy.isEmpty()) {
            for (Energy en : energy) {
                sum = sum + Double.parseDouble(en.getValue().toString());
            }
            for (Energy en : energy) {
                if (sum != 0.0) {
                    zhanbi = ScaleUtil.getRate(Double.parseDouble(en.getValue().toString()) / sum * 100) + "%";
                } else {
                    zhanbi = "0.00%";
                }
                en.setZhanbi(zhanbi);
            }
        }
        return energy;
    }


    @Override
    public Map<String, Object> getEnergyFenXiangBiao(CommonMonitorDataVo vo, Integer floor, int page, int size) {
        int catalog = vo.getCatalog();//能耗类型  水37 电 34
        int redioType = vo.getRedioType();//0 单个 1 范围
        int selectType = vo.getSelectType();//传2 月份
        String lastTime = vo.getLastTime();
        String startTime = vo.getStartTime();
        String tableName = vo.getTableName();
        if (redioType == 1 && selectType == 2) {
            lastTime = lastTime + "-01";
            startTime = startTime + "-01";
        }
        if (redioType == 1 && selectType == 1) {  //范围 月份 拼接
            lastTime = lastTime + "-01-01";
            startTime = startTime + "-01-01";
        }
        Map<String, Object> map = new HashMap<>();
        int num = 1 + size * (page - 1);   //排序数
        PageHelper.startPage(page, size);
        List<EnergySub> energy = energyCountMapper.getEnergyFenXiangBiao(redioType, catalog, startTime, lastTime, tableName, floor);
        PageInfo<EnergySub> pageEnergy = new PageInfo<EnergySub>(energy);
        //计算占比
        double sum = 0.00;
        String zhanbi = "";
        int xuhao = 1;
//		DecimalFormat df = new DecimalFormat("0.00");
        if (energy != null && !energy.isEmpty()) {
            EnergySub delec = new EnergySub();
            EnergySub zelec = new EnergySub();
            EnergySub telec = new EnergySub();
            for (EnergySub en : energy) {
                if (en.getName() == "动力用电") {
                } else {
                    delec.setName("动力用电");
                    delec.setValue(0.0);
                }
                if (en.getName() == "特殊用电") {
                } else {
                    telec.setName("特殊用电");
                    telec.setValue(0.0);
                }
            }
            if (delec.getName() != null) energy.add(delec);
            if (zelec.getName() != null) energy.add(zelec);
            if (telec.getName() != null) energy.add(telec);
            for (EnergySub en : energy) {
                en.setValue(ScaleUtil.getRateStr(en.getValue()));
                sum = sum + Double.parseDouble(en.getValue().toString());
            }
            for (EnergySub en : energy) {
                if (sum != 0.0) {
                    zhanbi = ScaleUtil.getRate(Double.parseDouble(en.getValue().toString()) / sum * 100) + "%";
                } else {
                    zhanbi = "0.00%";
                }
                en.setZhanbi(zhanbi);
                en.setXulie(xuhao);
                xuhao++;
            }
        }
        EnergySub en = new EnergySub();
        en.setXulie("总计");
        en.setName("类型总量");
        en.setValue(ScaleUtil.getRate(sum));
        en.setZhanbi("100%");
        energy.add(en);
        map.put("total", pageEnergy.getTotal());
        map.put("value", energy);
        return map;
    }


    @Override
    public List<EnergySub> getEnergyFenXiangBiaoExcel(CommonMonitorDataVo vo, Integer floor) {
        int catalog = vo.getCatalog();//能耗类型  水37 电 34
        int redioType = vo.getRedioType();//0 单个 1 范围
        int selectType = vo.getSelectType();//传2 月份
        String lastTime = vo.getLastTime();
        String startTime = vo.getStartTime();
        String tableName = vo.getTableName();
        if (redioType == 1 && selectType == 2) {
            lastTime = lastTime + "-01";
            startTime = startTime + "-01";
        }
        if (redioType == 1 && selectType == 1) {  //范围 月份 拼接
            lastTime = lastTime + "-01-01";
            startTime = startTime + "-01-01";
        }
        List<EnergySub> energy = energyCountMapper.getEnergyFenXiangBiao(redioType, catalog, startTime, lastTime, tableName, floor);
        //计算占比
        double sum = 0.0;
        String zhanbi = "";
        int xuhao = 1;
//		DecimalFormat df = new DecimalFormat("0.00");
        if (energy != null && !energy.isEmpty()) {
            EnergySub delec = new EnergySub();
            EnergySub zelec = new EnergySub();
            EnergySub telec = new EnergySub();
            for (EnergySub en : energy) {
                if (en.getName() == "动力用电") {
                } else {
                    delec.setName("动力用电");
                    delec.setValue(0.0);
                }
                if (en.getName() == "特殊用电") {
                } else {
                    telec.setName("特殊用电");
                    telec.setValue(0.0);
                }
            }
            if (delec.getName() != null) energy.add(delec);
            if (zelec.getName() != null) energy.add(zelec);
            if (telec.getName() != null) energy.add(telec);
            for (EnergySub en : energy) {
                en.setValue(ScaleUtil.getRateStr(en.getValue()));
                sum = sum + Double.parseDouble(en.getValue().toString());
            }
            for (EnergySub en : energy) {
                if (sum != 0.0) {
                    zhanbi = ScaleUtil.getRate(Double.parseDouble(en.getValue().toString()) / sum * 100) + "%";
                } else {
                    zhanbi = "0%";
                }
                en.setZhanbi(zhanbi);
                en.setXulie(xuhao);
                xuhao++;
            }
        }
        EnergySub en = new EnergySub();
        en.setXulie("总计");
        en.setName("类型总量");
        en.setValue(ScaleUtil.getRate(sum));
        en.setZhanbi("100%");
        energy.add(en);
        return energy;
    }


    @Override
    public Map<String, Object> getBaseRepresentationNumber(int page, int size, int catalog, String endTime, String startTime) {
        // TODO Auto-generated method stub
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格
        if ("".equals(endTime) || endTime == null) {
            String time = df.format(new Date());
            endTime = time;
        }
//		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd 00:00:00");//设置日期格式
        if ("".equals(startTime) || startTime == null) {
            Calendar calendar = Calendar.getInstance();
            /* HOUR_OF_DAY 指示一天中的小时 */
            calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 1);
            String time1 = df.format(calendar.getTime());
            startTime = time1;
        }
        List<BaseRepresentationNumber> baseList = null;
        PageInfo<BaseRepresentationNumber> pageInfo = null;
        if (catalog == 34) { //电
            catalog = 1002;
            PageHelper.startPage(page, size);
            baseList = energyCountMapper.getBaseRepresentationNumberElec(catalog, endTime, startTime);
            pageInfo = new PageInfo<BaseRepresentationNumber>(baseList);
        }
        if (catalog == 37) { //水
            catalog = 4000;
            PageHelper.startPage(page, size);
            baseList = energyCountMapper.getBaseRepresentationNumberWater(catalog, endTime, startTime);
            pageInfo = new PageInfo<BaseRepresentationNumber>(baseList);
        }
        Map<String, Object> map = new HashMap<>();
        long total = 0;
        if (pageInfo != null) {
            total = pageInfo.getTotal();
            int xuhao = 1 + (page - 1) * size;
            for (BaseRepresentationNumber ls : baseList) {
                ls.setXulie(xuhao);
                xuhao++;
            }
        }
        map.put("total", total);
        map.put("value", baseList);
        return map;
    }


    @Override
    public List<BaseRepresentationNumber> getBaseRepresentationNumberExcel(int catalog, String endTime,
                                                                           String startTime) {
        Calendar calendar = Calendar.getInstance();
        /* HOUR_OF_DAY 指示一天中的小时 */
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 1);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String time = df.format(new Date());
        if ("".equals(endTime) || endTime == null) {
            endTime = time;
        }
//		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd 00:00:00");//设置日期格式
        String time1 = df.format(calendar.getTime());
        if ("".equals(startTime) || startTime == null) {
            startTime = time1;
        }
        List<BaseRepresentationNumber> baseList = null;
        if (catalog == 34) { //电
            catalog = 1002;
            baseList = energyCountMapper.getBaseRepresentationNumberElec(catalog, endTime, startTime);
        }
        if (catalog == 37) { //水
            catalog = 4000;
            baseList = energyCountMapper.getBaseRepresentationNumberWater(catalog, endTime, startTime);
        }
        if (baseList != null) {
            int xuhao = 1;
            for (BaseRepresentationNumber ls : baseList) {
                ls.setXulie(xuhao);
                xuhao++;
            }
        }
        return baseList;
    }

    @Override
    public Map<String, Object> getLastNumber(int page, int size, int catalog) {
        //获取上月月份最后一天
        String lastMonthOfDay = DateUtil.getLastMonthOfDay();
        String startTime = lastMonthOfDay;
        String[] split = lastMonthOfDay.split("-");
        String year = split[0];
        String month = split[1];
        // 获取前月的第一天
        String endTime = DateUtil.getFirstDay();
        switch (catalog) {
            case 34:
                catalog = 1002;
                break;
            case 37:
                catalog = 4000;
                break;
        }
        PageHelper.startPage(page, size);
        List<BaseNumber> baseList = energyCountMapper.getLastNumber(catalog, year, month);
        PageInfo<BaseNumber> pageInfo = new PageInfo<>(baseList);
        Map<String, Object> map = new HashMap<>();
        map.put("total", pageInfo.getTotal());
        map.put("pageNum", pageInfo.getPageNum());
        map.put("pgeSize", pageInfo.getPageSize());
        if (baseList != null && !baseList.isEmpty()) {
            for (BaseNumber ls : baseList) {
                Map value = energyCountMapper.getValue(Integer.parseInt(ls.getValue().toString()), startTime, endTime);
                if (value != null)
                    ls.setValue(value.get("value") == null ? "" : value.get("value"));
                else
                    ls.setValue("");
            }
        }
        map.put("list", baseList);
        return map;
    }

    @Override
    public List<BaseNumber> getLastNumberExcel(int catalog) {
        //获取上月月份最后一天
        String lastMonthOfDay = DateUtil.getLastMonthOfDay();
        String startTime = lastMonthOfDay;
        String[] split = lastMonthOfDay.split("-");
        String year = split[0];
        String month = split[1];
        // 获取前月的第一天
        String endTime = DateUtil.getFirstDay();
        switch (catalog) {
            case 34:
                catalog = 1002;
                break;
            case 37:
                catalog = 4000;
                break;
        }
        List<BaseNumber> baseList = energyCountMapper.getLastNumber(catalog, year, month);
        if (baseList != null && !baseList.isEmpty()) {
            for (BaseNumber ls : baseList) {
                Map value = energyCountMapper.getValue(Integer.parseInt(ls.getValue().toString()), startTime, endTime);
                if (value != null)
                    ls.setValue(value.get("value") == null ? "" : value.get("value"));
                else
                    ls.setValue("");
            }
        }
        return baseList;
    }

}
