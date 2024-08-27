package com.app.rotatio.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class UriService {

    public HttpHeaders getContentTypeHeader(MediaType mediaType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        return headers;
    }

    public HttpHeaders getTokenHeader(String tokenName, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(tokenName, token);
        return headers;
    }

    public URI getExtendedUri(String uri, String path) {
        return UriComponentsBuilder.fromUri(getUri(uri))
                .path(path)
                .build().encode().toUri();
    }

    public URI getUri(String url) {
        return UriComponentsBuilder.fromHttpUrl(url)
                .build().encode().toUri();
    }

    public String encode(String input) {
        return input.replace("/", "%2F");
    }
}
