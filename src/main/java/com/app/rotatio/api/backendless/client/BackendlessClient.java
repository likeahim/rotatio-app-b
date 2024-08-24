package com.app.rotatio.api.backendless.client;

import com.app.rotatio.api.backendless.config.BackendlessConfig;
import com.app.rotatio.controller.exception.UserLoginProcessException;
import com.app.rotatio.controller.exception.UserRegisterProcessException;
import com.app.rotatio.domain.BackendlessLoginUser;
import com.app.rotatio.domain.BackendlessUser;
import com.app.rotatio.service.UriService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class BackendlessClient {

    private final RestTemplate restTemplate;
    private final UriService uriService;
    private final BackendlessConfig config;

    public BackendlessUser registerUser(BackendlessUser user) {
        try {
            URI uri = uriService.getExtendedUri(config.getBackendlessApiSubEndpoint(), "/users/register");
            HttpHeaders headers = uriService.getContentTypeHeader(MediaType.APPLICATION_JSON);
            HttpEntity<BackendlessUser> request = new HttpEntity<>(user, headers);
            return restTemplate.postForObject(uri, request, BackendlessUser.class);
        } catch (RestClientException e) {
            throw new UserRegisterProcessException(e.getMessage());
        }
    }

    public BackendlessUser loginUser(final BackendlessLoginUser userToLogin) throws UserLoginProcessException {
        try {
            URI uri = uriService.getExtendedUri(config.getBackendlessApiSubEndpoint(), "/users/login");
            HttpHeaders headers = uriService.getContentTypeHeader(MediaType.APPLICATION_JSON);
            HttpEntity<BackendlessLoginUser> request = new HttpEntity<>(userToLogin, headers);
            return restTemplate.postForObject(uri, request, BackendlessUser.class);
        } catch (RestClientException e) {
            throw new UserLoginProcessException();
        }
    }

    public void logoutUser(final BackendlessUser user) {
        URI uri = uriService.getExtendedUri(config.getBackendlessApiSubEndpoint(), "/users/logout");
        HttpHeaders headers = uriService.getTokenHeader("user-token", user.getUserToken());
        HttpEntity<String> request = new HttpEntity<>(headers);
        restTemplate.exchange(uri, HttpMethod.GET, request, Void.class);
    }

    public BackendlessUser fetchUserById(final String objectId) {
        URI uri = uriService.getExtendedUri(config.getBackendlessApiSubEndpoint(), "/data/users/" + objectId);
        return restTemplate.getForObject(uri, BackendlessUser.class);
    }

    public void restorePassword(final String email) {
        URI uri = uriService.getExtendedUri(config.getBackendlessApiSubEndpoint(), "/users/restorepassword/" + email);
        restTemplate.exchange(uri, HttpMethod.GET, null, Void.class);
    }

    public Object deleteUser(final String objectId) {
        URI uri = uriService.getExtendedUri(config.getBackendlessApiSubEndpoint(), "/data/users/" + objectId);
        return restTemplate.exchange(uri, HttpMethod.DELETE, null, Object.class);
    }

    public BackendlessUser update(final BackendlessUser user) {
        URI uri = uriService.getExtendedUri(config.getBackendlessApiSubEndpoint(), "/users/" + user.getObjectId());
        HttpHeaders headers = uriService.getContentTypeHeader(MediaType.APPLICATION_JSON);
        headers.add("user-token", user.getUserToken());
        HttpEntity<BackendlessUser> request = new HttpEntity<>(user, headers);
        return restTemplate.exchange(uri, HttpMethod.PUT, request, BackendlessUser.class).getBody();
    }
}
