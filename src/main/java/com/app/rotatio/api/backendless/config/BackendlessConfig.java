package com.app.rotatio.api.backendless.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class BackendlessConfig {

    @Value("${backendless.api.sub.endpoint.prod}")
    private String backendlessApiSubEndpoint;
    @Value("${backendless.api.native.endpoint.prod}")
    private String backendlessNativeEndpoint;
    @Value("${backendless.rest.api.key}")
    private String backendlessRestApiKey;
    @Value("${backendless.cloud.code.api.key}")
    private String backendlessCloudCodeApiKey;
    @Value("${backendless.app.id}")
    private String backendlessAppId;
}
