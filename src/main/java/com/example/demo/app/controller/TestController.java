package com.example.demo.app.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(TestController.URL)
public class TestController {
    public static final String URL = "/api/test";

    @GetMapping
    Map<String, String> get() {
        return Map.of("get", "success");
    }

    @PostMapping
    Map<String, String> post() {
        return Map.of("post", "success");
    }

    @PutMapping
    Map<String, String> put() {
        return Map.of("put", "success");
    }

    @DeleteMapping
    Map<String, String> delete() {
        return Map.of("delete", "success");
    }
}