package com.example.demo.app.controller;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.demo.app.entity.DetectionLog;
import com.example.demo.app.repository.DetectionLogRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(DetectionLogController.URL)
public class DetectionLogController {
    public static final String URL = "/api/detection-log";
    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Autowired
    private DetectionLogRepository logRepository;

    @GetMapping
    public List<DetectionLog> get(
        @DateTimeFormat(pattern = DATE_TIME_PATTERN) Date from,
        @DateTimeFormat(pattern = DATE_TIME_PATTERN) Date to) {
            return logRepository.findAllWithoutImage(from, to);
    }

    @GetMapping
    @RequestMapping("/{id}")
    public Map<String, String> getImageBase64(@PathVariable Integer id) {
        Optional<DetectionLog> log = logRepository.findById(id);
        return Map.of(
            "data", log.isPresent() ? Base64.getEncoder().encodeToString(log.get().getImg()) : null
        );
    }
}