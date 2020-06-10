package com.vibe.service.device;

import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Map;


public interface DeviceService {

    Map<String, Object> getPreparData(Integer id);

    /*public List<FeildViewType> getFormData(Integer id);*/
    ResponseEntity<byte[]> printDevice(String ids) throws IOException, Exception;
}
