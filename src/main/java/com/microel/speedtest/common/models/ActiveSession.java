package com.microel.speedtest.common.models;

import com.microel.speedtest.repositories.entities.Measure;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ActiveSession {
    private UUID id = UUID.randomUUID();
    private String deviceId = "";
    private Boolean isMobile = false;
    private String login = "";
    private String address = "";
    private Boolean isStarted = false;
    private Boolean hasInfo = false;

    public ActiveSession(Measure measure, WebSocketSession webSocketSession) {
        id = UUID.fromString(webSocketSession.getId());
        if (measure.hasDeviceInfo()) {
            deviceId = measure.getDevice().getDeviceId();
            isMobile = measure.getDevice().getIsMobile();
        }
        if (measure.hasSession()) {
            login = measure.getSession().getLogin();
            address = measure.getSession().getHouse().getAddress();
        }
        isStarted = measure.getIsStarted();
    }
}
