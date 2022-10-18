package com.microel.speedtest.common.models.chart;

import com.microel.speedtest.common.Util;
import com.microel.speedtest.repositories.proxies.DOWIntegerPointProxy;

public class DOWIntegerPoint extends ChartPoint<String,Integer>{
    public DOWIntegerPoint(String x, Integer y) {
        super(x, y);
    }

    public  static DOWIntegerPoint fromProxy(DOWIntegerPointProxy proxy){
        return new DOWIntegerPoint(Util.toDayOfWeek(proxy.getX()),proxy.getY());
    }
}
