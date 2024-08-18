package com.app.rotatio.api.time.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class TimeApiConfig {

    @Value("${time.api.datetime.endpoint.prod}")
    private String currentTimeByZoneEndpoint;
    @Value("${time.api.available.zones.endpoint.prod}")
    private String availableZonesEndpoint;
    @Value("${time.api.test.timezone}")
    private String testTimezone;
}
