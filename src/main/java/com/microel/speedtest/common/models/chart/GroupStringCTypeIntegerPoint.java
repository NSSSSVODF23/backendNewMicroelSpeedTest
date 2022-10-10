package com.microel.speedtest.common.models.chart;

import com.microel.speedtest.common.enums.MeasureConnectionTypes;
import com.microel.speedtest.repositories.proxies.GroupDayCTypeIntegerPointProxy;
import com.microel.speedtest.repositories.proxies.GroupStringCTypeIntegerPointProxy;

import java.sql.Timestamp;

public class GroupStringCTypeIntegerPoint extends GroupedChartPoint<String,Integer,MeasureConnectionTypes>{
    public GroupStringCTypeIntegerPoint(String name, Integer value, MeasureConnectionTypes connectionTypes){
        setX(name);
        setY(value);
        setG(connectionTypes);
    }

    public static GroupStringCTypeIntegerPoint fromProxy(GroupStringCTypeIntegerPointProxy proxy){
        return new GroupStringCTypeIntegerPoint(proxy.getX(), proxy.getY(), proxy.getG());
    };
}
