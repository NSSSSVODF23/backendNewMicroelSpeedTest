package com.microel.speedtest.repositories.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microel.speedtest.repositories.entities.CpuUtil;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface CpuUtilRepository extends JpaRepository<CpuUtil, Long> {

    List<CpuUtil> findByStampBetween(Timestamp start, Timestamp end);
}
