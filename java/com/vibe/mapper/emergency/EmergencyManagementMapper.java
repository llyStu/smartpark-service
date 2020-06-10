package com.vibe.mapper.emergency;

import com.vibe.pojo.emergency.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmergencyManagementMapper {
    int addEmergencyTask(EmergencyTask e);

    int updateEmergencyTask(EmergencyTask e);

    int deleteEmergencyTask(@Param("etids") int[] etids);

    List<EmergencyTask> findEmergencyTask(EmergencyTaskVo e);

    int addEmergencyTaskDetail(EmergencyTaskDetail e);

    int updateEmergencyTaskDetail(EmergencyTaskDetail e);

    int delEmergencyTaskDetail(@Param("etdids") int[] etdids);

    int delEmergencyTaskDetailByParent(@Param("etids") int[] etids);

    List<EmergencyTaskDetail> findEmergencyTaskDetail(EmergencyTaskDetailVo vo);

    int addEmergency(Emergency e);

    int updateEmergency(Emergency e);

    int delEmergency(@Param("eids") int[] eids);

    List<Emergency> findEmergency(EmergencyVo vo);
}
