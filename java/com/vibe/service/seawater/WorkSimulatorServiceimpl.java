package com.vibe.service.seawater;

import java.util.Date;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vibe.mapper.seawater.WorkSimulatorMapper;
import com.vibe.monitor.asset.Asset;
import com.vibe.monitor.asset.AssetStore;
import com.vibe.monitor.drivers.fictitious.service.SimulateService;
import com.vibe.pojo.seawater.WorkPattern;
import com.vibe.pojo.seawater.WorkPeriod;
import com.vibe.pojo.user.User;
import com.vibe.utils.EasyUIJson;
import com.vibe.utils.FormJson;
import com.vibe.utils.FormJsonBulider;

@Service
public class WorkSimulatorServiceimpl implements WorkSimulatorService {
    @Autowired
    private AssetStore assetStore;
    @Autowired
    private WorkSimulatorMapper workSimulatorMapper;

    @Override
    public FormJson operationSimulatorWork(WorkPattern workpattern) {
        if (workpattern.getName() == null) {
            return FormJsonBulider.fail("名称不能为空");
        }
        if (workpattern.getId() == null) {
            List<WorkPattern> works = workSimulatorMapper.findNameSimulatorWork(workpattern.getUser().getId(), workpattern.getName(), workpattern.getDelFalg());
            if (works.size() > 0)
                return FormJsonBulider.fail("名称重复,请修改");
            if (workSimulatorMapper.insertSimulatorWork(workpattern) == 0) {// 添加工况
                return FormJsonBulider.fail("添加失败");
            }
        } else {
            if (workSimulatorMapper.updateSimulatorWork(workpattern) == 0) {// 修改工况
                return FormJsonBulider.fail("修改失败");
            }
        }
        return FormJsonBulider.success();
    }

    @Override
    public EasyUIJson findAllSimulatorWork(int page, int rows, WorkPattern workpattern) {
        List<WorkPattern> list = workSimulatorMapper.findAllSimulatorWork(workpattern);
        if (list.size() > 0) {
            for (WorkPattern pattern : list) {
                pattern.setSortCreationTime(pattern.getCreationTime().getTime());
                List<WorkPeriod> workPeriods = pattern.getWorkPeriods();
                if (workPeriods.size() > 0) {
                    int Periodsize = workPeriods.size();
                    pattern.setWorkNumber(Periodsize);
                    long sumTime = 0;
                    for (WorkPeriod period : workPeriods) {
                        Date startTime = period.getStartTime();
                        Date endTime = period.getEndTime();
                        if (null != endTime && null != startTime)
                            sumTime += Math.abs(endTime.getTime() - startTime.getTime());
                    }
                    pattern.setSumTime(sumTime);
                    pattern.setSortSumtime(sumTime);
                    Date endTime = workPeriods.get(Periodsize - 1).getEndTime();
                    if (Periodsize != 0L && null != endTime) {
                        pattern.setLastWorkTime(endTime);
                        pattern.setSortLastWorkTime(endTime.getTime());
                    }
                }
            }
        }
        int sort = workpattern.getSort();
        int sortType = workpattern.getSortType();
        list.sort((a1, a2) -> {
            return a1.compareTo(a2, sort, sortType);
        });
        //Collections.sort(list);
        // 创建EasyUIJson对象，封装查询结果
        EasyUIJson uiJson = new EasyUIJson();
        // 设置查询总记录数

        uiJson.setTotal((long) list.size());
        // 设置查询记录
        uiJson.setRows(
                list.subList((page - 1) * rows, page * rows > list.size() ? list.size() : page * rows));
        return uiJson;
    }


    @Override
    public FormJson delSimulatorWork(int[] ids) {
        if (ids == null) {
            return FormJsonBulider.fail("参数不能为空");
        } else {
            if (workSimulatorMapper.delSimulatorWork(ids) != ids.length) {
                return FormJsonBulider.fail("删除失败");
            }
            workSimulatorMapper.delWorkTemplateId(ids);
        }
        return FormJsonBulider.success();
    }

    @Override
    public List<WorkPattern> findSimulatorWork(Integer[] ids) {
        return workSimulatorMapper.findSimulatorWork(ids);
    }


    @Override
    public EasyUIJson findAllWorkPeriod(int page, int rows, WorkPeriod workPeriod) {
        List<WorkPeriod> list = workSimulatorMapper.findAllWorkPeriod(workPeriod);
        list.forEach((period) -> {
            Date endTime = period.getEndTime();
            Date startTime = period.getStartTime();
            if (null != endTime && null != startTime)
                period.setRunTime(endTime.getTime() - startTime.getTime());
        });
        EasyUIJson uiJson = new EasyUIJson();
        uiJson.setTotal((long) list.size());
        if (workPeriod.getWorkTemplateId() == null) {
            uiJson.setRows(list.subList((page - 1) * rows, page * rows > list.size() ? list.size() : page * rows));
        } else {
            uiJson.setRows(list);
        }
        return uiJson;
    }

    @Override
    public FormJson delWorkPeriod(int[] ids, int falg) {
        if (ids == null) {
            return FormJsonBulider.fail("参数不能为空");
        } else {
            if (workSimulatorMapper.delWorkPeriod(ids, falg) != ids.length) {
                return FormJsonBulider.fail("删除失败");
            }
        }
        return FormJsonBulider.success();
    }


    @Override
    public List<WorkPattern> findAllWorkPatternName(User user) {
        return workSimulatorMapper.findAllWorkPatternName(user.getId(), 0);
    }

    @Override
    public List<WorkPattern> findWorkPatternCheckId(int[] ids) {
        return workSimulatorMapper.findWorkPatternCheckId(ids);
    }

    @Override
    public FormJson operationWorkPeriod(WorkPeriod period) {
        WorkPattern workPattern = new WorkPattern();
        workPattern.setId(period.getWorkTemplateId());
        if (period.getWorkTemplateId() == null) {
            return FormJsonBulider.fail("工况id不能为空失败");
        }
        SimulateService service = null;
        for (Asset<?> asset : assetStore.getAssets()) {
            if (asset instanceof SimulateService) {
                service = (SimulateService) asset;
            }
        }
        if (period.getId() == null) {
            if (service != null && period.getRunData() != null) {
                JSONObject rundata = JSONObject.parseObject(period.getRunData());
                JSONArray speeds = rundata.getJSONArray("speed");
                dispatch(service, speeds);
                JSONArray cable = rundata.getJSONArray("cable");
                dispatch(service, cable);
                JSONArray pressure = rundata.getJSONArray("pressure");
                dispatch(service, pressure);
                JSONArray level = rundata.getJSONArray("level");
                dispatch(service, level);
                JSONArray positionfeedback = rundata.getJSONArray("Position_feedback");
                dispatch(service, positionfeedback);
//					JSONArray flow = rundata.getJSONArray("flow");
//					dispatch(service, flow);
//					JSONArray conductivity = rundata.getJSONArray("conductivity");
//					dispatch(service, conductivity);
                JSONArray temperature = rundata.getJSONArray("temperature");
                dispatch(service, temperature);
            }
            period.setRunState(1);
            int insertWorkPeriod = workSimulatorMapper.insertWorkPeriod(period);
            workPattern.setState(1);
            int updateSimulatorWork = workSimulatorMapper.updateSimulatorWork(workPattern);
            if (insertWorkPeriod != 1) return FormJsonBulider.fail(insertWorkPeriod + "");
            if (updateSimulatorWork != 1) return FormJsonBulider.fail(updateSimulatorWork + "");
            return FormJsonBulider.success().withCause(period.getId() + "");
        } else {
            workPattern.setState(0);
            period.setRunState(0);
            if (service != null) {
                service.stopSimulateWork();
            }
            if (workSimulatorMapper.updateSimulatorWork(workPattern) == 0) {
                return FormJsonBulider.fail("修改失败");
            }
            ;
            if (workSimulatorMapper.updateWorkPeriod(period) == 0) {//修改工况
                return FormJsonBulider.fail("修改失败");
            }
        }
        return FormJsonBulider.success();
    }

    private void dispatch(SimulateService service, JSONArray datas) {
        for (int i = 0; i < datas.size(); i++) {
            JSONArray tmps = datas.getJSONObject(i).getJSONArray("tmp");
            for (int j = 0; j < tmps.size(); j++) {
                JSONObject node = tmps.getJSONObject(j);
                String fullName = node.getString("fullName");
                String value = node.getString("value");
                service.insetSimulateWork(fullName, value);
            }
        }
    }


}
