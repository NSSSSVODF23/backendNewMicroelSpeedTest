package com.microel.speedtest.repositories.proxies;

import com.microel.speedtest.common.enums.MeasureConnectionTypes;

public interface GroupHourCTypeIntegerPointProxy extends Proxy {
    Integer getX();
    Integer getY();
    MeasureConnectionTypes getG();
}
