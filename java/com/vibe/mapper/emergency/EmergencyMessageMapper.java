package com.vibe.mapper.emergency;

import com.vibe.pojo.emergency.EmergencyMessage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmergencyMessageMapper {

    List<EmergencyMessage> findAllEmergencyMessage(EmergencyMessage emergencyMessage);

    int addEmergencyMessage(EmergencyMessage emergencyMessage);

    EmergencyMessage findOneEmergencyMessage(@Param("id") Integer id);

    int updateEmergencyMessage(EmergencyMessage emergencyMessage);

    int deleteEmergencyMessage(@Param("id") int id);

    int deleteEmergencyMessages(@Param("ids") int[] ids);


}
