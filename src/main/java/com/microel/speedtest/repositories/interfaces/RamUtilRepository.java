package com.microel.speedtest.repositories.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microel.speedtest.repositories.entities.RamUtil;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface RamUtilRepository extends JpaRepository<RamUtil, Long> {

    List<RamUtil> findByStampBetween(Timestamp start, Timestamp end);
}
