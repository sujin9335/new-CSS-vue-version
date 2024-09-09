package com.codvill.controller;

import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codvill.dao.TestDao;

import oracle.jdbc.proxy.annotation.Post;

@Controller
public class TestController {

    @Autowired
    TestDao td;
    

    @PostMapping("/test")
    @ResponseBody
    public Object test(@RequestBody JSONObject jo) {
        Map<String, Object> map = (Map<String, Object>) jo.get("info");
        System.out.println(map);
        // String id=map.get("map")
        JSONObject loginObj = new JSONObject();
        loginObj.put("성공", "성공");
        return loginObj;
    }

    @GetMapping("/test2")
    public String test2() {

        return "test2";
    }

    @PostMapping("/moveTest")
    @ResponseBody
    public Object moveTest() {
        JSONObject oj = new JSONObject();
        oj=td.moveTest();

        return oj; 
    }


}
