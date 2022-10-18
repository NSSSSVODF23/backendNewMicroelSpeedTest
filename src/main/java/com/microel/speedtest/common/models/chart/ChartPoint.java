package com.microel.speedtest.common.models.chart;

import java.sql.Timestamp;
import java.time.Instant;

import com.microel.speedtest.repositories.proxies.Proxy;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ChartPoint<M,T> {
    private M x;
    private T y;

    public ChartPoint(M x, T y) {
        this.x = x;
        this.y = y;
    }
}
