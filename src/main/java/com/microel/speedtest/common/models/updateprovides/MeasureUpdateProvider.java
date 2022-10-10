package com.microel.speedtest.common.models.updateprovides;

import com.microel.speedtest.common.enums.ListMutationTypes;
import com.microel.speedtest.common.interfaces.IUpdateProvider;
import com.microel.speedtest.repositories.entities.Measure;

public class MeasureUpdateProvider extends IUpdateProvider<Measure> {
    public MeasureUpdateProvider(ListMutationTypes updateType, Measure object) {
        super(updateType, object);
    }
}
