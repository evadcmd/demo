package com.example.demo.app.controller;

import java.util.HashMap;
import java.util.Map;

import com.example.demo.app.entity.Task;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(TaskModeController.URL)
public class TaskModeController {
    public static final String URL = "/api/settings/taskmode";

    @GetMapping
    Map<String, String> get() {
        Map<String, String> res = new HashMap<>();
        for (Task.Mode mode : Task.Mode.values())
            res.put(mode.name(), mode.getLabel());
        return res;
    }
}