package com.app.rotatio.service;

import com.app.rotatio.api.backendless.config.BackendlessConfig;
import com.app.rotatio.controller.exception.EmailEncodingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class UriService {

    private final BackendlessConfig config;

    public HttpHeaders getContentTypeHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public HttpHeaders getTokenHeader(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("user-token", token);
        return headers;
    }

    public URI getExtendedUri(String path) {
        return UriComponentsBuilder.fromUri(getUri())
                .path(path)
                .build().encode().toUri();
    }

    private URI getUri() {
        return UriComponentsBuilder.fromHttpUrl(config.getBackendlessApiSubEndpoint())
                .build().encode().toUri();
    }
}
