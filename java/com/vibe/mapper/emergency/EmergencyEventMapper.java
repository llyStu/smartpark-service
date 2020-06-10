package com.vibe.mapper.emergency;

import com.vibe.pojo.emergency.EmergencyEventType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmergencyEventMapper {

    List<EmergencyEventType> findAllEventType();

    //EmergencyEventType findOneEventType(@Param("eventName")String eventName, @Param("eventGrade")String eventGrade);

    List<EmergencyEventType> findAllEventGrade();


}
