package com.microel.speedtest.common.models.chart;

import com.microel.speedtest.common.enums.MeasureConnectionTypes;
import com.microel.speedtest.repositories.proxies.GroupDayCTypeIntegerPointProxy;
import com.microel.speedtest.repositories.proxies.GroupDayStringIntegerPointProxy;

import java.sql.Timestamp;

public class GroupDayStringIntegerPoint extends GroupedChartPoint<Timestamp, Integer, String> {
    public GroupDayStringIntegerPoint(Timestamp stamp, Integer value, String groupName){
        setX(stamp);
        setY(value);
        setG(groupName);
    }

    public static GroupDayStringIntegerPoint fromProxy(GroupDayStringIntegerPointProxy proxy){
        return new GroupDayStringIntegerPoint(proxy.getX(), proxy.getY(), proxy.getG());
    };
}
