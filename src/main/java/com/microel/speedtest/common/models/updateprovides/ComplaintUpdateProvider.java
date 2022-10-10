package com.microel.speedtest.common.models.updateprovides;

import com.microel.speedtest.common.enums.ListMutationTypes;
import com.microel.speedtest.common.interfaces.IUpdateProvider;
import com.microel.speedtest.repositories.entities.Complaint;

public class ComplaintUpdateProvider extends IUpdateProvider<Complaint> {
    public ComplaintUpdateProvider(ListMutationTypes updateType, Complaint object) {
        super(updateType, object);
    }
}
