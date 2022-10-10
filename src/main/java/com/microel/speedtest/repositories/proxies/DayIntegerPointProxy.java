package com.microel.speedtest.repositories.proxies;

import java.sql.Timestamp;

public interface DayIntegerPointProxy extends Proxy {
    Timestamp getX();
    Integer getY();
}
