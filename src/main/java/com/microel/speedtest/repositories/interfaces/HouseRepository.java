package com.microel.speedtest.repositories.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microel.speedtest.repositories.entities.AcpHouse;

@Repository
public interface HouseRepository extends JpaRepository<AcpHouse, Long> {
    AcpHouse findByAddressAndVlan(String address, Short vlan);
}
