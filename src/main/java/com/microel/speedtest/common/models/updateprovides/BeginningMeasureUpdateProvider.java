package com.microel.speedtest.common.models.updateprovides;

import com.microel.speedtest.common.enums.ListMutationTypes;
import com.microel.speedtest.common.interfaces.IUpdateProvider;
import com.microel.speedtest.common.models.BeginningMeasure;

public class BeginningMeasureUpdateProvider extends IUpdateProvider<BeginningMeasure> {
    public BeginningMeasureUpdateProvider(ListMutationTypes updateType, BeginningMeasure object) {
        super(updateType, object);
    }
}
