package com.microel.speedtest.services.resolvers.subscription;

import com.microel.speedtest.common.models.updateprovides.DeviceUpdateProvider;
import graphql.kickstart.tools.GraphQLSubscriptionResolver;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class DeviceSubscription implements GraphQLSubscriptionResolver {
    private final Flux<DeviceUpdateProvider> updateDeviceFlux;

    public DeviceSubscription(Flux<DeviceUpdateProvider> updateDeviceFlux) {
        this.updateDeviceFlux = updateDeviceFlux;
    }

    public Publisher<DeviceUpdateProvider> updateDevices(){
        return updateDeviceFlux;
    }
}
