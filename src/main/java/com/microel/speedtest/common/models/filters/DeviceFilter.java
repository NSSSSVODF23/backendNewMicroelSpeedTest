package com.microel.speedtest.common.models.filters;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DeviceFilter {
    private String deviceId;
    private String login;
    private Long address;
    private String ip;
    private String hostname;
    private Integer first;
    private Integer rows;
}
