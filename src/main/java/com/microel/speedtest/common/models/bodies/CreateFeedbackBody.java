package com.microel.speedtest.common.models.bodies;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CreateFeedbackBody {
    private Integer rating;

    public Boolean isWrong() {
        return rating == null || rating > 5 || rating < 1;
    }
}
