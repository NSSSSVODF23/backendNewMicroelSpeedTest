package com.microel.speedtest.services.websocket.handler;

import java.io.IOException;
import java.time.Instant;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.microel.speedtest.controllers.uploadtest.types.UploadParticle;
import com.microel.speedtest.services.websocket.interfaces.IDisconnectHandler;
import com.microel.speedtest.services.websocket.interfaces.StandardSocketHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UploadResolverHandler extends StandardSocketHandler implements WebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.put(session, getDeviceId(session));
        connectHandlers.forEach(handler -> {
            handler.handle(getDeviceId(session));
        });
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
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

    public void sendSpeedResponse(String deviceId, Long uploadingBytes, Instant initial, Integer index) {
        WebSocketSession session = getSession(deviceId);
        if (session == null)
            throw new IllegalStateException("No session found for deviceId: " + deviceId);
        UploadParticle particle = new UploadParticle();
        particle.setB(uploadingBytes);
        particle.setI(index);
        particle.setE(Instant.now().toEpochMilli() - initial.toEpochMilli());
        try {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(particle.toString()));
            }
        } catch (IOException e) {
            log.error("Error sending speed response");
        }
    }

}
