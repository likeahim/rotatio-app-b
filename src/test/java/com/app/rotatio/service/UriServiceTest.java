package com.app.rotatio.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UriServiceTest {

    private final UriService uriService = new UriService();

    @Test
    void shouldReturnContentTypeHeader() {
        //Given
        MediaType mediaType = MediaType.APPLICATION_JSON;
        //When
        HttpHeaders headers = uriService.getContentTypeHeader(mediaType);
        //Then
        assertNotNull(headers);
        assertEquals(MediaType.APPLICATION_JSON, headers.getContentType());
    }

    @Test
    void shouldReturnTokenHeader() {
        //Given
        String tokenName = "Authorization";
        String token = "Bearer abc123";
        //When
        HttpHeaders headers = uriService.getTokenHeader(tokenName, token);
        //Then
        assertNotNull(headers);
        assertEquals("Bearer abc123", headers.getFirst(tokenName));
    }

    @Test
    void shouldReturnExtendedUri() {
        //Given
        String baseUri = "http://localhost:8080/api";
        String path = "/v1/rotatio";
        //When
        URI result = uriService.getExtendedUri(baseUri, path);
        //Then
        assertNotNull(result);
        assertEquals("http://localhost:8080/api/v1/rotatio", result.toString());
    }

    @Test
    void shouldReturnUri() {
        //Given
        String url = "http://localhost:8080/api";
        //When
        URI result = uriService.getUri(url);
        //Then
        assertNotNull(result);
        assertEquals("http://localhost:8080/api", result.toString());
    }

    @Test
    void shouldEncodeString() {
        //Given
        String input = "api/v1/rotatio";
        //When
        String result = uriService.encode(input);
        //Then
        assertNotNull(result);
        assertEquals("api%2Fv1%2Frotatio", result);
    }
}