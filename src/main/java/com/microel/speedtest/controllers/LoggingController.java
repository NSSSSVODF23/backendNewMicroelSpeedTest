package com.microel.speedtest.controllers;

import com.microel.speedtest.repositories.HouseRepositoryDispatcher;
import com.microel.speedtest.repositories.RoleGroupRepositoryDispatcher;
import com.microel.speedtest.repositories.SystemLogRepositoryDispatcher;
import com.microel.speedtest.repositories.UserRepositoryDispatcher;
import com.microel.speedtest.repositories.entities.AcpHouse;
import com.microel.speedtest.repositories.entities.RoleGroup;
import com.microel.speedtest.repositories.entities.User;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class LoggingController {
    private final SystemLogRepositoryDispatcher systemLogRepositoryDispatcher;
    private final UserRepositoryDispatcher userRepositoryDispatcher;
    private final RoleGroupRepositoryDispatcher roleGroupRepositoryDispatcher;
    private final HouseRepositoryDispatcher houseRepositoryDispatcher;

    public LoggingController(SystemLogRepositoryDispatcher systemLogRepositoryDispatcher, UserRepositoryDispatcher userRepositoryDispatcher, RoleGroupRepositoryDispatcher roleGroupRepositoryDispatcher, HouseRepositoryDispatcher houseRepositoryDispatcher) {
        this.systemLogRepositoryDispatcher = systemLogRepositoryDispatcher;
        this.userRepositoryDispatcher = userRepositoryDispatcher;
        this.roleGroupRepositoryDispatcher = roleGroupRepositoryDispatcher;
        this.houseRepositoryDispatcher = houseRepositoryDispatcher;

        firstInitializeDB();
        systemLogRepositoryDispatcher.insertRunningLog();
    }

    private void firstInitializeDB(){
        if(systemLogRepositoryDispatcher.isClear()){
            RoleGroup adminRole = new RoleGroup();
            adminRole.setDescription("Администратор");
            adminRole.setGroupName("admin");
            RoleGroup userRole = new RoleGroup();
            userRole.setDescription("Пользователь");
            userRole.setGroupName("user");
            adminRole = roleGroupRepositoryDispatcher.save(adminRole);
            roleGroupRepositoryDispatcher.save(userRole);
            User admin = new User();
            admin.setRole(adminRole);
            admin.setName("Admin");
            admin.setUsername("admin");
            admin.setPassword("admin");
            userRepositoryDispatcher.insert(admin);
            AcpHouse house = new AcpHouse();
            house.setAddress("Не определен");
            house.setVlan((short) 0);
            houseRepositoryDispatcher.save(house);
        }
    }
}
