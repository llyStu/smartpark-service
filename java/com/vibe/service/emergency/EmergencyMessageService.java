package com.vibe.service.emergency;


import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vibe.mapper.emergency.EmergencyMessageMapper;
import com.vibe.pojo.emergency.EmergencyMessage;
import com.vibe.utils.FormJson;
import com.vibe.utils.FormJsonBulider;
import com.vibe.utils.Page;

@Service
public class EmergencyMessageService {

    @Autowired
    private EmergencyMessageMapper emm;

    public Page<EmergencyMessage> findAllEmergencyMessage(int page, int rows, EmergencyMessage emergencyMessage) {
        // TODO Auto-generated method stub
        PageHelper.startPage(page, rows);
        List<EmergencyMessage> list = emm.findAllEmergencyMessage(emergencyMessage);
        return toPage(list);
    }

    private static <T> Page<T> toPage(List<T> list) {
        PageInfo<T> page = new PageInfo<>(list);
        Page<T> result = new Page<>();
        result.setRows(list);
        result.setPage(page.getPageNum());
        result.setSize(page.getPageSize());
        result.setTotal((int) page.getTotal());
        return result;
    }

    public FormJson addEmergencyMessage(EmergencyMessage emergencyMessage) {
        emergencyMessage.setBeginTime(new Date());
        emergencyMessage.setProvenance("手动");
        if (1 != emm.addEmergencyMessage(emergencyMessage)) {
            return FormJsonBulider.fail("消息添加失败");
        }
        return FormJsonBulider.success();
    }

    public EmergencyMessage findOneEmergencyMessage(Integer id) {

        return emm.findOneEmergencyMessage(id);
    }

    public FormJson updateEmergencyMessage(EmergencyMessage em) {
        if (em.getId() == null) {
            return FormJsonBulider.fail("需要id");
        }
        if (em.getPid() == null) {
            return FormJsonBulider.fail("需要父级id");
        }
        if (emm.updateEmergencyMessage(em) != 1) {
            return FormJsonBulider.fail(null);
        }
        if (2 == em.getStatus()) {
            em.setEndTime(new Date());
        }
        return FormJsonBulider.success();
    }

    public FormJson deleteEmergencyMessage(int[] ids) {
        if (ids == null || ids.length == 0)
            FormJsonBulider.success();
        int deleted = emm.deleteEmergencyMessages(ids);
        if (deleted != ids.length) {
            return FormJsonBulider.fail("只删除了 " + deleted + " 条记录");
        }
        emm.deleteEmergencyMessages(ids);
        return FormJsonBulider.success();
    }


}
