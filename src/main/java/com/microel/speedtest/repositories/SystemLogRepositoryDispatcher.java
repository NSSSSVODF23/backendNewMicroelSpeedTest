package com.microel.speedtest.repositories;

import com.microel.speedtest.repositories.entities.SystemLog;
import com.microel.speedtest.repositories.interfaces.SystemLogRepository;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;

@Component
public class SystemLogRepositoryDispatcher {
    private final SystemLogRepository systemLogRepository;

    public SystemLogRepositoryDispatcher(SystemLogRepository systemLogRepository) {
        this.systemLogRepository = systemLogRepository;
    }

    public Boolean isClear(){
        return this.systemLogRepository.count() == 0L;
    }

    public SystemLog insertRunningLog(){
        SystemLog log = new SystemLog();
        log.setStamp(Timestamp.from(Instant.now()));
        log.setDescription("Server run");
        return this.systemLogRepository.save(log);
    }
}
