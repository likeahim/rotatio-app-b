package com.app.rotatio.api.backendless.client;

import com.app.rotatio.api.backendless.config.BackendlessConfig;
import com.app.rotatio.config.AdminConfig;
import com.app.rotatio.controller.exception.UserLoginProcessException;
import com.app.rotatio.controller.exception.UserRegisterProcessException;
import com.app.rotatio.domain.BackendlessLoginUser;
import com.app.rotatio.domain.BackendlessUser;
import com.app.rotatio.service.UriService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class BackendlessClientTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private UriService uriService;

    @Mock
    private BackendlessConfig config;

    @Mock
    private AdminConfig adminConfig;

    @InjectMocks
    private BackendlessClient backendlessClient;

    private BackendlessUser backendlessUser;
    private BackendlessLoginUser backendlessLoginUser;

    @BeforeEach
    void setUp() {
        backendlessUser = BackendlessUser.builder()
                .email("test@example.com")
                .userToken("testToken")
                .build();

        backendlessLoginUser = BackendlessLoginUser.builder()
                .login("test@example.com")
                .password("password")
                .build();
        when(config.getBackendlessApiSubEndpoint()).thenReturn("http://backendless.com");
    }

    @Test
    void shouldRegisterUser() {
        URI uri = URI.create("http://backendless.com/users/register");
        when(uriService.getExtendedUri(config.getBackendlessApiSubEndpoint(), "/users/register")).thenReturn(uri);
        when(uriService.getContentTypeHeader(MediaType.APPLICATION_JSON)).thenReturn(new HttpHeaders());
        when(restTemplate.postForObject(any(URI.class), any(HttpEntity.class), eq(BackendlessUser.class)))
                .thenReturn(backendlessUser);

        BackendlessUser result = backendlessClient.registerUser(backendlessUser);

        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void shouldThrowExceptionWhenRegisterUserFails() {
        URI uri = URI.create("http://backendless.com/users/register");
        when(uriService.getExtendedUri(config.getBackendlessApiSubEndpoint(), "/users/register")).thenReturn(uri);
        when(uriService.getContentTypeHeader(MediaType.APPLICATION_JSON)).thenReturn(new HttpHeaders());
        when(restTemplate.postForObject(any(URI.class), any(HttpEntity.class), eq(BackendlessUser.class)))
                .thenThrow(new RestClientException("Registration failed"));

        assertThrows(UserRegisterProcessException.class, () -> backendlessClient.registerUser(backendlessUser));
    }

    @Test
    void shouldLoginUser() throws UserLoginProcessException {
        URI uri = URI.create("http://backendless.com/users/login");
        when(uriService.getExtendedUri(config.getBackendlessApiSubEndpoint(), "/users/login")).thenReturn(uri);
        when(uriService.getContentTypeHeader(MediaType.APPLICATION_JSON)).thenReturn(new HttpHeaders());
        when(restTemplate.postForObject(any(URI.class), any(HttpEntity.class), eq(BackendlessUser.class)))
                .thenReturn(backendlessUser);

        BackendlessUser result = backendlessClient.loginUser(backendlessLoginUser);

        assertNotNull(result);
        assertEquals("testToken", result.getUserToken());
    }

    @Test
    void shouldLogoutUser() {
        URI uri = URI.create("http://backendless.com/users/logout");
        HttpHeaders headers = new HttpHeaders();
        headers.set("user-token", "testToken");
        when(uriService.getExtendedUri(config.getBackendlessApiSubEndpoint(), "/users/logout")).thenReturn(uri);
        when(uriService.getTokenHeader(anyString(), anyString())).thenReturn(headers);

        backendlessClient.logoutUser(backendlessUser);

        verify(restTemplate, times(1)).exchange(
                eq(uri), eq(HttpMethod.GET), any(HttpEntity.class), eq(Void.class));
    }

    @Test
    void shouldFetchUserById() {
        URI uri = URI.create("http://backendless.com/data/users/12345");
        when(uriService.getExtendedUri(config.getBackendlessApiSubEndpoint(), "/data/users/12345")).thenReturn(uri);
        when(restTemplate.getForObject(any(URI.class), eq(BackendlessUser.class)))
                .thenReturn(backendlessUser);

        BackendlessUser result = backendlessClient.fetchUserById("12345");

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void shouldRestorePassword() {
        URI uri = URI.create("http://backendless.com/users/restorepassword/test@example.com");
        when(uriService.getExtendedUri(config.getBackendlessApiSubEndpoint(),
                "/users/restorepassword/test@example.com")).thenReturn(uri);

        backendlessClient.restorePassword("test@example.com");

        verify(restTemplate, times(1)).exchange(eq(uri),
                eq(HttpMethod.GET), isNull(), eq(Void.class));
    }

    @Test
    void shouldDeleteUser() {
        URI uri = URI.create("http://backendless.com/data/users/12345");
        when(uriService.getExtendedUri(config.getBackendlessApiSubEndpoint(), "/data/users/12345")).thenReturn(uri);

        backendlessClient.deleteUser("12345");

        verify(restTemplate, times(1)).exchange(eq(uri), eq(HttpMethod.DELETE), isNull(), eq(Object.class));
    }
}