package com.codvill.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codvill.service.HighChartService;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

@Controller
@RequestMapping("/highChart")
public class HighChartController {

    @Autowired
    HighChartService hs;

    @GetMapping("/")
    public String highChart() {

        return "chart";
    }


    
    @PostMapping("/line")
    @ResponseBody
    public Object line() {
        JSONArray obj=new JSONArray();
        obj=hs.line();
        
        return obj;
    }

    @PostMapping("/pie")
    @ResponseBody
    public Object pie() {
        JSONObject obj=new JSONObject();
        obj=(JSONObject) hs.pie();
        // System.out.println(obj);

        return obj;
    }

    @PostMapping("/barDrill")
    @ResponseBody
    public Object barDrill() {
        JSONObject obj=new JSONObject();
        obj=(JSONObject) hs.barDrill();

        return obj;
    }

    @PostMapping("/lineClick")
    @ResponseBody
    public Object lineClick(@RequestBody Map<String, Object> param) {
        JSONArray arr=new JSONArray();
        // System.out.println(param);
        arr=(JSONArray) hs.lineClick(param);

        return arr;
    }

    @PostMapping("/pieDrill")
    @ResponseBody
    public Object pieDrill() {
        JSONObject obj=new JSONObject();
        obj=(JSONObject) hs.pieDrill();

        return obj;
    }


    @PostMapping("/vLine")
    @ResponseBody
    public Object vLine() {
        JSONArray LineObj=new JSONArray();
        LineObj=hs.line();
        System.out.println(LineObj);
        
        return LineObj;
    }

    @PostMapping("/vLineClick")
    @ResponseBody
    public Object vLineClick(@RequestBody JSONObject jo) {
        Map<String, Object> map = (Map<String, Object>) jo;

        JSONArray arr=new JSONArray();

        arr=(JSONArray) hs.lineClick(map);
        // System.out.println("반환");
        // System.out.println(arr);
        return arr;
    }

    @PostMapping("/vBarDrill")
    @ResponseBody
    public Object vBarDrill() {
        JSONObject obj=new JSONObject();
        obj=(JSONObject) hs.barDrill();
        System.out.println(obj);
        return obj;
    }

    @PostMapping("/vPie")
    @ResponseBody
    public Object vPie() {
        JSONObject obj=new JSONObject();
        obj=(JSONObject) hs.pie();
        System.out.println("=========데이타");
        System.out.println(obj);

        return obj;
    }

    @PostMapping("/vPieDrill")
    @ResponseBody
    public Object vPieDrill() {
        JSONObject obj=new JSONObject();
        obj=(JSONObject) hs.pieDrill();

        return obj;
    }

    @PostMapping("/vRain")
    @ResponseBody
    public Object vRain() {
        JSONArray arr=new JSONArray();
        arr=(JSONArray) hs.vRain();

        return arr;
    }

}
