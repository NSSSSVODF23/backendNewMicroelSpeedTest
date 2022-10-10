package com.microel.speedtest.services.websocket.interfaces;

@FunctionalInterface
public interface IDisconnectHandler {
    void handle(String deviceId);
}
