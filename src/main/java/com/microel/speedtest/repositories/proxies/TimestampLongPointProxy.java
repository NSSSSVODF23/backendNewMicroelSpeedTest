package com.microel.speedtest.repositories.proxies;

import java.sql.Timestamp;

public interface TimestampLongPointProxy extends Proxy {
    Timestamp getX();
    Long getY();
}
