package com.microel.speedtest.common.models.chart;

import com.microel.speedtest.repositories.proxies.StringIntegerPointProxy;

public class StringIntegerPoint extends ChartPoint<String,Integer> implements StringIntegerPointProxy {
    public StringIntegerPoint(String x, Integer y) {
        super(x, y);
    }
    public static StringIntegerPoint fromProxy(StringIntegerPointProxy proxy){
        return new StringIntegerPoint(proxy.getX(),proxy.getY());
    }
}
