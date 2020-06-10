package com.vibe.service.park;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vibe.mapper.park.ParkCameraRecognitionMapper;
import com.vibe.pojo.park.CameraRecognitionData;
import com.vibe.utils.FormJson;
import com.vibe.utils.FormJsonBulider;
import com.vibe.utils.Page;

@Service
public class ParkCameraRecognitionServiceimpl implements ParkCameraRecognitionService {

    @Autowired
    private ParkCameraRecognitionMapper pcrm;

    @Override
    public Page<CameraRecognitionData> findCameraRecognitionLog(int pageNum, int pageSize, CameraRecognitionData data) {
        PageHelper.startPage(pageNum, pageSize);
        List<CameraRecognitionData> list = pcrm.findCameraRecognitionLog(data);
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

    @Override
    public FormJson delCameraRecognitionlog(int[] ids) {
        if (null == ids) {
            return FormJsonBulider.fail("参数不能为空");
        } else {
            if (pcrm.delCameraRecognitionlog(ids) == 0) {
                return FormJsonBulider.fail("删除失败");
            }
        }
        return FormJsonBulider.success();
    }

}
