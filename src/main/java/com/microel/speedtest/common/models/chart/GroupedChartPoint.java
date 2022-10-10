package com.microel.speedtest.common.models.chart;

import com.microel.speedtest.repositories.proxies.Proxy;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class GroupedChartPoint <X,Y,G> {
    private X x;
    private Y y;
    private G g;
}
