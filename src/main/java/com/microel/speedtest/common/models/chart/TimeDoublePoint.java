package com.microel.speedtest.common.models.chart;

import java.sql.Timestamp;
import java.time.Instant;

public class TimeDoublePoint extends ChartPoint<Timestamp,Double> {
    public TimeDoublePoint(Double y) {
        super(Timestamp.from(Instant.now()), y);
    }
}
