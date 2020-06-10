package com.vibe.mapper.park;

import com.vibe.pojo.park.CameraRecognitionData;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkCameraRecognitionMapper {

    List<CameraRecognitionData> findCameraRecognitionLog(CameraRecognitionData data);

    int delCameraRecognitionlog(@Param("ids") int[] ids);

}
