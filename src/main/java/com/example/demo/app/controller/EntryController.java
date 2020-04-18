package com.example.demo.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(EntryController.URL)
public class EntryController {
    public static final String URL = "/*";

    @RequestMapping(method = RequestMethod.GET)
    public String entry() {
        return "index";
    }
}