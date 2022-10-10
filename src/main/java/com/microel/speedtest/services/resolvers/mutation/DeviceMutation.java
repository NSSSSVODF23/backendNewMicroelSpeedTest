package com.microel.speedtest.services.resolvers.mutation;

import com.microel.speedtest.common.enums.ListMutationTypes;
import com.microel.speedtest.common.exceptions.CustomGraphqlException;
import com.microel.speedtest.common.models.updateprovides.DeviceUpdateProvider;
import com.microel.speedtest.repositories.DeviceRepositoryDispatcher;
import com.microel.speedtest.repositories.entities.Device;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

import java.util.UUID;

@Service
public class DeviceMutation implements GraphQLMutationResolver {

    private final DeviceRepositoryDispatcher deviceRepositoryDispatcher;
    private final Sinks.Many<DeviceUpdateProvider> updateDeviceSink;

    public DeviceMutation(DeviceRepositoryDispatcher deviceRepositoryDispatcher, Sinks.Many<DeviceUpdateProvider> updateDeviceSink) {
        this.deviceRepositoryDispatcher = deviceRepositoryDispatcher;
        this.updateDeviceSink = updateDeviceSink;
    }

    public Boolean setDeviceMode(String deviceId, Boolean mode){
        final Device device = deviceRepositoryDispatcher.findById(deviceId);
        if(device == null) throw new CustomGraphqlException("Устройство не найдено");
        device.setIsPro(mode);
        updateDeviceSink.tryEmitNext(new DeviceUpdateProvider(ListMutationTypes.UPDATE, deviceRepositoryDispatcher.save(device)));
        return true;
    }
}
