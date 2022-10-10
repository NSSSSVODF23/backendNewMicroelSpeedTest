package com.microel.speedtest.common.models.bodies;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@ToString
public class RecaptchaResponseBody {
    private Boolean success;
    private Timestamp challenge_ts;
    private String hostname;
    private List<String> errorCodes;
}
