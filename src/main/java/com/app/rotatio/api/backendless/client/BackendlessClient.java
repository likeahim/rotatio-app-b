package com.app.rotatio.api.backendless.client;

import com.app.rotatio.controller.exception.UserRegisterProcessException;
import com.app.rotatio.domain.BackendlessLoginUser;
import com.app.rotatio.domain.BackendlessUser;
import com.app.rotatio.service.UriService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class BackendlessClient {

    private final RestTemplate restTemplate;
    private final UriService uriService;

    public BackendlessUser registerUser(BackendlessUser user) {
        try {
            URI uri = uriService.getExtendedUri("/users/register");
            HttpHeaders headers = uriService.getContentTypeHeader();
            HttpEntity<BackendlessUser> request = new HttpEntity<>(user, headers);
            return restTemplate.postForObject(uri, request, BackendlessUser.class);
        } catch (RestClientException e) {
            throw new UserRegisterProcessException(e.getMessage());
        }
    }

    public BackendlessUser loginUser(BackendlessLoginUser userToLogin) {
        URI uri = uriService.getExtendedUri("/users/login");
        HttpHeaders headers = uriService.getContentTypeHeader();
        HttpEntity<BackendlessLoginUser> request = new HttpEntity<>(userToLogin, headers);
        return restTemplate.postForObject(uri, request, BackendlessUser.class);
    }

    public void logoutUser(BackendlessUser user) {
        URI uri = uriService.getExtendedUri("/users/logout");
        HttpHeaders headers = uriService.getTokenHeader(user.getUserToken());
        HttpEntity<String> request = new HttpEntity<>(headers);
        restTemplate.exchange(uri, HttpMethod.GET, request, Void.class);
    }

    public BackendlessUser fetchUserById(String objectId) {
        URI uri = uriService.getExtendedUri("/data/users/" + objectId);
        return restTemplate.getForObject(uri, BackendlessUser.class);
    }

    public void restorePassword(String email) {
        URI uri = uriService.getExtendedUri("/users/restorepassword/" + email);
        restTemplate.exchange(uri, HttpMethod.GET, null, Void.class);
    }

    public Object deleteUser(String objectId) {
        URI uri = uriService.getExtendedUri("/data/users/" + objectId);
        return restTemplate.exchange(uri, HttpMethod.DELETE, null, Object.class);
    }
}
