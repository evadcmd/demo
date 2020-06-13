package com.example.demo.auth.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.demo.auth.Resp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "server.servlet.session")
public class CookieRefresher implements HandlerInterceptor {

    @Setter
    private int timeout;

    @Override
    public void postHandle(HttpServletRequest req, HttpServletResponse resp, Object handler, ModelAndView mv) {
        Resp.of(resp)
            .addCookie(new Cookie("isAuthenticated", "true"), timeout);
    }
}