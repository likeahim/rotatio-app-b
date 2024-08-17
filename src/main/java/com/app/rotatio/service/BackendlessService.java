package com.app.rotatio.service;

import com.app.rotatio.api.backendless.client.BackendlessClient;
import com.app.rotatio.domain.BackendlessUser;
import com.app.rotatio.domain.dto.backendless.BackendlessUserDto;
import com.app.rotatio.domain.dto.backendless.BackendlessUserToLoginDto;
import com.app.rotatio.domain.dto.backendless.BackendlessUserToRegisterDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BackendlessService {

    private final BackendlessClient backendlessClient;

    public BackendlessUserDto registerUser(BackendlessUserToRegisterDto user) {
        log.info("Registering user " + user.email());
        return backendlessClient.registerUser(user);
    }

    public BackendlessUserDto loginUser(BackendlessUserToLoginDto user) {
        log.info("Login user " + user.login());
        return backendlessClient.loginUser(user);
    }

    public void logoutUser(BackendlessUser user) {
        backendlessClient.logoutUser(user);
        log.info(user.getEmail() + " logged out");
    }

    public BackendlessUserDto getUser(String objectId) {
        return backendlessClient.fetchUserById(objectId);
    }

    public void restorePassword(String email) {
        backendlessClient.restorePassword(email);
        log.info(email + " restored password with backendless generated link");
    }

    public Object deleteUser(String objectId) {
        log.info("Deleting user with id " + objectId);
        return backendlessClient.deleteUser(objectId);
    }
}
