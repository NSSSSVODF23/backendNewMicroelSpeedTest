package com.microel.speedtest.common.models.chart;

import java.sql.Timestamp;
import java.time.Instant;

public class TimeLongPoint extends ChartPoint<Timestamp,Long> {
    public TimeLongPoint(Long y) {
        super(Timestamp.from(Instant.now()), y);
    }
}
