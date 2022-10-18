package com.microel.speedtest.repositories.proxies;

import java.sql.Timestamp;

public interface TimestampDoublePointProxy extends Proxy {
    Timestamp getX();
    Double getY();
}
