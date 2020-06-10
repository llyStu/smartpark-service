package com.vibe.service.information;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vibe.mapper.information.ShowTaskMapper;
import com.vibe.pojo.infomation.Message;
import com.vibe.pojo.infomation.ShowTask;
import com.vibe.pojo.infomation.ShowTaskVo;
import com.vibe.service.information.push.Client;
import com.vibe.service.information.push.Msg;
import com.vibe.utils.Page;

@Service
public class ShowTaskService {
    @Autowired
    private ShowTaskMapper stm;

    private ScheduledExecutorService ses;
    private Instant next = Instant.MAX;
    private ScheduledFuture<?> schedule;

    public ShowTaskService() {
        ses = Executors.newSingleThreadScheduledExecutor();
        Client.setListener(Client.SUBSCRIBE + "=ShowTask", (client, msg) -> {
            try {
                Integer equipment = Integer.valueOf(msg.getDesc());
                List<ShowTask> currentTask = this.getCurrentTask(equipment);
                client.write(Msg.of("ShowTask", msg.getDesc(), currentTask));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void addTask(ShowTask st) {
        stm.add(st);
        if (!notifyClient(st)) schedule(st.getStartTime(), false);
    }

    private boolean notifyClient(ShowTask st) {
        Instant now = Instant.now();
        if (ShowTask.INVALID.equals(st.getType())
                || st.getEndTime().toInstant().minus(Duration.ofMinutes(3)).isBefore(now)) {
            return true;
        }
        if (st.getStartTime().toInstant().isBefore(now)) {
            sendMsg(st);
            return true;
        }
        return false;
    }

    private synchronized void schedule(Date startTime, boolean go) {
        Instant time = startTime.toInstant();
        if (!go && (time.isAfter(next) || time.equals(next))) return;
        if (!go && schedule != null) schedule.cancel(false);
        next = time;
        schedule = ses.schedule(
                () -> this.runTask(time),
                ChronoUnit.SECONDS.between(Instant.now(), next),
                TimeUnit.SECONDS);
    }

    private synchronized void runTask(Instant time) {
        Date startTime = new Date(time.toEpochMilli());

        Date newStart = stm.getAfterStart(startTime);
        if (newStart == null) {
            next = Instant.MAX;
            schedule = null;
        } else
            schedule(newStart, true);


        List<ShowTask> tasks = stm.getByStartAfter(startTime);
        if (tasks == null || tasks.isEmpty()) return;
        sendMsg(tasks);
    }

    public void cancelTask(Integer[] ids) {
        stm.setType(ids, ShowTask.INVALID);

        sendMsg(stm.getByIds(ids));
    }

    private void sendMsg(ShowTask st) {
        sendMsg(Arrays.asList(st));
    }

    private void sendMsg(List<ShowTask> sts) {
        HashMap<Integer, List<ShowTask>> map = new HashMap<>();
        for (ShowTask it : sts) {
            List<ShowTask> list = map.get(it.getEquipment());
            if (list == null) {
                list = new ArrayList<>();
                map.put(it.getEquipment(), list);
            }
            list.add(it);
        }
        for (Entry<Integer, List<ShowTask>> it : map.entrySet()) {
            try {
                Msg msg = Msg.of("ShowTask", it.getKey().toString(), it.getValue());
                Client.writeToAll(msg);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateTask(ShowTask st) {
        stm.update(st);
        ShowTask updated = stm.getById(st.getId());
        Instant starttime = updated.getStartTime().toInstant();
        if (starttime.isAfter(next) || starttime.equals(next)) {
            return;
        }
        if (starttime.isAfter(Instant.now())) {
            if (!updated.getType().equals(ShowTask.INVALID))
                schedule(updated.getStartTime(), false);
            return;
        }
        if (updated.getEndTime().before(new Date())) {
            return;
        }
        sendMsg(updated);
        if (st.getEquipment() != null && !st.getEquipment().equals(updated.getEquipment())) {
            updated.setEquipment(st.getEquipment());
            updated.setType(ShowTask.INVALID);
            sendMsg(updated);
        }
    }

    public Page<ShowTask> getTask(ShowTaskVo stv, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ShowTask> list = stm.find(stv);
        PageInfo<ShowTask> page = new PageInfo<>(list);

        Page<ShowTask> result = new Page<>();
        result.setRows(list);
        result.setPage(page.getPageNum());
        result.setSize(page.getPageSize());
        result.setTotal((int) page.getTotal());
        return result;
    }

    public List<ShowTask> getCurrentTask(Integer equipment) {
        LocalDateTime now = LocalDateTime.now();
        ShowTaskVo stv = new ShowTaskVo();
        stv.setType(ShowTask.VALID);
        stv.setEquipment(equipment);

        String startTime = now.plusMinutes(1).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        stv.setStartTimeMax(startTime);

        String endTime = now.plusMinutes(1).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        stv.setEndTimeMin(endTime);

        List<ShowTask> find = stm.find(stv);
        find.forEach(it -> {
            if (it.getUrl() != null && it.getUrl().startsWith("pid=")) {
                it.setUrl("information/detail?" + it.getUrl());
            }
        });
        return find;
    }

    public ShowTask get(Integer id) {
        return stm.getById(id);
    }

}
