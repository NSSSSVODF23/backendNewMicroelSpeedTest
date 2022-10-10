package com.microel.speedtest.common.models.bodies;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateComplaintBody {
    private String phone;
    private String description;

    private String captchaToken;

    public Boolean isWrong() {
        return phone == null || description == null;
    }
}
