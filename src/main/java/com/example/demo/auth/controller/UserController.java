package com.example.demo.auth.controller;

import java.util.List;

import com.example.demo.auth.entity.User;
import com.example.demo.auth.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("hasRole('ROOT') OR hasRole('ADMIN')")
@Transactional
@RestController
@RequestMapping(UserController.URL)
public class UserController {
    public static final String URL = "/api/user";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping
    List<User> get() {
        return userRepository.findAllWithoutRoot();
    }

    @PostMapping
    List<User> post(@RequestBody User user) {
        User prev = userRepository.findById(user.getId()).orElseThrow();
        user.setPassword(prev.getPassword());
        userRepository.save(user);
        return userRepository.findAllWithoutRoot();
    }

    @PutMapping
    List<User> put(@RequestBody User user) {
        user.setPassword(
            passwordEncoder.encode(user.getPassword())
        );
        userRepository.save(user);
        return userRepository.findAllWithoutRoot();
    }

    @DeleteMapping
    List<User> delete(@RequestParam Integer id) {
        userRepository.deleteById(id);
        return userRepository.findAllWithoutRoot();
    }
}