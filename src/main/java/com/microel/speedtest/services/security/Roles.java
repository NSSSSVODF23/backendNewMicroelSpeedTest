package com.microel.speedtest.services.security;

import org.springframework.security.core.GrantedAuthority;

public enum Roles implements GrantedAuthority {
    USER("USER"),
    ADMIN("ADMIN");

    private final String role;

    Roles(String role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role;
    }
}
