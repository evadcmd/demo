package com.example.demo.app.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(TestController.URL)
public class TestController {
    public static final String URL = "/test";

    @RequestMapping(method = RequestMethod.GET)
    Map<String, String> get() {
        return Map.of("get", "success");
    }

    @RequestMapping(method = RequestMethod.POST)
    Map<String, String> post() {
        return Map.of("post", "success");
    }

    @RequestMapping(method = RequestMethod.PUT)
    Map<String, String> put() {
        return Map.of("put", "success");
    }

    @RequestMapping(method = RequestMethod.DELETE)
    Map<String, String> delete() {
        return Map.of("delete", "success");
    }
}