package com.microel.speedtest.services.security;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.microel.speedtest.repositories.UserRepositoryDispatcher;
import com.microel.speedtest.repositories.entities.User;
import com.microel.speedtest.services.security.dispath.AuthRequest;
import com.microel.speedtest.services.security.dispath.AuthResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.NonNull;

@Component
public class JwtAuthenticationProvider {

    private final JwtGenerator jwtGenerator;

    private final JwtValidator jwtValidator;

    private final UserRepositoryDispatcher userRepositoryDispatcher;

    private final JwtConfigurationValues jwtConfigurationValues;

    public JwtAuthenticationProvider(JwtGenerator jwtGenerator, JwtValidator jwtValidator, UserRepositoryDispatcher userRepositoryDispatcher, JwtConfigurationValues jwtConfigurationValues) {
        this.jwtGenerator = jwtGenerator;
        this.jwtValidator = jwtValidator;
        this.userRepositoryDispatcher = userRepositoryDispatcher;
        this.jwtConfigurationValues = jwtConfigurationValues;
    }

    public AuthResponse login(AuthRequest request) {
        User user = userRepositoryDispatcher.findByUsername(request.getUsername());
        if (user == null) {
            return null;
        }
        if (!user.getPassword().equals(request.getPassword())) {
            return null;
        }
        userRepositoryDispatcher.updateLoginTime(user);
        return new AuthResponse(jwtGenerator.generate(user), jwtGenerator.generateRefresh(user));
    }

    public AuthResponse getToken(@NonNull String refreshToken) {
        if (validateRefresh(refreshToken)) {
            final Claims claims = getRefreshClaims(refreshToken);
            final String username = claims.getSubject();
            final User user = userRepositoryDispatcher.findByUsername(username);
            if (user == null)
                throw new RuntimeException("User not found");
            return new AuthResponse(jwtGenerator.generate(user), null);
        }
        return new AuthResponse(null, null);
    }

    public AuthResponse refresh(@NonNull String refreshToken) {
        if (validateRefresh(refreshToken)) {
            final Claims claims = getRefreshClaims(refreshToken);
            final String username = claims.getSubject();
            final User user = userRepositoryDispatcher.findByUsername(username);
            if (user == null)
                throw new RuntimeException("Пользователь не найден");
            final String accessToken = jwtGenerator.generate(user);
            final String newRefreshToken = jwtGenerator.generateRefresh(user);
            return new AuthResponse(accessToken, newRefreshToken);

        }
        throw new RuntimeException("Невалидный JWT токен");
    }

    public boolean validate(String token) {
        return jwtValidator.validate(token, jwtConfigurationValues.getSecret());
    }

    public boolean validateRefresh(String token) {
        return jwtValidator.validate(token, jwtConfigurationValues.getRefreshSecret());
    }

    public Claims getAccessClaims(@NonNull String token) {
        return getClaims(token, jwtConfigurationValues.getSecret());
    }

    public Claims getRefreshClaims(@NonNull String token) {
        return getClaims(token, jwtConfigurationValues.getRefreshSecret());
    }

    private Claims getClaims(@NonNull String token, @NonNull String secret) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    public JwtAuthentication getAuthentication(@NonNull String token) {
        final Claims claims = getAccessClaims(token);
        final String username = claims.getSubject();
        final String name = claims.get("name", String.class);
        final Object rawRoles = claims.get("roles");
        final Set<Roles> roles = rawRoles != null
                ? ((Set<?>) rawRoles).stream().map(Object::toString).map(Roles::valueOf).collect(Collectors.toSet())
                : null;

        final User user = userRepositoryDispatcher.findByUsername(username);
        if (user == null)
            throw new RuntimeException("Пользователь не найден");
        return new JwtAuthentication(true, username, name, roles);
    }
}
