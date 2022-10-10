package com.microel.speedtest.repositories.interfaces;

import com.microel.speedtest.repositories.entities.SystemLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemLogRepository extends JpaRepository<SystemLog,Long> {
}
