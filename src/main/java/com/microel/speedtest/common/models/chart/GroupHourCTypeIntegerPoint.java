package com.microel.speedtest.common.models.chart;

import com.microel.speedtest.common.enums.MeasureConnectionTypes;
import com.microel.speedtest.repositories.proxies.GroupDateCTypeIntegerPointProxy;

public class GroupHourCTypeIntegerPoint extends GroupedChartPoint<Integer, Integer, MeasureConnectionTypes>{
    public GroupHourCTypeIntegerPoint(Integer stamp, Integer value, MeasureConnectionTypes connectionTypes){
        setX(stamp);
        setY(value);
        setG(connectionTypes);
    }

    public static GroupHourCTypeIntegerPoint fromProxy(GroupDateCTypeIntegerPointProxy proxy){
        return new GroupHourCTypeIntegerPoint(proxy.getX().toLocalDateTime().getHour(), proxy.getY(), proxy.getG());
    };
}
