package com.vibe.service.park;



import com.vibe.pojo.park.CameraRecognitionData;
import com.vibe.utils.FormJson;
import com.vibe.utils.Page;

public interface ParkCameraRecognitionService {


	Page<CameraRecognitionData> findCameraRecognitionLog(int pageNum, int pageSize, CameraRecognitionData data);

	FormJson delCameraRecognitionlog(int[] ids);

}
