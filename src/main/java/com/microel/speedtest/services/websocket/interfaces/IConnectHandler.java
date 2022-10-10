package com.microel.speedtest.services.websocket.interfaces;

@FunctionalInterface
public interface IConnectHandler {
    void handle(String deviceId);
}
