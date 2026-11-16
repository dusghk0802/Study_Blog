package com.example.demo;

import java.util.HashMap;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RestDbController {

    private final JdbcTemplate jdbcTemplate;

    @GetMapping("/db")
    public String db() {
        return "dbTest";
    }

    @GetMapping("/db-test")
    public Map<String, Object> dbTest() {
        Map<String, Object> result = new HashMap<>();

        try {
            String version = jdbcTemplate.queryForObject(
                "SELECT VERSION()", String.class);
            String database = jdbcTemplate.queryForObject(
                "SELECT DATABASE()", String.class);

            result.put("status", "success");
            result.put("message", "MySQL 연결 성공");
            result.put("database", database);
            result.put("mysqlVersion", version);
        } catch (Exception e) {
            result.put("status", "FAIL");
            result.put("message", "MySQL 연결 실패");
            result.put("error", e.getMessage());
        }

        return result;
    }
}