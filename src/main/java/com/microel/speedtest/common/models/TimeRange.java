package com.microel.speedtest.common.models;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class TimeRange {
    private Timestamp start;
    private Timestamp end;

    public Boolean isEmpty(){
        return start == null || end == null;
    }
}
