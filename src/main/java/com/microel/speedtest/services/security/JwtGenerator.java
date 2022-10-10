package com.microel.speedtest.services.security;

import java.time.Instant;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.microel.speedtest.repositories.entities.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.NonNull;

@Component
public class JwtGenerator {

    private final JwtConfigurationValues jwtConfigurationValues;

    public JwtGenerator(JwtConfigurationValues jwtConfigurationValues) {
        this.jwtConfigurationValues = jwtConfigurationValues;
    }

    public String generate(@NonNull User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(getTokenExpirationDate())
                .signWith(SignatureAlgorithm.HS512, jwtConfigurationValues.getSecret())
                .claim("role", user.getRole().getRoleId())
                .claim("name", user.getName())
                .compact();
    }

    public String generateRefresh(@NonNull User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(getRefreshExpirationDate())
                .signWith(SignatureAlgorithm.HS512, jwtConfigurationValues.getRefreshSecret())
                .compact();
    }

    private Date getTokenExpirationDate() {
        final Instant instant = Instant.now().plusSeconds(jwtConfigurationValues.getExpiration());
        return Date.from(instant);
    }

    private Date getRefreshExpirationDate() {
        final Instant instant = Instant.now().plusSeconds(jwtConfigurationValues.getRefreshExpiration());
        return Date.from(instant);
    }
}
