package com.microel.speedtest.common.models.updateprovides;

import com.microel.speedtest.common.enums.ListMutationTypes;
import com.microel.speedtest.common.interfaces.IUpdateProvider;
import com.microel.speedtest.repositories.entities.Device;

public class DeviceUpdateProvider extends IUpdateProvider<Device> {
    public DeviceUpdateProvider(ListMutationTypes updateType, Device object) {
        super(updateType, object);
    }
}
