package com.microel.speedtest.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MeasureConnectionTypes {
    ETHERNET, WIFI;

    @JsonValue
    public int toValue() {
        return ordinal();
    }
}
