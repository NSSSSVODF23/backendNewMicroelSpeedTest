package com.microel.speedtest.services.resolvers.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.microel.speedtest.services.security.JwtAuthenticationProvider;
import com.microel.speedtest.services.security.dispath.AuthRequest;
import com.microel.speedtest.services.security.dispath.AuthResponse;
import com.microel.speedtest.services.security.dispath.RefreshRequest;

@Controller
@RequestMapping("public")
public class AuthenticationResolver {

    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    @PostMapping("login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {

        AuthResponse authResponse = jwtAuthenticationProvider.login(request);
        if (authResponse == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("token")
    public ResponseEntity<AuthResponse> getToken(@RequestBody RefreshRequest request) {
        return ResponseEntity.ok(jwtAuthenticationProvider.getToken(request.getRefreshToken()));
    }

    @PostMapping("refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshRequest request) {
        return ResponseEntity.ok(jwtAuthenticationProvider.refresh(request.getRefreshToken()));
    }
}
