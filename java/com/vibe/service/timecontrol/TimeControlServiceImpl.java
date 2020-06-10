package com.vibe.service.timecontrol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibe.common.code.CodeDictManager;
import com.vibe.common.code.CodeItem;
import com.vibe.mapper.task.TaskDao;
import com.vibe.mapper.timecontrol.TimeControlDao;
import com.vibe.monitor.asset.AssetStore;
import com.vibe.monitor.util.UnitInterface;
import com.vibe.monitor.util.UnitUtil;
import com.vibe.pojo.TimeControlBean;
import com.vibe.pojo.TimeControlTimePointBean;
import com.vibe.scheduledtasks.ScheduledTaskBean;
import com.vibe.scheduledtasks.ScheduledTaskManager;
import com.vibe.scheduledtasks.statisticstask.TimeControlDaoBean;
import com.vibe.scheduledtasks.statisticstask.TimeControlLogBean;
import com.vibe.scheduledtasks.statisticstask.TimeControlTimePointDaoBean;

@Service
public class TimeControlServiceImpl implements TimeControlService {

    private static final String TIME_CONTROL = "TIME_CONTROL";

    private static final String Time_Control_scheduled_task = "com.vibe.scheduledtasks.statisticstask.TimeControlScheduledTask";
    @Autowired
    CodeDictManager codeDictManager;
    @Autowired
    private TimeControlDao timeControlDao;
    @Autowired
    private AssetStore assetStore;
    @Autowired
    private TaskDao taskDao;

    @Override
    public List<TimeControlBean> queryTimeControlList() {
        // TODO Auto-generated method stub
        List<TimeControlDaoBean> timeControlDaoBeans = timeControlDao.queryTimeControlList();
        List<TimeControlBean> timeControlBeans = new ArrayList<>();
        for (TimeControlDaoBean timeControlDaoBean : timeControlDaoBeans) {
            TimeControlBean timeControlBean = new TimeControlBean();
            timeControlBean.setCatlog(timeControlDaoBean.getCatlog());
            CodeItem codeItem = codeDictManager.getLocalDict().getItem(
                    (short) com.vibe.service.classification.Code.PROBE, (short) timeControlDaoBean.getCatlog());
            timeControlBean.setCatlogName(codeItem.getName());
            timeControlBean
                    .setControlIds(stringListToIntList(Arrays.asList(timeControlDaoBean.getControlIds().split(","))));
            List<String> controlNames = new ArrayList<>();
            for (Integer controlId : timeControlBean.getControlIds()) {
                controlNames.add(assetStore.findAsset(controlId).getCaption());
            }
            timeControlBean.setControlNames(controlNames);
            timeControlBean.setId(timeControlDaoBean.getId());
            timeControlBean.setName(timeControlDaoBean.getName());
            timeControlBean.setState(timeControlDaoBean.getState());
            List<TimeControlTimePointDaoBean> controlTimePointDaoBeans = timeControlDao
                    .queryTimeControlTimePointList(timeControlBean.getId());
            List<TimeControlTimePointBean> timeControlTimePointBeans = new ArrayList<>();
            String timeExpression = null;
            for (TimeControlTimePointDaoBean timeControlTimePointDaoBean : controlTimePointDaoBeans) {
                ScheduledTaskBean scheduledTaskBean = timeControlDao
                        .queryTimeControlScheduledTask(timeControlTimePointDaoBean.getScheduledTaskId());
                TimeControlTimePointBean timeControlTimePointBean = new TimeControlTimePointBean();
                timeControlTimePointBean.setId(timeControlTimePointDaoBean.getId());
                timeControlTimePointBean.setScheduledTaskId(timeControlTimePointDaoBean.getScheduledTaskId());
                timeControlTimePointBean.setTimeTaskId(timeControlTimePointDaoBean.getTimeTaskId());
                timeControlTimePointBean.setValue(timeControlTimePointDaoBean.getValue());
                timeControlTimePointBean.setUnit(codeItem.getUnit());
                UnitUtil.unitParse(codeItem.getUnit(), timeControlTimePointDaoBean.getValue() + "", new UnitInterface() {

                    @Override
                    public void parseResult(String result) {
                        // TODO Auto-generated method stub
                        timeControlTimePointBean.setValueStr(result);
                    }
                });
                timeExpression = scheduledTaskBean.getTimeExpression();
                String[] timeArr = timeExpression.split(" ");
                String second = timeArr[0];
                String minute = timeArr[1];
                String hour = timeArr[2];
                String time = hour + ":" + minute + ":" + second;
                timeControlTimePointBean.setTime(time);
                timeControlTimePointBeans.add(timeControlTimePointBean);
            }
            timeControlBean.setTimeControlTimePointBeans(timeControlTimePointBeans);
            System.out.println(timeExpression);
            timeControlBean.setDays(stringListToIntList(Arrays.asList(timeExpression.split(" ")[5].split(","))));
            timeControlBeans.add(timeControlBean);
        }
        return timeControlBeans;
    }

    private List<Integer> stringListToIntList(List<String> data) {
        List<Integer> result = new ArrayList<>();
        for (String item : data) {
            result.add(Integer.parseInt(item));
        }
        return result;
    }

    private String generateTimeExpression(String time, List<Integer> days) {
        StringBuilder sb = new StringBuilder();
        String[] timeArr = time.split(":");
        sb.append(timeArr[2]);
        sb.append(" ");
        sb.append(timeArr[1]);
        sb.append(" ");
        sb.append(timeArr[0]);
        sb.append(" ");
        sb.append("?");
        sb.append(" ");
        sb.append("*");
        sb.append(" ");
        for (int day : days) {
            sb.append(day);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        System.out.println("timeControl " + sb.toString());
        return sb.toString();
    }

    @Override
    public TimeControlBean insertTimeControl(TimeControlBean timeControlBean) {
        // TODO Auto-generated method stub
        TimeControlDaoBean timeControlDaoBean = new TimeControlDaoBean();
        timeControlDaoBean.setCatlog(timeControlBean.getCatlog());
        StringBuilder sb = new StringBuilder();
        for (Integer controlId : timeControlBean.getControlIds()) {
            sb.append(controlId);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        timeControlDaoBean.setControlIds(sb.toString());
        timeControlDaoBean.setName(timeControlBean.getName());
        timeControlDaoBean.setState(timeControlBean.getState());
        timeControlDao.insertTimeControl(timeControlDaoBean);
        timeControlBean.setId(timeControlDaoBean.getId());
        for (TimeControlTimePointBean timeControlTimePointBean : timeControlBean.getTimeControlTimePointBeans()) {
            insertTimeControlTimePoint(timeControlTimePointBean, timeControlBean);
        }
        return timeControlBean;
    }

    private void insertTimeControlTimePoint(TimeControlTimePointBean timeControlTimePointBean,
                                            TimeControlBean timeControlBean) {
        ScheduledTaskBean scheduledTaskBean = new ScheduledTaskBean();
        scheduledTaskBean.setState(timeControlBean.getState());
        scheduledTaskBean.setTimeExpression(
                generateTimeExpression(timeControlTimePointBean.getTime(), timeControlBean.getDays()));
        timeControlDao.insertTimeControlScheduledTask(scheduledTaskBean);
        timeControlTimePointBean.setScheduledTaskId(scheduledTaskBean.getId());
        TimeControlTimePointDaoBean timeControlTimePointDaoBean = new TimeControlTimePointDaoBean();
        timeControlTimePointDaoBean.setScheduledTaskId(scheduledTaskBean.getId());
        timeControlTimePointDaoBean.setValue(timeControlTimePointBean.getValue());
        timeControlTimePointDaoBean.setTimeTaskId(timeControlBean.getId());
        timeControlDao.insertTimeControlTimePoint(timeControlTimePointDaoBean);
        timeControlTimePointBean.setId(timeControlTimePointDaoBean.getId());
        timeControlTimePointBean.setTimeTaskId(timeControlBean.getId());
        if (scheduledTaskBean.getState() == 1) {
            try {
                ScheduledTaskManager.addJob(TIME_CONTROL + scheduledTaskBean.getId(),
                        Class.forName(Time_Control_scheduled_task), scheduledTaskBean.getTimeExpression(),
                        scheduledTaskBean);
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void deleteTimeControl(int id) {
        // TODO Auto-generated method stub
        timeControlDao.deleteTimeControl(id);
        List<TimeControlTimePointDaoBean> timeControlTimePointDaoBeans = timeControlDao
                .queryTimeControlTimePointList(id);
        for (TimeControlTimePointDaoBean timeControlTimePointDaoBean : timeControlTimePointDaoBeans) {
            taskDao.deleteScheduledTask(timeControlTimePointDaoBean.getScheduledTaskId());
            ScheduledTaskManager.removeJob(TIME_CONTROL + timeControlTimePointDaoBean.getScheduledTaskId());
        }
    }

    @Override
    public TimeControlBean updateTimeControl(TimeControlBean timeControlBean) {
        // TODO Auto-generated method stub
        TimeControlDaoBean timeControlDaoBean = new TimeControlDaoBean();
        timeControlDaoBean.setCatlog(timeControlBean.getCatlog());
        List<Integer> controlIds = timeControlBean.getControlIds();
        StringBuilder sb = new StringBuilder();
        for (Integer integer : controlIds) {
            sb.append(integer);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        timeControlDaoBean.setControlIds(sb.toString());
        timeControlDaoBean.setId(timeControlBean.getId());
        timeControlDaoBean.setName(timeControlBean.getName());
        timeControlDaoBean.setState(timeControlBean.getState());
        timeControlDao.updateTimeControl(timeControlDaoBean);
        for (TimeControlTimePointBean timeControlTimePointBean : timeControlBean.getTimeControlTimePointBeans()) {
            if (timeControlTimePointBean.getId() == 0) {
                insertTimeControlTimePoint(timeControlTimePointBean, timeControlBean);
            } else {
                TimeControlTimePointDaoBean timeControlTimePointDaoBean = new TimeControlTimePointDaoBean();
                timeControlTimePointDaoBean.setId(timeControlTimePointBean.getId());
                timeControlTimePointDaoBean.setScheduledTaskId(timeControlTimePointBean.getScheduledTaskId());
                timeControlTimePointDaoBean.setTimeTaskId(timeControlTimePointBean.getTimeTaskId());
                timeControlTimePointDaoBean.setValue(timeControlTimePointBean.getValue());
                timeControlDao.updateTimeControlTimePoint(timeControlTimePointDaoBean);
                ScheduledTaskBean scheduledTaskBean = new ScheduledTaskBean();
                scheduledTaskBean.setTimeExpression(
                        generateTimeExpression(timeControlTimePointBean.getTime(), timeControlBean.getDays()));
                scheduledTaskBean.setId(timeControlTimePointBean.getScheduledTaskId());
                scheduledTaskBean.setState(timeControlBean.getState());
                timeControlDao.updateScheduledTask(scheduledTaskBean);
                if (scheduledTaskBean.getState() == 1) {
                    ScheduledTaskManager.modifyJobTime(TIME_CONTROL + scheduledTaskBean.getId(),
                            scheduledTaskBean.getTimeExpression(), scheduledTaskBean);
                }
            }
        }
        List<TimeControlTimePointDaoBean> timeControlTimePointDaoBeans = timeControlDao
                .queryTimeControlTimePointList(timeControlBean.getId());
        for (TimeControlTimePointDaoBean timeControlTimePointDaoBean : timeControlTimePointDaoBeans) {
            boolean deleted = true;
            for (TimeControlTimePointBean timeControlTimePointBean : timeControlBean.getTimeControlTimePointBeans()) {
                if (timeControlTimePointBean.getId() == timeControlTimePointDaoBean.getId()) {
                    deleted = false;
                }
            }
            if (deleted) {
                taskDao.deleteScheduledTask(timeControlTimePointDaoBean.getScheduledTaskId());
                if (timeControlBean.getState() == 2) {
                    ScheduledTaskManager.removeJob(TIME_CONTROL + timeControlTimePointDaoBean.getScheduledTaskId());
                }
            }
        }
        return timeControlBean;
    }

    @Override
    public List<TimeControlLogBean> queryTimeControlLog(int id) {
        // TODO Auto-generated method stub
        return timeControlDao.queryTimeControlLog(id);
    }

}
