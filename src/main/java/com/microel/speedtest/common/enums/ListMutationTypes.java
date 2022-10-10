package com.microel.speedtest.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ListMutationTypes {
    ADD,
    UPDATE,
    DELETE;

    @JsonValue
    public int toValue() {
        return ordinal();
    }
}
