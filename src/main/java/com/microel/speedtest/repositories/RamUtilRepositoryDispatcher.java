package com.microel.speedtest.repositories;

import com.microel.speedtest.common.models.TimeRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microel.speedtest.repositories.entities.RamUtil;
import com.microel.speedtest.repositories.interfaces.RamUtilRepository;

import java.util.List;

@Component
public class RamUtilRepositoryDispatcher {
    
    @Autowired
    private RamUtilRepository repository;

    public RamUtil save(RamUtil entity){
        return repository.save(entity);
    }

    public List<RamUtil> getStatistic(TimeRange timeRange){
        return repository.findByStampBetween(timeRange.getStart(),timeRange.getEnd());
    }
}
