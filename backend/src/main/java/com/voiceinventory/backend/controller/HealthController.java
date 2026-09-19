package com.voiceinventory.backend.controller;

import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HealthController {

    // JdbcTemplate lets us run plain SQL. Spring creates it and passes it
    // in through the constructor (this is called dependency injection).
    private final JdbcTemplate jdbcTemplate;

    public HealthController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of(
                "status", "UP",
                "app", "Voice Inventory Management"
        );
    }

    // Proves the backend can reach MySQL and read real data.
    @GetMapping("/health/db")
    public Map<String, Object> databaseHealth() {
        String databaseName = jdbcTemplate.queryForObject("SELECT DATABASE()", String.class);
        Integer productCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM products", Integer.class);
        return Map.of(
                "status", "UP",
                "database", databaseName,
                "productCount", productCount
        );
    }
}