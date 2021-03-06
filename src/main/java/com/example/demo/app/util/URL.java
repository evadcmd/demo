package com.example.demo.app.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Setter;

@Setter
@ConfigurationProperties(prefix = "aiserver")
@Component
public class URL {
    private String ip;
    private int port;
    private String contextpath;

    @Override
    public String toString() {
        return new StringBuilder("http://")
            .append(ip)
            .append(":")
            .append(port)
            .append("/")
            .append(contextpath)
            .toString();
    }
}