package com.example.demo.websocket.config;

import java.security.Principal;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WebSocketHandlerDecoratorFactoryImpl implements WebSocketHandlerDecoratorFactory {
    @Override
    public WebSocketHandler decorate(WebSocketHandler handler) {
        return new WebSocketHandlerDecorator(handler) {
            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                log.info("websocket connected. sessionID = {}", session.getId());
                Principal principal = session.getPrincipal();
                if (principal != null)
                    WebSocketManager.add(principal.getName(), session);
                super.afterConnectionEstablished(session);
            }
            @Override
            public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
                log.info("websocket disconnected. sessionID = {}", session.getId());
                Principal principal = session.getPrincipal();
                if (principal != null)
                    WebSocketManager.remove(principal.getName());
                super.afterConnectionClosed(session, closeStatus);
            }
        };
    }

}