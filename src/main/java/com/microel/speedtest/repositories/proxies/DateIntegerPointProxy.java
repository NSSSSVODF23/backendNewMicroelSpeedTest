package com.microel.speedtest.repositories.proxies;

import java.sql.Timestamp;

public interface DateIntegerPointProxy extends Proxy {
    Timestamp getX();
    Integer getY();
}
