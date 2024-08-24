package com.app.rotatio.service;

import com.app.rotatio.api.backendless.client.BackendlessClient;
import com.app.rotatio.controller.exception.UserLoginProcessException;
import com.app.rotatio.controller.exception.UserRegisterProcessException;
import com.app.rotatio.domain.BackendlessLoginUser;
import com.app.rotatio.domain.BackendlessUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BackendlessServiceTest {

    private static final Logger log = LoggerFactory.getLogger(BackendlessServiceTest.class);

    @Mock
    private BackendlessClient backendlessClient;

    @InjectMocks
    private BackendlessService backendlessService;

    private BackendlessUser user;
    private BackendlessLoginUser loginUser;

    @BeforeEach
    void setUp() {
        user = BackendlessUser.builder()
                .email("test@example.com")
                .password("password")
                .build();
        loginUser = BackendlessLoginUser.builder()
                .login("test@example.com")
                .password("password")
                .build();
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        //Given
        when(backendlessClient.registerUser(any(BackendlessUser.class))).thenReturn(user);
        //When
        BackendlessUser registeredUser = backendlessService.registerUser(user);
        //Then
        assertNotNull(registeredUser);
        assertEquals("test@example.com", registeredUser.getEmail());
        verify(backendlessClient, times(1)).registerUser(user);
        log.info("User registered successfully in test.");
    }

    @Test
    void shouldFailToRegisterUserWithoudEmail() {
        //Given
        BackendlessUser invalidUser = BackendlessUser.builder()
                .email(null)
                .password("password")
                .build();
        when(backendlessClient.registerUser(invalidUser)).thenThrow(
                new UserRegisterProcessException("Missing email to register"));

        //When
        //Then
        assertThrows(UserRegisterProcessException.class, () -> {
            backendlessService.registerUser(invalidUser);
        });
    }

    @Test
    void shouldLoginUserSuccessfully() throws UserLoginProcessException {
        //Given
        when(backendlessClient.loginUser(any(BackendlessLoginUser.class))).thenReturn(user);
        //When
        BackendlessUser loggedInUser = backendlessService.loginUser(loginUser);
        //Then
        assertNotNull(loggedInUser);
        assertEquals("test@example.com", loggedInUser.getEmail());
        verify(backendlessClient, times(1)).loginUser(loginUser);
        log.info("User logged in successfully in test.");
    }

    @Test
    void shouldFailToLoginWhenEmailIsNull() throws UserLoginProcessException {
        //Given
        BackendlessLoginUser invalidUser = BackendlessLoginUser.builder()
                .login(null)
                .password("password")
                .build();
        when(backendlessClient.loginUser(invalidUser)).thenThrow(new UserLoginProcessException());
        //When
        //Then
        assertThrows(UserLoginProcessException.class, () -> {
            backendlessService.loginUser(invalidUser);
        });
    }

    @Test
    void shouldLogoutUserSuccessfully() {
        //When
        backendlessService.logoutUser(user);
        //Then
        verify(backendlessClient, times(1)).logoutUser(user);
        log.info("User logged out successfully in test.");
    }

    @Test
    void shouldGetUserSuccessfully() {
        //Given
        when(backendlessClient.fetchUserById("123")).thenReturn(user);
        //When
        BackendlessUser fetchedUser = backendlessService.getUser("123");
        //Then
        assertNotNull(fetchedUser);
        assertEquals("test@example.com", fetchedUser.getEmail());
        verify(backendlessClient, times(1)).fetchUserById("123");
    }

    @Test
    void shouldRestorePasswordSuccessfully() {
        //When
        backendlessService.restorePassword("test@example.com");
        //Then
        verify(backendlessClient, times(1)).restorePassword("test@example.com");
        log.info("Password restored successfully in test.");
    }

    @Test
    void shouldDeleteUserSuccessfully() {
        //Given
        when(backendlessClient.deleteUser("123")).thenReturn(true);
        //When
        Object result = backendlessService.deleteUser("123");
        //Then
        assertNotNull(result);
        assertTrue((Boolean) result);
        verify(backendlessClient, times(1)).deleteUser("123");
        log.info("User deleted successfully in test.");
    }
}