package com.example.demo;

import java.net.UnknownHostException;
import java.util.List;

import com.example.demo.app.util.IP;
import com.example.demo.auth.entity.Auth;
import com.example.demo.auth.entity.User;
import com.example.demo.auth.repository.AuthRepository;
import com.example.demo.auth.repository.UserRepository;
import com.example.demo.auth.util.RSAUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.Setter;

@Service
@ConfigurationProperties
public class Initializer implements CommandLineRunner {

    @Setter
    private List<Auth> authorities;

    @Setter
    private User root;

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private IP ip;

    @Override
    public void run(String... args) throws UnknownHostException {
        // init authorities
        if (authRepository.findAll().isEmpty())
            authRepository.saveAll(authorities);
        // init root user
        if (!userRepository.findByEmail(root.getEmail()).isPresent())
            User.builder()
                .email(root.getEmail())
                .password(passwordEncoder.encode(root.getPassword()))
                .username(root.getUsername())
                .auth(authorities.get(0))
                .build();
        // RSAUtils.generateAndShowKeyPair();
        // cache self URL
        ip.getSelfURL();
    }
}