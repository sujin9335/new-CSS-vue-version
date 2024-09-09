package com.codvill.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codvill.service.LoginService;
import com.codvill.service.UserService;


@Controller
public class LoginController {
    
    @Autowired
    LoginService ls;
    @Autowired
    UserService us;

    @GetMapping("/login")
    public String login() {

        return "/login";
    }

    @PostMapping("/vloginCheck")
    @ResponseBody
    public Object loginCheck(@RequestBody JSONObject jo, Model model, HttpSession session) {
        System.out.println("loginCheck 실행");
        JSONObject loginObj = new JSONObject();

        // System.out.println(id+ " " + pw);

        Map<String, Object> mapLoginInfo = (Map<String, Object>) jo.get("info");

        String id="";
        String pw="";

        if (mapLoginInfo.size() > 0) {
            id=mapLoginInfo.get("id").toString();
            pw=mapLoginInfo.get("pw").toString();
        }

        Map<String, Object> map=ls.loginCheck(id,pw);
        System.out.println(map);

        if(map == null) { //로그인 실패
            System.out.println("로그인실패");
            // model.addAttribute("msg", "아이디 또는 비밀번호가 잘못되었습니다");
            loginObj.put("msg", "아이디 또는 비밀번호가 잘못되었습니다");

        }else {
            if((Integer)map.get("user_lock_cnt") > 4) { //로그인 횟수 5회 이상이면 접속금지
                // model.addAttribute("msg", "로그인 실패 5회입니다 관리자 문의하세요");
                loginObj.put("msg", "로그인 실패 5회입니다 관리자 문의하세요");
            }else { 

                if(map.get("id") != null) { //로그인시 비번만 틀렸을경우
                    //로그인 실패 카운트 증가 
                    // model.addAttribute("msg", "비밀번호를 " + (Integer.parseInt(map.get("user_lock_cnt").toString()) +1) + "회 잘못 입력하셨습니다");
                    ls.updateLockCnt(id);
                    loginObj.put("msg", "비밀번호를 " + (Integer.parseInt(map.get("user_lock_cnt").toString()) +1) + "회 잘못 입력하셨습니다");

                }else { 
                    
                    String user_use=(String) map.get("user_use");
                    if (user_use.equals("n")) { //계정 잠금 검사
                        // model.addAttribute("msg", "계정 잠금 상태입니다 관리자 문의하세요");
                        loginObj.put("msg", "계정 잠금 상태입니다 관리자 문의하세요");
                    }else { //로그인 성공
                        //로그인횟수 초기화
                        ls.resetLockCnt(id);
            
                        System.out.println("로그인성공");
                        Map<String, Object> userInfo=new HashMap<>();
                        userInfo.put("user_id", map.get("user_id"));
                        userInfo.put("user_name", map.get("user_name"));
                        userInfo.put("user_auth", map.get("user_auth"));
            
                        loginObj.put("userInfo", userInfo);
            
                        //유저 정보가져오기
                        // String name=(String) map.get("user_name");
                        // String mail=(String) map.get("user_mail");
                        // String tel=(String) map.get("user_tel");
                        // int auth=(Integer) map.get("user_auth");
            
                        // session.setAttribute("id", id);
                        // session.setAttribute("name", name);
                        // session.setAttribute("mail", mail);
                        // session.setAttribute("tel", tel);
                        // session.setAttribute("auth", auth);
                        session.setAttribute("userInfo", userInfo);
                        System.out.println("=====================================");
                        System.out.println(userInfo);
                        System.out.println("=====================================");

                    }

                }

            }
            
        }
        
        return loginObj;

    }

    @PostMapping("/vlogout")
    @ResponseBody
    public Object logout(HttpSession session) {
        JSONObject logoutObj = new JSONObject();

        if (session.getAttribute("userInfo") != null) {
            session.invalidate();
            logoutObj.put("msg", "logout");
        }else {
            logoutObj.put("msg", "logout failed");
        }

        return logoutObj;
    }

    @PostMapping("/vgetSession")
    @ResponseBody
    public Object getSession(HttpSession session) {
        JSONObject sessionObj = new JSONObject();
        Map<String, Object> userInfo = (Map<String, Object>) session.getAttribute("userInfo");

        if (userInfo != null) {
            sessionObj.put("userInfo", userInfo);
        }else {
            sessionObj.put("msg", "getSession failed");
        }

        return sessionObj;
    }

    @PostMapping("/vIdCheck")
    @ResponseBody
    public Object idCheck(@RequestBody JSONObject jo) {
        System.out.println("ID체크 실행");
        JSONObject idCheckObj=new JSONObject();
        System.out.println(jo);

        Map<String, Object> mapResisId = (Map<String, Object>) jo.get("info");
        idCheckObj=(JSONObject) us.idCheck(mapResisId);

        return idCheckObj;

    }

    @PostMapping("/vRegis")
    @ResponseBody
    public Object insert(@RequestBody JSONObject jo) {
        System.out.println("Insert 실행");
        JSONObject obj=new JSONObject();
        System.out.println(jo);

        Map<String, Object> mapResisInfo = (Map<String, Object>) jo.get("info");
        obj=(JSONObject) us.insert(mapResisInfo);

        return obj;

    }

    
}
