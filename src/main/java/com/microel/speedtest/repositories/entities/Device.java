package com.microel.speedtest.repositories.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import com.microel.speedtest.common.models.filters.DeviceFilter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

@Entity
@Table(name = "devices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Device {
    @Id
    private String deviceId;
    @Column(columnDefinition = "varchar(255) default '0.0.0.0'")
    private String ip;
    private String hostname;
    private String system;
    private String platform;
    private String userAgent;
    @Column(columnDefinition = "boolean default true")
    private Boolean isMobile;
    @Column(columnDefinition = "boolean default false")
    private Boolean isPro;

    @ManyToOne
    @JoinColumn(name = "f_session_id")
    private AcpSession lastSession;

    @OneToMany(mappedBy = "device", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private Set<Measure> measures;

    @Override
    public String toString() {
        return "Device [deviceId=" + deviceId + ", ip=" + ip + ", hostname=" + hostname + ", system=" + system
                + ", platform=" + platform + ", userAgent=" + userAgent + ", isMobile=" + isMobile + ", isPro=" + isPro + "]";
    }

    @Override
    public int hashCode() {
        return deviceId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Device other = (Device) obj;
        if (deviceId == null) {
            if (other.deviceId != null)
                return false;
        } else if (!deviceId.equals(other.deviceId))
            return false;
        return true;
    }
}
