package com.example.demo.auth;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Resp {

    HttpServletResponse resp;

    public static final Resp of(HttpServletResponse resp) {
        return new Resp(resp);
    }

    private Resp(HttpServletResponse resp) {
        this.resp = resp;
    }

    public Resp setStatus(int sc) {
        resp.setStatus(sc);
        return this;
    }

    public Resp addCookie(Cookie cookie, int maxAge) {
        cookie.setMaxAge(maxAge * 60);
        resp.addCookie(cookie);
        return this;
    }

    public Resp setContentType(String type) {
        resp.setContentType(type);
        return this;
    }

    public Resp setHeader(String name, String value) {
        resp.setHeader(name, value);
        return this;
    }

    public void writeResponseBody(Object data) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        PrintWriter writer = resp.getWriter();
        writer.println(mapper.writeValueAsString(data));
        writer.flush();
        writer.close();
    }
}