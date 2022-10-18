package com.microel.speedtest.common.models.chart;

import com.microel.speedtest.repositories.proxies.DOWIntegerPointProxy;
import com.microel.speedtest.repositories.proxies.DateIntegerPointProxy;
import com.microel.speedtest.repositories.proxies.HourIntegerPointProxy;

public class HourIntegerPoint extends ChartPoint<Integer, Integer> implements HourIntegerPointProxy {
    public HourIntegerPoint(Integer x, Integer y) {
        super(x, y);
    }
    public  static HourIntegerPoint fromProxy(DateIntegerPointProxy proxy){
        return new HourIntegerPoint(proxy.getX().toLocalDateTime().getHour(),proxy.getY());
    }
}
