package com.microel.speedtest.common.types;

import java.io.Serializable;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpeedChartPoint implements Serializable {
    public Integer stamp;
    public Float speed;

    public SpeedChartPoint() {
    }

    public SpeedChartPoint(Integer stamp, Float speed) {
        this.stamp = stamp;
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "SpeedChartParticle{" +
                "stamp=" + stamp +
                ", speed=" + speed +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        SpeedChartPoint that = (SpeedChartPoint) o;

        return stamp.equals(that.stamp) && speed.equals(that.speed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stamp, speed);
    }
}
