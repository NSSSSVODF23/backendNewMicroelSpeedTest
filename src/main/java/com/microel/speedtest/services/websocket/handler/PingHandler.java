package com.microel.speedtest.services.websocket.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.microel.speedtest.services.websocket.interfaces.IDisconnectHandler;
import com.microel.speedtest.services.websocket.interfaces.StandardSocketHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PingHandler extends StandardSocketHandler implements WebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("Connection established: {} {}", session.getId(), getDeviceId(session));
        sessions.put(session, getDeviceId(session));
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {

        session.sendMessage(message);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        // Remove session from map and emit disconnect event
        sessions.remove(session);
        IDisconnectHandler handler = disconnectHandlers.remove(session);
        if (handler != null) {
            handler.handle(getDeviceId(session));
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

}
