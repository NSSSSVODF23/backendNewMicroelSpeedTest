package com.microel.speedtest.controllers.uploadtest.types;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadParticle {
    private Long b;
    private Long e;
    private Integer i;

    @Override
    public String toString() {
        if(b == null || e==null|| i==null) throw new RuntimeException("Не верный UploadParticle");
        String response = "{" +
                "\"b\":" + "" + b + "," +
                "\"e\":" + "" + e + "," +
                "\"i\":" + "" + i + "" +
                "}";
        return response;
    }
}
