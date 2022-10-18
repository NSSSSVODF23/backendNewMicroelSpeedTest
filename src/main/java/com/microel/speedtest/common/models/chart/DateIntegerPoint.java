package com.microel.speedtest.common.models.chart;

import com.microel.speedtest.repositories.proxies.DateIntegerPointProxy;
import com.microel.speedtest.repositories.proxies.Proxy;

import java.sql.Timestamp;
import java.time.LocalDate;

public class DateIntegerPoint extends ChartPoint<Timestamp,Integer> implements DateIntegerPointProxy {
    public DateIntegerPoint(Integer y) {
        super(Timestamp.valueOf(LocalDate.now().toString()), y);
    }
    public DateIntegerPoint(Timestamp x, Integer y){
        super(x, y);
    }

    public static DateIntegerPoint fromProxy(DateIntegerPointProxy proxy){
        return new DateIntegerPoint(proxy.getX(),proxy.getY());
    }
}
