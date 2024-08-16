package com.app.rotatio.api.backendless.client;

import com.app.rotatio.api.backendless.config.BackendlessConfig;
import com.app.rotatio.domain.BackendlessUser;
import com.app.rotatio.domain.dto.backendless.BackendlessUserToLoginDto;
import com.app.rotatio.domain.dto.backendless.BackendlessUserToRegisterDto;
import com.app.rotatio.domain.dto.backendless.BackendlessUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class BackendlessClient {

    private final RestTemplate restTemplate;
    private final BackendlessConfig config;

    public BackendlessUserDto registerUser(BackendlessUserDto userDto) {
        URI uri = getExtendedUri("/users/register");
        HttpHeaders headers = getContentTypeHeader();
        HttpEntity<BackendlessUserDto> request = new HttpEntity<>(userDto, headers);
        return restTemplate.postForObject(uri, request, BackendlessUserDto.class);
    }

    public BackendlessUserDto loginUser(BackendlessUserToLoginDto userDto) {
        URI uri = getExtendedUri("/users/login");
        HttpHeaders headers = getContentTypeHeader();
        HttpEntity<BackendlessUserToLoginDto> request = new HttpEntity<>(userDto, headers);
        return restTemplate.postForObject(uri, request, BackendlessUserDto.class);
    }

    public void logoutUser(BackendlessUser user) {
        URI uri = getExtendedUri("/users/logout");
        HttpHeaders headers = getTokenHeader(user.getUserToken());
        HttpEntity<String> request = new HttpEntity<>(headers);
        restTemplate.exchange(uri, HttpMethod.GET, request, Void.class);
    }

    public BackendlessUserDto fetchUserById(BackendlessUser user) {
        URI uri = getExtendedUri("/data/users/" + user.getObjectId());
        return restTemplate.getForObject(uri, BackendlessUserDto.class);
    }



    private HttpHeaders getContentTypeHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private HttpHeaders getTokenHeader(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("user-token", token);
        return headers;
    }

    private URI getUri() {
        return UriComponentsBuilder.fromHttpUrl(config.getBackendlessApiSubEndpoint())
                .build().encode().toUri();
    }

    private URI getExtendedUri(String path) {
        return UriComponentsBuilder.fromUri(getUri())
                .path(path)
                .build().encode().toUri();
    }

}
