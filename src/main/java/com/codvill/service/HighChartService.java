package com.codvill.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.codvill.dao.HighChartDao;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

@Service
public class HighChartService {

    @Autowired
    HighChartDao hd;

    public JSONArray line() {
        JSONArray obj = new JSONArray();

        List<Map<String, Object>> list = hd.line();
        // System.out.println("쿼리결과");
        // System.out.println(list.toString());

        // 게시글 0개인 날짜 포함 return
        if (list.size() > 0) {

            // 시작 날짜 구하기
            String startDateString = (String) list.get(0).get("total_date");
            String[] startDateArr = startDateString.split("-");
            int[] startDateIntArr = new int[startDateArr.length];

            // 문자열을 int로 변환하여 int 배열에 저장
            for (int i = 0; i < startDateArr.length; i++) {
                startDateIntArr[i] = Integer.parseInt(startDateArr[i]);
            }
            // System.out.println("시작날짜");
            // System.out.println(Arrays.toString(startDateIntArr));

            // 종료 날짜 구하기
            String endDateString = (String) list.get(list.size() - 1).get("total_date");
            String[] endDateArr = endDateString.split("-");
            int[] endDateIntArr = new int[endDateArr.length];

            // 문자열을 int로 변환하여 int 배열에 저장
            for (int i = 0; i < endDateArr.length; i++) {
                endDateIntArr[i] = Integer.parseInt(endDateArr[i]);
            }

            // System.out.println(Arrays.toString(endDateIntArr));

            // 날짜 형식 정의
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            // 시작 날짜와 종료 날짜를 설정합니다.
            LocalDate startDate = LocalDate.of(startDateIntArr[0], startDateIntArr[1], startDateIntArr[2]); // 2024년 7월
                                                                                                            // 15일
            LocalDate endDate = LocalDate.of(endDateIntArr[0], endDateIntArr[1], endDateIntArr[2]); // 2024년 7월 25일

            // 날짜 목록을 생성합니다.
            List<LocalDate> dates = new ArrayList<>();
            LocalDate currentDate = startDate;

            while (!currentDate.isAfter(endDate)) {
                dates.add(currentDate);
                currentDate = currentDate.plusDays(1);
            }

            // 날짜 목록을 출력해 list에 없으면 total=0, 있으면 total 데이터를 그대로
            for (LocalDate date : dates) {
                String formattedDate = date.format(formatter);
                Map<String, Object> m = new HashMap<>();
                m.put("total_date", formattedDate);
                m.put("total", 0);

                for (Map<String, Object> map : list) {
                    if (map.get("total_date").equals(formattedDate)) {
                        m.put("total", map.get("total"));
                    }
                }

                obj.add(m);
            }

        }
        System.out.println();
        System.out.println("데이터");

        System.out.println(obj);
        System.out.println();

        return obj;
    }

    public JSONObject pie() {
        JSONObject obj = new JSONObject();
        obj = hd.pie();

        return obj;
    }

    public JSONObject barDrill() {
        JSONObject obj = new JSONObject();

        List<Map<String, Object>> list = hd.line();
        // System.out.println("쿼리결과");
        // System.out.println(list.toString());

        if (list.size() > 0) {

            for (Map<String, Object> map : list) {
                String date = (String) map.get("total_date");
                List<Map<String, Object>> drillData = hd.drill(date);
                obj.put(date, drillData);
            }

        }

        System.out.println(obj);

        return obj;
    }

    public JSONArray lineClick(Map<String, Object> param) {
        JSONArray arr = new JSONArray();
        String date = (String) param.get("date");

        List<Map<String, Object>> clickData = hd.drill(date);

        for (Map<String, Object> map : clickData) {
            JSONObject obj = new JSONObject(map);
            arr.add(obj);
        }

        System.out.println();
        System.out.println("라인클릭데이터");
        // System.out.println(clickData);
        System.out.println(arr);

        return arr;
    }

    public JSONObject pieDrill() {
        JSONObject obj = new JSONObject();

        List<Map<String, Object>> list = hd.pieList();
        // System.out.println("쿼리결과");
        // System.out.println(list.toString());

        if (list.size() > 0) {

            for (Map<String, Object> map : list) {
                int id = (Integer) map.get("group_id");
                List<Map<String, Object>> drillData = hd.pieDrill(id);
                obj.put(id + "", drillData);
            }

        }

        System.out.println(obj);

        return obj;
    }

    public JSONArray vRain() {
        JSONArray arr = new JSONArray();

        Instant now = Instant.now();
        System.out.println("Current UTC time: " + now);

        // UTC 시간을 시스템 기본 시간대의 LocalDateTime으로 변환
        LocalDateTime localDateTime = LocalDateTime.ofInstant(now, ZoneId.systemDefault());
        System.out.println("Local date and time: " + localDateTime);

        // 시간만 추출
        LocalTime localTime = localDateTime.toLocalTime();
        System.out.println("Current time: " + localTime);

        // 포맷팅
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH");
        String formattedTime = localTime.format(formatter)+"00";
        System.out.println("Formatted time: " + formattedTime);
		
		
		//좌표데이터
        int seoulX = 60;
        int seoulY = 127;
        int busanX = 98;
        int busanY = 76;
        int deaeguX = 89;
        int deaeguY = 90;
        int incheonX = 55;
        int incheonY = 124;

		
		int seoulRain = rainData(formattedTime ,seoulX, seoulY);
		int busanRain = rainData(formattedTime ,busanX, busanY);
		int deaeguRain = rainData(formattedTime ,deaeguX, deaeguY);
		int incheonRain = rainData(formattedTime ,incheonX, incheonY);

        // List<Map<String, Object>> list = new ArrayList<>();
		
		Map<String, Object> MapSeoul = new HashMap<>();
		MapSeoul.put("name", "서울");
		MapSeoul.put("y", seoulRain);
		
		Map<String, Object> MapBusan = new HashMap<>();
		MapBusan.put("name", "부산");
		MapBusan.put("y", busanRain);
		
		Map<String, Object> MapDeaegu = new HashMap<>();
		MapDeaegu.put("name", "대구");
		MapDeaegu.put("y", deaeguRain);
		
		Map<String, Object> MapIncheon = new HashMap<>();
		MapBusan.put("name", "인천");
		MapBusan.put("y", incheonRain);
        
        // list.add(MapSeoul);
		// list.add(MapBusan);
		// list.add(MapDeaegu);
		// list.add(MapIncheon);
        arr.add(MapSeoul);
        arr.add(MapBusan);
        arr.add(MapDeaegu);
        arr.add(MapIncheon);
        

        return arr;
    }

    public static int rainData(String time, int x, int y) {
		int result = 0;
		try {
			StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst"); /*URL*/
			
			String key ="YgihNGI8Qc0cmtagDlWAUerTD0s9N1Mpo2yXN1dcmJ2FUOX8lyY8EB%2BIP3nnZO1iy5f3C0me58vH79c6n1%2Buvw%3D%3D";
	        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + key); /*Service Key*/
	        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
	        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8")); /*한 페이지 결과 수*/
	        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("XML", "UTF-8")); /*요청자료형식(XML/JSON) Default: XML*/
	        urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode("20240823", "UTF-8")); /*‘21년 6월 28일 발표*/
	        urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(time, "UTF-8")); /*06시 발표(정시단위) */
	        urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode("55", "UTF-8")); /*예보지점의 X 좌표값*/
	        urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode("127", "UTF-8")); /*예보지점의 Y 좌표값*/
	        
	        System.out.println("url: "+urlBuilder);
	        URL url = new URL(urlBuilder.toString());
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("GET");
	        conn.setRequestProperty("Content-type", "application/json");
	        System.out.println("Response code: " + conn.getResponseCode());
	        BufferedReader rd;
	        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
	            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        } else {
	            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
	        }
	        StringBuilder sb = new StringBuilder();
	        
	        String line;
	        while ((line = rd.readLine()) != null) {
	            sb.append(line);
	        }
	        rd.close();
	        conn.disconnect();
	        System.out.println(sb.toString());
	        
	        // XML 파싱
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new java.io.ByteArrayInputStream(sb.toString().getBytes()));

            doc.getDocumentElement().normalize();

            // XML 데이터에서 원하는 태그를 가져옵니다. 예시로 "item" 태그를 가져옴.
            NodeList nList = doc.getElementsByTagName("item");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Element element = (Element) nList.item(temp);
                
                // 각 "item" 요소 내의 데이터 접근
                String category = element.getElementsByTagName("category").item(0).getTextContent();
                String obsrValue = element.getElementsByTagName("obsrValue").item(0).getTextContent();
                
                if(category.equals("RN1")) {
                	result = Integer.parseInt(obsrValue);
                }
                
                System.out.println("Category: " + category);
                System.out.println("Observation Value: " + obsrValue);
            }
	        
	        
	        
	        
	        
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

}
