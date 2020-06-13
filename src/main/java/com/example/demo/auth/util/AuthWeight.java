package com.example.demo.auth.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.demo.auth.entity.Auth;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Setter;

// cached authority weight map
@Component
@ConfigurationProperties
public class AuthWeight {
    @Setter List<Auth> auths;

    private Map<Auth, Integer> map;

    public void init() {
        this.map = new HashMap<>();
        for (Auth auth : auths)
            this.map.put(auth, auth.getWeight());
    }

    public Integer get(Auth auth) {
        return this.map.get(auth);
    }

}