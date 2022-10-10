package com.microel.speedtest.services.security.dispath;

import lombok.Getter;

@Getter
public class AuthResponse {
    private String type = "Bearer";
    private String token;
    private String refreshToken;

    public AuthResponse(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }
}
