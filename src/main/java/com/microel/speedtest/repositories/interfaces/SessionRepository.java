package com.microel.speedtest.repositories.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.microel.speedtest.repositories.entities.AcpSession;

@Repository
public interface SessionRepository extends JpaRepository<AcpSession, Long> {
    AcpSession findByMacAndIp(String mac, String ip);

    AcpSession findByMacAndLoginAndIp(String mac, String login, String ip);

    @Modifying
    @Query(value = "UPDATE acp_session SET f_house_id = :house, login = :login, mac = :mac, vlan = :vlan WHERE session_id = :sessionId", nativeQuery = true)
    boolean updateSession(@Param("login") String login, @Param("house") Long house, @Param("mac") String mac,
            @Param("vlan") Short vlan,
            @Param("sessionId") Long sessionId);
}
