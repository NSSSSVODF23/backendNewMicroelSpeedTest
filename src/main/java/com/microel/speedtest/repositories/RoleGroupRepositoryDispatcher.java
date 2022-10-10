package com.microel.speedtest.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microel.speedtest.repositories.entities.RoleGroup;
import com.microel.speedtest.repositories.interfaces.RoleGroupRepository;

@Component
public class RoleGroupRepositoryDispatcher {
    private final RoleGroupRepository roleGroupRepository;

    public RoleGroupRepositoryDispatcher(RoleGroupRepository roleGroupRepository) {
        this.roleGroupRepository = roleGroupRepository;
    }

    public List<RoleGroup> getAllRoles() {
        return roleGroupRepository.findAll();
    }

    public RoleGroup save(RoleGroup roleGroup) {
        return roleGroupRepository.save(roleGroup);
    }
}
