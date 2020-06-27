package com.example.demo.websocket.config;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.web.socket.WebSocketSession;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebSocketManager {
    private static ConcurrentHashMap<String, WebSocketSession> map = new ConcurrentHashMap<>();

    public static void add(String key, WebSocketSession webSocketSession) {
        log.info("add new websocket connection: {}", key);
        map.put(key, webSocketSession);
    }

    public static void remove(String key) {
        log.info("remove websocket connection: {}", key);
        map.remove(key);
    }

    public static WebSocketSession get(String key) {
        log.info("get websocket connection: {}", key);
        return map.get(key);
    }

    private WebSocketManager() {}
}