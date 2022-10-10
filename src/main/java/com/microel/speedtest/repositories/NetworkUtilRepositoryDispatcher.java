package com.microel.speedtest.repositories;

import com.microel.speedtest.common.models.TimeRange;

import com.microel.speedtest.repositories.entities.NetworkUtil;
import com.microel.speedtest.repositories.interfaces.NetworkUtilRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NetworkUtilRepositoryDispatcher {

    private final NetworkUtilRepository repository;

    public NetworkUtilRepositoryDispatcher(NetworkUtilRepository repository) {
        this.repository = repository;
    }

    public NetworkUtil save(NetworkUtil entity) {
        return repository.save(entity);
    }

    public List<NetworkUtil> getStatistic(TimeRange timeRange) {
        return repository.findByStampBetween(timeRange.getStart(),timeRange.getEnd());
    }
}
