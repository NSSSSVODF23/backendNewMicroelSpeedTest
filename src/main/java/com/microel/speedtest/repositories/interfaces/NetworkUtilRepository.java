package com.microel.speedtest.repositories.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microel.speedtest.repositories.entities.NetworkUtil;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface NetworkUtilRepository extends JpaRepository<NetworkUtil, Long> {

    List<NetworkUtil> findByStampBetween(Timestamp start, Timestamp end);
}
