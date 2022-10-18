package com.microel.speedtest.common.models.chart;

import com.microel.speedtest.repositories.proxies.TimestampLongPointProxy;

import java.sql.Timestamp;
import java.time.Instant;

public class TimeLongPoint extends ChartPoint<Timestamp,Long> implements TimestampLongPointProxy {
    public TimeLongPoint(Long y) {
        super(Timestamp.from(Instant.now()), y);
    }
}
