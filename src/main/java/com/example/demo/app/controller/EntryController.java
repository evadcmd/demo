package com.example.demo.app.controller;

import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import com.example.demo.auth.entity.Auth;
import com.example.demo.auth.entity.User;
import com.example.demo.auth.repository.AuthRepository;
import com.example.demo.auth.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(EntryController.URL)
public class EntryController {
    public static final String URL = "/*";

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthRepository authRepository;

    @RequestMapping(method = RequestMethod.GET)
    public String entry(Model model, HttpServletResponse resp, Authentication auth) {
        /*
        authRepository.save(new Auth("ROLE_ROOT"));
        User user = User.builder()
                        .username("root")
                        .displayname("root")
                        .password("root")
                        .authorities(Set.of(new Auth("ROLE_ROOT")))
                        .enabled(true)
                        .accountNonExpired(true)
                        .accountNonLocked(true)
                        .credentialsNonExpired(true)
                        .build();
        userRepository.save(user);
        */
        // model.addAttribute("isAuthenticated", (auth != null) && auth.isAuthenticated());
        return "index";
    }
}