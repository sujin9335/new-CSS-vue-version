package com.codvill.controller;

import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codvill.service.MonitorService;

@Controller
public class MonitorController {
    
    @Autowired
    MonitorService ms;

    @PostMapping("/vmonitorList")
    @ResponseBody
    public ResponseEntity<Object> monitorList() {
        System.out.println("monitorList 작동");
        JSONObject obj = new JSONObject();


        try {
            obj=ms.monitorList();
            return ResponseEntity.ok(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }

        
    }

    @PostMapping("/vmonitorCoordinateUpdate")
    @ResponseBody
    public ResponseEntity<Object> monitorCoordinateUpdate(@RequestBody Map<String, Object> param) {
        System.out.println("monitor좌표 업데이트 작동");
        JSONObject obj = new JSONObject();
        System.out.println(param);

        try {
            obj=ms.monitorCoordinateUpdate(param);
            return ResponseEntity.ok(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }

        
    }

    @PostMapping("/vmonitorInsert")
    @ResponseBody
    public ResponseEntity<Object> monitorInsert(@RequestBody Map<String, Object> param) {
        System.out.println("monitorInsert 작동");
        JSONObject obj = new JSONObject();
        System.out.println(param);

        try {
            ms.monitorInsert(param);
            return ResponseEntity.ok(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }

        
    }

    @PostMapping("/vmonitorUpdate")
    @ResponseBody
    public ResponseEntity<Object> monitorUpdate(@RequestBody Map<String, Object> param) {
        System.out.println("monitorUpdate 작동");
        JSONObject obj = new JSONObject();
        System.out.println(param);

        try {
            ms.monitorUpdate(param);
            return ResponseEntity.ok(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }

        
    }

    @PostMapping("/vmonitorDelete")
    @ResponseBody
    public ResponseEntity<Object> monitorDelete(@RequestBody Map<String, Object> param) {
        System.out.println("monitorDelete 작동");
        JSONObject obj = new JSONObject();
        System.out.println(param);

        try {
            ms.monitorDelete(param);
            return ResponseEntity.ok(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }

        
    }





}
