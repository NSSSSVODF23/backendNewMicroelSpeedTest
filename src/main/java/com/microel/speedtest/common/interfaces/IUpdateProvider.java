package com.microel.speedtest.common.interfaces;

import com.microel.speedtest.common.enums.ListMutationTypes;

public abstract class IUpdateProvider<T> {
    protected ListMutationTypes updateType;
    protected T object;

    public IUpdateProvider(ListMutationTypes updateType, T object) {
        this.updateType = updateType;
        this.object = object;
    }
}
