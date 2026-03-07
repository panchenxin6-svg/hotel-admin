package com.pcx.hotel_admin.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class AutoDbInitializer implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        // 创建 stay 表
        String createStay = "CREATE TABLE IF NOT EXISTS stay (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "room_id BIGINT NOT NULL, " +
                "channel_id BIGINT, " +
                "booking_id BIGINT, " +
                "status TINYINT DEFAULT 1, " +
                "check_in_at DATETIME, " +
                "expected_check_out_at DATETIME, " +
                "check_out_at DATETIME, " +
                "remark VARCHAR(255), " +
                "active TINYINT DEFAULT 1, " +
                "create_time DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
                
        // 创建 stay_guest 表
        String createStayGuest = "CREATE TABLE IF NOT EXISTS stay_guest (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "stay_id BIGINT NOT NULL, " +
                "name VARCHAR(50) NOT NULL, " +
                "id_card VARCHAR(20), " +
                "phone VARCHAR(20), " +
                "gender TINYINT DEFAULT 0, " +
                "is_main TINYINT DEFAULT 0, " +
                "active TINYINT DEFAULT 1, " +
                "create_time DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                "update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
        
        try {
            jdbcTemplate.execute(createStay);
            System.out.println("[AutoDbInitializer] stay 表创建成功或已存在");
        } catch (Exception e) {
            System.err.println("[AutoDbInitializer] stay 表创建失败: " + e.getMessage());
        }
        
        try {
            jdbcTemplate.execute(createStayGuest);
            System.out.println("[AutoDbInitializer] stay_guest 表创建成功或已存在");
        } catch (Exception e) {
            System.err.println("[AutoDbInitializer] stay_guest 表创建失败: " + e.getMessage());
        }
        
        System.out.println("[AutoDbInitializer] 数据库初始化完成");
    }
}
