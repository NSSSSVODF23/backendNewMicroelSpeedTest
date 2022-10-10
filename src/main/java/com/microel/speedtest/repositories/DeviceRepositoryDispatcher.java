package com.microel.speedtest.repositories;

import com.microel.speedtest.common.enums.ListMutationTypes;
import com.microel.speedtest.common.models.TimeRange;
import com.microel.speedtest.common.models.updateprovides.DeviceUpdateProvider;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.convert.QueryByExamplePredicateBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.microel.speedtest.repositories.entities.Device;
import com.microel.speedtest.repositories.interfaces.DeviceRepository;
import reactor.core.publisher.Sinks;

@Component
public class DeviceRepositoryDispatcher {
    private final DeviceRepository deviceRepository;
    private final Sinks.Many<DeviceUpdateProvider> updateDeviceSink;

    public DeviceRepositoryDispatcher(DeviceRepository deviceRepository, Sinks.Many<DeviceUpdateProvider> updateDeviceSink) {
        this.deviceRepository = deviceRepository;
        this.updateDeviceSink = updateDeviceSink;
    }

    public Device save(Device device) {
        final Device savedDevice = deviceRepository.save(device);
        updateDeviceSink.tryEmitNext(new DeviceUpdateProvider(ListMutationTypes.UPDATE, savedDevice));
        return savedDevice;
    }

    public Device updateDeviceInfo(Device device){
        final Device foundDevice = deviceRepository.findByDeviceId(device.getDeviceId());
        if(foundDevice != null){
            foundDevice.setIp(device.getIp());
            foundDevice.setHostname(device.getHostname());
            foundDevice.setIsMobile(device.getIsMobile());
            foundDevice.setSystem(device.getSystem());
            foundDevice.setPlatform(device.getPlatform());
            foundDevice.setUserAgent(device.getUserAgent());
            updateDeviceSink.tryEmitNext(new DeviceUpdateProvider(ListMutationTypes.UPDATE, foundDevice));
            return deviceRepository.save(foundDevice);
        }else{
            return deviceRepository.save(device);
        }
    }

    public void updateSession(Device device){
        final Device foundDevice = deviceRepository.findByDeviceId(device.getDeviceId());
        if(foundDevice != null){
            foundDevice.setLastSession(device.getLastSession());
            updateDeviceSink.tryEmitNext(new DeviceUpdateProvider(ListMutationTypes.UPDATE, foundDevice));
            deviceRepository.save(foundDevice);
        }
    }

    private Specification<Device> createdRange(TimeRange timeRange){
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.between(root.get("created"),timeRange.getStart(),timeRange.getEnd());
        };
    }

    private Specification<Device> matched(Example<Device> example){
        return (root, query, criteriaBuilder) -> {
            return QueryByExamplePredicateBuilder.getPredicate(root,criteriaBuilder,example);
        };
    }

    public Page<Device> find(Example<Device> example, Pageable paginator) {
        return deviceRepository.findAll(example, paginator);
    }

    public Long count(Example<Device> example) {
        return deviceRepository.count(example);
    }

    public Device findById(String deviceId) {
        return deviceRepository.findByDeviceId(deviceId);
    }

}
