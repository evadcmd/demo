package com.example.demo.app.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(EntryController.URL)
public class EntryController {
    public static final String URL = "/*";

    @RequestMapping(method = RequestMethod.GET)
    public String entry(Model model, HttpServletResponse resp, Authentication auth) {
        // model.addAttribute("isAuthenticated", (auth != null) && auth.isAuthenticated());
        return "index";
    }
}