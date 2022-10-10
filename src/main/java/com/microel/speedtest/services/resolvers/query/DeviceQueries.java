package com.microel.speedtest.services.resolvers.query;

import java.util.List;

import com.microel.speedtest.common.models.QueryLimit;
import com.microel.speedtest.common.models.filters.MatchingFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.microel.speedtest.repositories.DeviceRepositoryDispatcher;
import com.microel.speedtest.repositories.entities.Device;

import graphql.kickstart.tools.GraphQLQueryResolver;

@Service
public class DeviceQueries implements GraphQLQueryResolver {

    private final DeviceRepositoryDispatcher deviceRepositoryDispatcher;

    public DeviceQueries(DeviceRepositoryDispatcher deviceRepositoryDispatcher) {
        this.deviceRepositoryDispatcher = deviceRepositoryDispatcher;
    }

    public Page<Device> getDevices(Device matchingObject, QueryLimit limit) {
        return deviceRepositoryDispatcher.find(MatchingFactory.standardExample(matchingObject), MatchingFactory.paginator(limit, Sort.unsorted()));
    }

    public Long getTotalDevices(Device matchingObject) {

        return deviceRepositoryDispatcher.count(MatchingFactory.standardExample(matchingObject));
    }
}
