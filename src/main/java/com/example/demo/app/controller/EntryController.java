package com.example.demo.app.controller;

import com.example.demo.auth.util.RSA;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({
    EntryController.ROOT,
    EntryController.USER_URL,
    EntryController.SETTINGS_URL,
    EntryController.LOG_URL
})
public class EntryController {
    public static final String ROOT = "/*";
    public static final String USER_URL = "/user/*";
    public static final String SETTINGS_URL = "/settings/*";
    public static final String LOG_URL = "/detection-log/*";

    @Autowired RSA rsa;

    @GetMapping
    public String entry(Model model) {
        model.addAttribute("rsaPublicKey", rsa.getBase64PublicKey());
        return "index";
    }
}