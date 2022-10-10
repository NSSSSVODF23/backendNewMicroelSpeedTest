package com.microel.speedtest.common.models.updateprovides;

import com.microel.speedtest.common.enums.ListMutationTypes;
import com.microel.speedtest.common.interfaces.IUpdateProvider;
import com.microel.speedtest.common.models.ActiveSession;
import com.microel.speedtest.repositories.entities.Measure;

public class ActiveSessionsUpdateProvider extends IUpdateProvider<ActiveSession> {
    public ActiveSessionsUpdateProvider(ListMutationTypes updateType, ActiveSession object) {
        super(updateType, object);
    }
}
