package com.microel.speedtest.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DeviceTypes {
    DESKTOP, MOBILE;

    @JsonValue
    public int toValue() {
        return ordinal();
    }
}
