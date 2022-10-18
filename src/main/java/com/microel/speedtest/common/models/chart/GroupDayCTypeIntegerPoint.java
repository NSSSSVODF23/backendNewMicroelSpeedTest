package com.microel.speedtest.common.models.chart;

import com.microel.speedtest.common.Util;
import com.microel.speedtest.common.enums.MeasureConnectionTypes;
import com.microel.speedtest.repositories.proxies.GroupDayCTypeIntegerPointProxy;

public class GroupDayCTypeIntegerPoint extends GroupedChartPoint<String, Integer, MeasureConnectionTypes> {
    public GroupDayCTypeIntegerPoint(String weekDay, Integer value, MeasureConnectionTypes connectionTypes){
        setX(weekDay);
        setY(value);
        setG(connectionTypes);
    }

    public static GroupDayCTypeIntegerPoint fromProxy(GroupDayCTypeIntegerPointProxy proxy){
        return new GroupDayCTypeIntegerPoint(Util.toDayOfWeek(proxy.getX()), proxy.getY(), proxy.getG());
    };
}
