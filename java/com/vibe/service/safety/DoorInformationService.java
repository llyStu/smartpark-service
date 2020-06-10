package com.vibe.service.safety;


import java.util.List;

import com.vibe.pojo.safety.SafetyMessage;
import com.vibe.utils.Page;

public interface DoorInformationService {

    public Page<SafetyMessage> findDoorRecord(int pageNum, int pageSize, SafetyMessage message);


    public List<SafetyMessage> findAllDoorRecord(SafetyMessage message);
}
