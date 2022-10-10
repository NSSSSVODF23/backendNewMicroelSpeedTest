package com.microel.speedtest.services.resolvers.query;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microel.speedtest.repositories.HouseRepositoryDispatcher;
import com.microel.speedtest.repositories.entities.AcpHouse;

import graphql.kickstart.tools.GraphQLQueryResolver;

@Service
public class HouseQueries implements GraphQLQueryResolver {

    private final HouseRepositoryDispatcher houseRepositoryDispatcher;

    public HouseQueries(HouseRepositoryDispatcher houseRepositoryDispatcher) {
        this.houseRepositoryDispatcher = houseRepositoryDispatcher;
    }

    public List<AcpHouse> getAllHouses() {
        return houseRepositoryDispatcher.getAllHouses();
    }
}
