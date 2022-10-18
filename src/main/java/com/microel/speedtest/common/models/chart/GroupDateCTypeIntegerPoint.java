package com.microel.speedtest.common.models.chart;

import com.microel.speedtest.common.enums.MeasureConnectionTypes;
import com.microel.speedtest.repositories.proxies.GroupDateCTypeIntegerPointProxy;
import com.microel.speedtest.repositories.proxies.GroupDayCTypeIntegerPointProxy;

import java.sql.Timestamp;

public class GroupDateCTypeIntegerPoint extends GroupedChartPoint<Timestamp, Integer, MeasureConnectionTypes>{
    public GroupDateCTypeIntegerPoint(Timestamp stamp, Integer value, MeasureConnectionTypes connectionTypes){
        setX(stamp);
        setY(value);
        setG(connectionTypes);
    }

    public static GroupDateCTypeIntegerPoint fromProxy(GroupDateCTypeIntegerPointProxy proxy){
        return new GroupDateCTypeIntegerPoint(proxy.getX(), proxy.getY(), proxy.getG());
    };
}
