package com.microel.speedtest.services.resolvers.subscription;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microel.speedtest.controllers.performance.PerformanceInfo;

import graphql.kickstart.tools.GraphQLSubscriptionResolver;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Service
public class PerformanceSubscriptions implements GraphQLSubscriptionResolver {

    private final Flux<PerformanceInfo> updatePerformanceFlux;

    public PerformanceSubscriptions(Flux<PerformanceInfo> updatePerformanceFlux) {
        this.updatePerformanceFlux = updatePerformanceFlux;
    }

    public Publisher<PerformanceInfo> getPerformance() {
        return updatePerformanceFlux;
    }
}
