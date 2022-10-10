package com.microel.speedtest.services.websocket.transport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microel.speedtest.common.enums.MeasureActionTypes;
import com.microel.speedtest.common.enums.MeasureConnectionTypes;
import com.microel.speedtest.repositories.entities.Device;
import com.microel.speedtest.repositories.entities.Measure;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.socket.AbstractWebSocketMessage;

@Getter
@Setter
@NoArgsConstructor
public class MeasureActionMessage {
    private MeasureActionTypes type;
    private Device deviceInfo;
    private MeasureConnectionTypes connectionType;
    private Measure result;
    private Long resultId;
    private Boolean isPro;

    public MeasureActionMessage(MeasureActionTypes type){
        this.type = type;
    }
    public MeasureActionMessage(MeasureActionTypes type, Boolean isPro){this.type = type; this.isPro = isPro;}

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        MeasureActionMessage that = (MeasureActionMessage) o;

        return type.equals(that.type) && deviceInfo.equals(that.deviceInfo);
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + deviceInfo.hashCode();
        return result;
    }
}
