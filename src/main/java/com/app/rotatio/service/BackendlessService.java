package com.app.rotatio.service;

import com.app.rotatio.api.backendless.client.BackendlessClient;
import com.app.rotatio.controller.exception.UserLoginProcessException;
import com.app.rotatio.domain.BackendlessLoginUser;
import com.app.rotatio.domain.BackendlessUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BackendlessService {

    private final BackendlessClient backendlessClient;

    public BackendlessUser registerUser(BackendlessUser user) {
        log.info("Registering user " + user.getEmail());
        return backendlessClient.registerUser(user);
    }

    public BackendlessUser loginUser(BackendlessLoginUser user) throws UserLoginProcessException {
        log.info("Login user " + user.getLogin());
        return backendlessClient.loginUser(user);
    }

    public void logoutUser(BackendlessUser user) {
        backendlessClient.logoutUser(user);
        log.info(user.getEmail() + " logged out");
    }

    public BackendlessUser getUser(String objectId) {
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

    public BackendlessUser update(BackendlessUser user) {
        log.info("Updating user " + user.getEmail());
        return backendlessClient.update(user);
    }


}
