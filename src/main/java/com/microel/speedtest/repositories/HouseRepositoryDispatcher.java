package com.microel.speedtest.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microel.speedtest.repositories.entities.AcpHouse;
import com.microel.speedtest.repositories.interfaces.HouseRepository;

@Component
public class HouseRepositoryDispatcher {
    @Autowired
    private HouseRepository houseRepository;

    public Long getHouseId(AcpHouse house) {
        final AcpHouse foundHouse = houseRepository.findByAddressAndVlan(house.getAddress(), house.getVlan());
        if (foundHouse != null) {
            return foundHouse.getHouseId();
        }
        return null;
    }

    public AcpHouse save(AcpHouse house) {
        house.setHouseId(getHouseId(house));
        return houseRepository.save(house);
    }

    public List<AcpHouse> getAllHouses() {
        return houseRepository.findAll();
    }
}
