package com.microel.speedtest.common.models.bodies;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GettingMeasureBody {
    private Long id;

    public Boolean isWrong(){
        return id == null;
    }
}
