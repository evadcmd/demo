package com.example.demo.auth;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.demo.auth.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class AuthConfig {

    @Autowired
    private UserRepository userRepository;

    @Bean
    UserDetailsService userDetailsService() {
        return (username) -> {
            return userRepository.findById(username).orElseThrow(
                () -> { return new UsernameNotFoundException("username not found."); }
            );
        };
    }

    @Bean
    AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (HttpServletRequest req, HttpServletResponse resp, Authentication auth) -> {
            Resp.of(resp)
                .setStatus(200)
                .setContentType("application/json;charset=UTF-8")
                .writeResponseBody(Map.of("authorities", auth.getAuthorities()));
        };
    }

    @Bean
    AuthenticationFailureHandler authenticationFailureHandler() {
        return (HttpServletRequest rea, HttpServletResponse resp, AuthenticationException e) -> {
            Resp.of(resp)
                .setStatus(401)
                .setContentType("application/json;charset=UTF-8")
                .writeResponseBody(Map.of("authorities", e.getMessage()));
        };
    }

}