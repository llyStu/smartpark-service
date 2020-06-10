package com.vibe.service.emergency;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vibe.mapper.emergency.OperationMapper;
import com.vibe.pojo.emergency.Operation;
import com.vibe.pojo.emergency.OperationVo;
import com.vibe.utils.FormJson;
import com.vibe.utils.FormJsonBulider;
import com.vibe.utils.Page;

@Service
public class OperationService {
    @Autowired
    private OperationMapper om;

    public FormJson insertOperation(Operation oper) {
        int saveOperation = om.insertOperation(oper);
        if (saveOperation != 1) return FormJsonBulider.fail(saveOperation + "");
        return FormJsonBulider.success().withCause(oper.getOid() + "");
    }

    public FormJson deleteOperation(int[] oids) {
        if (oids == null || oids.length == 0) return FormJsonBulider.success();
        int saveOperation = om.deleteOperation(oids);
        return FormJsonBulider.success().withCause(saveOperation + "");
    }

    public FormJson updateOperation(Operation oper) {
        if (oper.getOid() == null) return FormJsonBulider.fail(null);

        int saveOperation = om.updateOperation(oper);
        return FormJsonBulider.success().withCause(saveOperation + "");
    }

    public Page<Operation> queryOperation(OperationVo oper, int pnum, int psize) {
        PageHelper.startPage(pnum, psize);
        List<Operation> data = om.queryOperation(oper);
        return from(data);
    }


    public static <T> Page<T> from(List<T> list) {
        PageInfo<T> page = new PageInfo<>(list);

        Page<T> result = new Page<>();
        result.setRows(list);
        result.setPage(page.getPageNum());
        result.setSize(page.getPageSize());
        result.setTotal((int) page.getTotal());
        return result;
    }
}
