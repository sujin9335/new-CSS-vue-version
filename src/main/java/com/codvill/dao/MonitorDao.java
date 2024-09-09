package com.codvill.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.codvill.comm.Utils;

@Repository
public class MonitorDao {
    static JdbcTemplate jt;

	@Autowired
	@Resource(name = "dataSource")
	DataSource ds;

	String dbType = "";

	private boolean isLoaded;

	@PostConstruct
	public void init() {
		new Thread(this::loadDatasource).start();
	}

	// 처음 실행시 디비가 제대로 실행되지 않은상태라면
	// 아래 로직을 실행하다 전체 서비스가 중단된다.
	// 이를 막기위해 쓰레드로 동작하게처리함.
	private void loadDatasource() {

		while (!isLoaded) {
			try {
				jt = new JdbcTemplate(ds);
				// dbType = getDbType();
				isLoaded = true;
			} catch (Exception e) {
				isLoaded = false;
				try {
					Thread.sleep(1000L);
				} catch (Exception ignored) {
				}
			}
		}
	}

    public JSONObject monitorList() {
        JSONObject obj = new JSONObject();
        String sql = "select * from tbl_monitor order by monitor_date desc";

        List<Map<String, Object>> list = jt.queryForList(sql);

        obj.put("data", list);


        return obj;
    }

    public JSONObject monitorCoordinateUpdate(Map<String, Object> param) {
        JSONObject obj = new JSONObject();
        Map<String, Object> map = (Map<String, Object>) param.get("info");
        if (map == null || map.isEmpty()) {
            obj.put("msg", "빈값");
            return obj;
        }
        String x = Utils.nvl(map.get("x").toString(),"100");
        String y = Utils.nvl(map.get("y").toString(),"100");
        String width = Utils.nvl(map.get("width").toString(),"200");
        String height = Utils.nvl(map.get("height").toString(),"200");
        String id = Utils.nvl(map.get("id").toString(),"");


        String sql = "UPDATE tbl_monitor " +
                    "SET " +
                        "monitor_x = ?, " +
                        "monitor_y = ?, " +
                        "monitor_width = ?, " +
                        "monitor_hight = ?  " +
                    "WHERE " +
                        "monitor_id = ? " ;
        jt.update(sql, x, y, width, height, id);

        return obj;
    }

    public JSONObject monitorInsert(Map<String, Object> param) {
        JSONObject obj = new JSONObject();
        Map<String, Object> map = (Map<String, Object>) param.get("info");
        if (map == null || map.isEmpty()) {
            obj.put("msg", "빈값");
            return obj;
        }
        UUID uuid = UUID.randomUUID();

        String title = Utils.nvl(map.get("monitor_title").toString(),"-"); 
        String content = Utils.nvl(map.get("monitor_content").toString(),""); 


        String sql = "INSERT INTO tbl_monitor (monitor_id, monitor_content, monitor_title, monitor_x, monitor_y, monitor_width, monitor_hight, monitor_date) " +
                         "VALUES (?,?,?,?,?,?,?,now())";

        jt.update(sql, uuid.toString(), content, title, "100", "200", "300", "200");



        return obj;
    }

    public JSONObject monitorUpdate(Map<String, Object> param) {
        JSONObject obj = new JSONObject();
        Map<String, Object> map = (Map<String, Object>) param.get("info");
        if (map == null || map.isEmpty()) {
            obj.put("msg", "빈값");
            return obj;
        }
        String title = Utils.nvl(map.get("monitor_title").toString(),"-"); 
        String content = Utils.nvl(map.get("monitor_content").toString(),""); 
        String id = Utils.nvl(map.get("monitor_id").toString(),"");

        String sql = "UPDATE tbl_monitor " +
                    "SET " +
                        "monitor_content = ?, " +
                        "monitor_title = ?  " +
                    "WHERE " +
                        "monitor_id = ? ";
        jt.update(sql, content, title, id);

        return obj;
    }

    public JSONObject monitorDelete(Map<String, Object> param) {
        JSONObject obj = new JSONObject();
        List<String> list = null;
        list = (ArrayList<String>) param.get("info");
        if (list == null || list.isEmpty()) {
            obj.put("msg", "빈값");
            return obj;
            
        }

        for (String id : list) {
            String sql = "DELETE FROM tbl_monitor WHERE monitor_id = ? ";
            jt.update(sql, id);
        }

        return obj;
    }






    
}
