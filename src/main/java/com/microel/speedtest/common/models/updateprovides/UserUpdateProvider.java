package com.microel.speedtest.common.models.updateprovides;

import com.microel.speedtest.common.enums.ListMutationTypes;
import com.microel.speedtest.common.interfaces.IUpdateProvider;
import com.microel.speedtest.repositories.entities.User;

public class UserUpdateProvider extends IUpdateProvider<User> {
    public UserUpdateProvider(ListMutationTypes updateType, User user) {
        super(updateType, user);
    }
}
