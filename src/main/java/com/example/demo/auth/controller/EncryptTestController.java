package com.example.demo.auth.controller;

import java.util.Map;

import com.example.demo.auth.util.RSA;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(EncryptTestController.URL)
public class EncryptTestController {
    public static final String URL = "/api/encrypt";

    @Autowired private RSA rsa;

    @Getter
    @Setter
    private static class Req {
        private String cipherText;
    }

    @PostMapping
    public Map<String, Boolean> decrypt(@RequestBody Req req) {
        log.info("Decrypt data = {}", rsa.decrypt(req.cipherText));
        return Map.of("decrypt", true);
    }
    
}