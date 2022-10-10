package com.microel.speedtest.services.acpparser.interfaces;

import com.microel.speedtest.repositories.entities.AcpSession;

@FunctionalInterface
public interface MeasureSessionCallback {
    void handle(AcpSession session);
}
