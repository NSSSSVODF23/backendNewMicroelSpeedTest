package com.microel.speedtest.common.models.filters;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ComplaintsFilter {
    private String login;
    private String phone;
    private Boolean processed;
    private Long address;
    private String ip;
    private String mac;
    private String start;
    private String end;
    private Integer first;
    private Integer rows;
}
