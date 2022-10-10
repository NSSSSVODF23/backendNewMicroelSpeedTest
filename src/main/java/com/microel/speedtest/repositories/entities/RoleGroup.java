package com.microel.speedtest.repositories.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "role_groups")
@Getter
@Setter
public class RoleGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;
    @Column(updatable = false)
    private String groupName;
    @Column(updatable = false)
    private String description;
    @OneToMany(mappedBy = "role")
    private Set<User> users;
}
