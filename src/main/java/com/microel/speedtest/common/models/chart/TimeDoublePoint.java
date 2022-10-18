package com.microel.speedtest.common.models.chart;

import com.microel.speedtest.repositories.proxies.TimestampDoublePointProxy;

import java.sql.Timestamp;
import java.time.Instant;

public class TimeDoublePoint extends ChartPoint<Timestamp,Double> implements TimestampDoublePointProxy {
    public TimeDoublePoint(Double y) {
        super(Timestamp.from(Instant.now()), y);
    }
}
