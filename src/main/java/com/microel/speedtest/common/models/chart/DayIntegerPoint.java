package com.microel.speedtest.common.models.chart;

import com.microel.speedtest.repositories.proxies.DayIntegerPointProxy;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DayIntegerPoint extends ChartPoint<Timestamp,Integer> implements DayIntegerPointProxy {
    public DayIntegerPoint(Integer y) {
        super(Timestamp.valueOf(LocalDate.now().toString()), y);
    }
    public DayIntegerPoint(Timestamp x, Integer y){
        super(x, y);
    }
}
