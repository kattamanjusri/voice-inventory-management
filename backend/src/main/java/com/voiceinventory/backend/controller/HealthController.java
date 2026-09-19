package com.voiceinventory.backend.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// @RestController tells Spring: "this class handles web requests and
// returns data (JSON) directly, not web pages."
@RestController
// @RequestMapping sets a common URL prefix for every method in this class.
@RequestMapping("/api")
public class HealthController {

    // @GetMapping means: run this method when the browser sends a GET
    // request to /api/health.
    @GetMapping("/health")
    public Map<String, String> health() {
        // Spring automatically converts this Map into JSON.
        return Map.of(
                "status", "UP",
                "app", "Voice Inventory Management"
        );
    }
}