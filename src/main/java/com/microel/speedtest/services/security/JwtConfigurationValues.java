package com.microel.speedtest.services.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class JwtConfigurationValues {
    private String secret;
    private Long expiration;
    private String refreshSecret;
    private Long refreshExpiration;

    @Value("${jwt.access.token.secret}")
    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Value("${jwt.access.token.expiration}")
    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }

    @Value("${jwt.refresh.token.secret}")
    public void setRefreshSecret(String refreshSecret) {
        this.refreshSecret = refreshSecret;
    }

    @Value("${jwt.refresh.token.expiration}")
    public void setRefreshExpiration(Long refreshExpiration) {
        this.refreshExpiration = refreshExpiration;
    }
}
