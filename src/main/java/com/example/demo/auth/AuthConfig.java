package com.example.demo.auth;

import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.demo.auth.entity.User;
import com.example.demo.auth.entity.Auth;
import com.example.demo.auth.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "server.servlet.session")
public class AuthConfig {

    @Getter
    @Setter
    private int timeout;

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
            User user = (User) auth.getPrincipal();
            String authorities = user.getAuthorities().stream().map(Auth::getAuthority).collect(Collectors.joining(","));
            Resp.of(resp)
                .setStatus(200)
                .addCookie(new Cookie("isAuthenticated", "true"), timeout)
                .addCookie(new Cookie("displayname", user.getDisplayname()), timeout)
                .addCookie(new Cookie("authorities", authorities), timeout)
                .setContentType("application/json;charset=UTF-8")
                .writeResponseBody(Map.of("authentication", true));
        };
    }

    @Bean
    AuthenticationFailureHandler authenticationFailureHandler() {
        return (HttpServletRequest req, HttpServletResponse resp, AuthenticationException e) -> {
            Resp.of(resp)
                .setStatus(401)
                .addCookie(new Cookie("isAuthenticated", "false"), 0)
                .addCookie(new Cookie("displayname", ""), 0)
                .addCookie(new Cookie("authorities", ""), 0)
                .setContentType("application/json;charset=UTF-8")
                .writeResponseBody(
                    Map.of(
                        "authentication", false,
                        "authorities", e.getMessage()
                    )
                );
        };
    }

    @Bean
    LogoutSuccessHandler logouSuccessHandler() {
        return (HttpServletRequest req, HttpServletResponse resp, Authentication auth) -> {
            Resp.of(resp)
                .setStatus(200)
                .setContentType("application/json;charset=UTF-8")
                .writeResponseBody(Map.of("authentication", false));
        };
    }

}