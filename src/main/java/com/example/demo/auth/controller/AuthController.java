package com.example.demo.auth.controller;

import java.util.List;

import com.example.demo.auth.entity.Auth;
import com.example.demo.auth.repository.AuthRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("hasRole('ROOT') OR hasRole('ADMIN')")
@RestController
@RequestMapping(AuthController.URL)
public class AuthController {
    public static final String URL = "/api/authority";
    
    @Autowired
    private AuthRepository authRepository;

    @GetMapping
    public List<Auth> get() {
        return authRepository.findAllWithoutRoot();
    }
}