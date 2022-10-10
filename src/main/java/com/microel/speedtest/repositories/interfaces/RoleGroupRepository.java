package com.microel.speedtest.repositories.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microel.speedtest.repositories.entities.RoleGroup;

@Repository
public interface RoleGroupRepository extends JpaRepository<RoleGroup,Long> {
    
}
