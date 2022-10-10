package com.microel.speedtest.repositories.interfaces;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.microel.speedtest.repositories.entities.Device;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    Device findByDeviceId(String deviceId);

}
