package com.example.demo.app.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IP {
    public static final String valueOf(int ip) {
        int mask = 0xFF;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++, ip >>= 8) {
            if (i != 0)
                sb.append(".");
            sb.insert(0, ip & mask);
        }
        return sb.toString();
    }

    @Value("${server.port}")
    Integer port;
    @Value("${server.servlet.context-path}")
    String contextPath;

    String selfURL = null;

    public String getSelfURL() throws UnknownHostException {
        if (this.selfURL == null)
            this.selfURL = String.format(
                "http://%s:%d%s",
                InetAddress.getLocalHost().getHostAddress(),
                port,
                contextPath
            );
        return this.selfURL;
    }
}