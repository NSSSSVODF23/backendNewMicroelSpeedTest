package com.microel.speedtest.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microel.speedtest.repositories.entities.AcpSession;
import com.microel.speedtest.repositories.interfaces.SessionRepository;

@Component
public class SessionRepositoryDispatcher {
    private final SessionRepository sessionRepository;

    private final HouseRepositoryDispatcher houseRepositoryDispatcher;

    public SessionRepositoryDispatcher(SessionRepository sessionRepository, HouseRepositoryDispatcher houseRepositoryDispatcher) {
        this.sessionRepository = sessionRepository;
        this.houseRepositoryDispatcher = houseRepositoryDispatcher;
    }

    public Long getSessionId(AcpSession session) {
        AcpSession foundSession = sessionRepository.findByMacAndLoginAndIp(session.getMac(), session.getLogin(),
                session.getIp());
        if (foundSession != null) {
            return foundSession.getSessionId();
        }
        return null;
    }

    public AcpSession save(AcpSession session) {
        try {
            session.setSessionId(getSessionId(session));
            session.setHouse(houseRepositoryDispatcher.save(session.getHouse()));
            AcpSession savedSession = sessionRepository.save(session);
            return savedSession;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
