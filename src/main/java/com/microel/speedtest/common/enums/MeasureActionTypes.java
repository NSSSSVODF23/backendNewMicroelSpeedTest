package com.microel.speedtest.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MeasureActionTypes {
    DEVICE_INFO,
    START,
    END,
    ERROR,
    RESULT,
    ABORT,
    GET_DEVICE_INFO,
    TESTING_MODE;

    @JsonValue
    public int toValue() {
        return ordinal();
    }
}
