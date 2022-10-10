package com.microel.speedtest.repositories.proxies;

import com.microel.speedtest.common.enums.MeasureConnectionTypes;

public interface GroupStringCTypeIntegerPointProxy extends Proxy {
    String getX();
    Integer getY();
    MeasureConnectionTypes getG();
}
