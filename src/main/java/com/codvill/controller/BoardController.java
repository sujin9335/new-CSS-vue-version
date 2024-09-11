package com.codvill.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.codvill.service.BoardService;


@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    BoardService bs;

    @Value("${project.uploadpath}") //파일 업로드 경로
    private String uploadPath;
    
    @PostMapping("/vlist")
    @ResponseBody
    public ResponseEntity<Object> list(@RequestBody Map<String, Object> param) {
        System.out.println("list 작동");
        System.out.println(param);

        Map<String, Object> paramMap = new HashMap<String, Object>();

        try {
            if(param.get("param") != null) {
                paramMap = (Map<String, Object>) param.get("param");
                JSONObject obj =new JSONObject();
                obj = bs.boardList(paramMap);
                
                return ResponseEntity.ok(obj);
            }else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 요청입니다");
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("오류입니다");
        }
        
    }
    

    

   
    
    
}
