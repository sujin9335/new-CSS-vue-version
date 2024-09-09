package com.codvill.service;

import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codvill.dao.MonitorDao;

@Service
public class MonitorService {
    
    @Autowired
    MonitorDao md;

    public JSONObject monitorList() {
        return md.monitorList();
    }

    public JSONObject monitorCoordinateUpdate(Map<String, Object> param) {
        return md.monitorCoordinateUpdate(param);
    }

    public JSONObject monitorInsert(Map<String, Object> param) {
        return md.monitorInsert(param);
    }

    public JSONObject monitorUpdate(Map<String, Object> param) {
        return md.monitorUpdate(param);
    }

    public JSONObject monitorDelete(Map<String, Object> param) {
        return md.monitorDelete(param);
    }



}
