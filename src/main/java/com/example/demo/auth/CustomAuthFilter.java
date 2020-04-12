package com.example.demo.auth;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.demo.auth.dto.UserPrincipal;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class CustomAuthFilter extends UsernamePasswordAuthenticationFilter {
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse resp) {
        if (req.getHeader("Content-Type").contains("application/json")) {
            try (BufferedReader reader = req.getReader()) {
                ObjectMapper mapper = new ObjectMapper();
                UserPrincipal user = mapper.readValue(reader, UserPrincipal.class);
                
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
                setDetails(req, token);

                return this.getAuthenticationManager().authenticate(token);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return super.attemptAuthentication(req, resp);
    }

}