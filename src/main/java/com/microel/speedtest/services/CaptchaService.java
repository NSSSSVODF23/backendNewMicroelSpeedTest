package com.microel.speedtest.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microel.speedtest.common.models.bodies.RecaptchaResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class CaptchaService {
    public Boolean validate(String token){
        try {
            final String apiResponse = Jsoup.connect("https://www.google.com/recaptcha/api/siteverify").data("secret","6LeEWkIiAAAAAGX92zBdWfIlksByH7o6c2S9RJ9R").data("response",token).ignoreContentType(true).post().text();
            final RecaptchaResponseBody responseBody = new ObjectMapper().readValue(apiResponse, RecaptchaResponseBody.class);
            return responseBody.getSuccess();
        } catch (IOException e) {
            log.warn(e.getMessage());
        }
        return false;
    }
}
