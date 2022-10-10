package com.microel.speedtest.services.websocket.interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

public class StandardSocketHandler {
    protected final Map<WebSocketSession, String> sessions = new HashMap<>();
    protected final HashSet<IConnectHandler> connectHandlers = new HashSet<>();
    protected final Map<WebSocketSession, IDisconnectHandler> disconnectHandlers = new HashMap<>();
    protected final List<String> unfinishedMessageBuffer = new ArrayList<>();

    protected String getDeviceId(WebSocketSession session) {
        return session.getUri().getPath().split("/")[2];
    }

    /**
     * Буферизует данные сообщений.
     * 
     * @param message Web socket сообщение.
     * @return Возвращает true, если последняя часть сообщения.
     */
    protected Boolean bufferingMessage(WebSocketMessage<?> message) {
        unfinishedMessageBuffer.add(message.getPayload().toString());
        return message.isLast();
    }

    protected String getBufferedMessage() {
        String message = unfinishedMessageBuffer.stream().reduce("", (a, b) -> a + b);
        unfinishedMessageBuffer.clear();
        return message;
    }

    protected WebSocketSession getSession(String deviceId) {
        Entry<WebSocketSession, String> entry = sessions.entrySet().stream().filter(e -> e.getValue().equals(deviceId))
                .findFirst().orElse(null);
        if (entry != null) {
            return entry.getKey();
        } else {
            return null;
        }
    }

    public void addConnectHandler(IConnectHandler handler) {
        connectHandlers.add(handler);
    }

    public void addDisconnectHandler(String deviceId, IDisconnectHandler handler) {
        WebSocketSession session = getSession(deviceId);
        if (session == null) {
            throw new IllegalArgumentException("Device with id " + deviceId + " not found");
        }
        disconnectHandlers.put(session, handler);
    }
}
