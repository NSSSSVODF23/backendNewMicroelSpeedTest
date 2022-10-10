package com.microel.speedtest.repositories;

import com.microel.speedtest.common.models.TimeRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microel.speedtest.repositories.entities.CpuUtil;
import com.microel.speedtest.repositories.interfaces.CpuUtilRepository;

import java.util.List;

@Component
public class CpuUtilRepositoryDispatcher {

    @Autowired
    private CpuUtilRepository repository;

    public CpuUtil save(CpuUtil entity) {
        return repository.save(entity);
    }

    public List<CpuUtil> getStatistic(TimeRange timeRange) {
        return repository.findByStampBetween(timeRange.getStart(),timeRange.getEnd());
    }
}
