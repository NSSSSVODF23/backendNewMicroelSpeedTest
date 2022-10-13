package com.microel.speedtest.common.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QueryLimit {
    private Integer offset=0;
    private Integer limit=1;
    public static QueryLimit of(Integer offset, Integer limit){
        return new QueryLimit(offset,limit);
    }
}
