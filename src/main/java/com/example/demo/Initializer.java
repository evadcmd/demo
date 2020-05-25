package com.example.demo;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.demo.auth.entity.Auth;
import com.example.demo.auth.entity.User;
import com.example.demo.auth.repository.AuthRepository;
import com.example.demo.auth.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;

@Service
@PropertySource(value = "classpath:config.properties")
@ConfigurationProperties
public class Initializer implements CommandLineRunner {

    @Getter
    @Setter
    private List<String> authorities;

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Override
    public void run(String... args) {

        authRepository.saveAll(
            authorities.stream().map(Auth::of).collect(Collectors.toList())
        );

        if (!userRepository.findByEmail("evadcmd@gmail.com").isPresent()) {
            userRepository.save(
                User.builder()
                    .username("root")
                    .password("root")
                    .email("evadcmd@gmail.com")
                    .authorities(Set.of(Auth.of(authorities.get(0))))
                    .enabled(true)
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .build()
            );
        }
    }
}