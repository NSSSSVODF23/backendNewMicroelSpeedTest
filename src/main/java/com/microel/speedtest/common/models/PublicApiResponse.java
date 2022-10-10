package com.microel.speedtest.common.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PublicApiResponse {
    private Boolean isError;
    private String errorMessage;
    private Object responseBody;

    public static PublicApiResponse error(String message){
        PublicApiResponse o = new PublicApiResponse();
        o.errorMessage = message;
        o.isError = true;
        return o;
    }

    public static PublicApiResponse ok(Object responseBody){
        PublicApiResponse o = new PublicApiResponse();
        o.responseBody = responseBody;
        return o;
    }

    public static PublicApiResponse wrongRequest(){
        PublicApiResponse o = new PublicApiResponse();
        o.errorMessage = "Не верный запрос";
        o.isError = true;
        return o;
    }
}
