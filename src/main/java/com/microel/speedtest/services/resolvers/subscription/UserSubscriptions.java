package com.microel.speedtest.services.resolvers.subscription;

import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;

import com.microel.speedtest.common.models.updateprovides.UserUpdateProvider;

import graphql.kickstart.tools.GraphQLSubscriptionResolver;
import reactor.core.publisher.Flux;

@Service
public class UserSubscriptions implements GraphQLSubscriptionResolver {

    private final Flux<UserUpdateProvider> updateUserFlux;

    public UserSubscriptions(Flux<UserUpdateProvider> updateUserFlux) {
        this.updateUserFlux = updateUserFlux;
    }

    public Publisher<UserUpdateProvider> updateUsers() {
        return updateUserFlux;
    }
}
