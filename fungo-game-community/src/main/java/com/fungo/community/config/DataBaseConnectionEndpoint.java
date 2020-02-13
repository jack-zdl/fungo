package com.fungo.community.config;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/5/6
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

//自定义检查数据库端点
//@Component
//@Endpoint(id = "dbcheck",enableByDefault = true)//是否在默认情况下启用端点
public class DataBaseConnectionEndpoint {

//    private static final String DRIVER ="com.mysql.dbc.Driver";
//    @Value("${spring.datasource.driver-class-name}")
//    private String driver ;
//    @Value("${spring.datasource.url}")
//    private String url  ;
//    @Value("{spring.datasource.username}")
//    private String username ;
//    @Value("{spring.datasource.password}")
//    private String password  ;


//    @ReadOperation
//    public Map<String , Object> test () {
//        Connection conn = null;
//        Map<String, Object> msgMap = new HashMap<>();
//        try {
//            Class.forName(driver);
//            conn = DriverManager.getConnection(url, username, password);
//            msgMap.put("success", true);
//            msgMap.put("message", " 测试数据库连接成功");
//        } catch (Exception ex) {
//            msgMap.put("success", false);
//            msgMap.put("message", ex.getMessage());
//        } finally {
//            if (conn != null) {
//                try {
//                    conn.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }
//        return msgMap;
//    }

    }
