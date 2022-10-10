package com.microel.speedtest.services.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.microel.speedtest.services.websocket.handler.MeasureHandler;
import com.microel.speedtest.services.websocket.handler.PingHandler;
import com.microel.speedtest.services.websocket.handler.UploadResolverHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    final
    PingHandler pingHandler;
    final
    UploadResolverHandler uploadResolverHandler;
    final
    MeasureHandler measureSession;

    public WebSocketConfiguration(PingHandler pingHandler, UploadResolverHandler uploadResolverHandler, MeasureHandler measureSession) {
        this.pingHandler = pingHandler;
        this.uploadResolverHandler = uploadResolverHandler;
        this.measureSession = measureSession;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(pingHandler, "/ping/{deviceId}").addHandler(uploadResolverHandler, "/upload/{deviceId}")
                .addHandler(measureSession, "/measure/{deviceId}")
                .setAllowedOrigins("*");
    }

}