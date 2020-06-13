package com.example.demo.auth;

import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.demo.auth.entity.User;
import com.example.demo.auth.entity.Auth;
import com.example.demo.auth.repository.UserRepository;
import com.example.demo.auth.util.AuthWeight;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "server.servlet.session")
public class AuthConfig {
    private static final String APPLICATION_JSON = "application/json;charset=UTF-8";

    @Setter
    private int timeout;

    @Autowired
    private AuthWeight authWeight;

    @Autowired
    private UserRepository userRepository;

    @Bean
    protected AntPathRequestMatcher antPathRequestMatcher() {
        return new AntPathRequestMatcher("/api/*");
    }

    @Bean
    protected BCryptPasswordEncoder bcryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService() {
        return (email) -> (
            userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(String.format("email: %s not found.", email))
            )
        );
    }

    @Bean
    AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (HttpServletRequest req, HttpServletResponse resp, Authentication auth) -> {
            User user = (User) auth.getPrincipal();
            Integer weight = user.getAuthorities().stream().mapToInt(authWeight::get).sum();
            Resp.of(resp)
                .setStatus(200)
                .addCookie(new Cookie("isAuthenticated", "true"), timeout)
                .addCookie(new Cookie("username", user.getUsername()), timeout)
                .addCookie(new Cookie("authorities", weight.toString()), timeout)
                .setContentType(APPLICATION_JSON)
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
                .setContentType(APPLICATION_JSON)
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
                .setContentType(APPLICATION_JSON)
                .writeResponseBody(Map.of("authentication", false));
        };
    }

}