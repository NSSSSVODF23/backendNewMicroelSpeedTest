package com.microel.speedtest.repositories.proxies;

import com.microel.speedtest.common.enums.MeasureConnectionTypes;

import java.sql.Timestamp;

public interface GroupDayCTypeIntegerPointProxy extends Proxy{
    Integer getX();
    Integer getY();
    MeasureConnectionTypes getG();
}
