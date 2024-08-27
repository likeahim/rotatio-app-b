package com.app.rotatio.api.backendless.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class BackendlessConfig {

    @Value("${backendless.api.sub.endpoint.prod}")
    private String backendlessApiSubEndpoint;
}
