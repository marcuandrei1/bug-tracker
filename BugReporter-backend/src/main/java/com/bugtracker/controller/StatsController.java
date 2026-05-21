package com.bugtracker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/stats")
public class StatsController {

    private final RestTemplate restTemplate;
    private static final String STATS_SERVICE_URL = "http://localhost:8081/api/stats";

    public StatsController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public Object getStats() {
        return restTemplate.getForObject(STATS_SERVICE_URL, Object.class);
    }
}
