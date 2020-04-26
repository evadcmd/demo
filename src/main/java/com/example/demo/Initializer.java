package com.example.demo;

import java.util.List;
import java.util.Set;

import com.example.demo.auth.entity.Auth;
import com.example.demo.auth.entity.User;
import com.example.demo.auth.repository.AuthRepository;
import com.example.demo.auth.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
public class Initializer implements CommandLineRunner {

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Override
    public void run(String... args) {
        // default authorities
        Auth admin = Auth.of(Auth.Role.ROLE_ADMIN);
        Auth manager = Auth.of(Auth.Role.ROLE_MANAGER);
        Auth user = Auth.of(Auth.Role.ROLE_USER);

        authRepository.saveAll(List.of(admin, manager, user));

        if (!userRepository.findByEmail("evadcmd@gmail.com").isPresent()) {
            userRepository.save(
                User.builder()
                    .username("root")
                    .password("root")
                    .email("evadcmd@gmail.com")
                    .authorities(Set.of(admin))
                    .enabled(true)
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .build()
            );
        }
    }
}