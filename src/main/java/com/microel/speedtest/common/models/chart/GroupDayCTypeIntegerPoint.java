package com.microel.speedtest.common.models.chart;

import com.microel.speedtest.common.enums.MeasureConnectionTypes;
import com.microel.speedtest.repositories.proxies.GroupDayCTypeIntegerPointProxy;
import com.microel.speedtest.repositories.proxies.Proxy;

import java.sql.Timestamp;

public class GroupDayCTypeIntegerPoint extends GroupedChartPoint<Timestamp, Integer, MeasureConnectionTypes>{
    public GroupDayCTypeIntegerPoint(Timestamp stamp, Integer value, MeasureConnectionTypes connectionTypes){
        setX(stamp);
        setY(value);
        setG(connectionTypes);
    }

    public static GroupDayCTypeIntegerPoint fromProxy(GroupDayCTypeIntegerPointProxy proxy){
        return new GroupDayCTypeIntegerPoint(proxy.getX(), proxy.getY(), proxy.getG());
    };
}
