package com.app.rotatio.mapper;

import com.app.rotatio.domain.BackendlessUser;
import com.app.rotatio.domain.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class BackendlessMapper {

    public User mapBackendlessToUser(BackendlessUser backendlessUser) {
        return User.builder()
                .id(backendlessUser.getId())
                .firstName(backendlessUser.getFirstName())
                .lastname(backendlessUser.getLastname())
                .email(backendlessUser.getEmail())
                .password(backendlessUser.getPassword())
                .objectId(backendlessUser.getObjectId())
                .userStatus(backendlessUser.getUserStatus())
                .userToken(backendlessUser.getUserToken() != null ? backendlessUser.getUserToken() : null)
                .plannedDays(new ArrayList<>())
                .build();
    }

    public BackendlessUser mapToBackendlessUser(User user) {
        return BackendlessUser.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .password(user.getPassword())
                .objectId(user.getObjectId())
                .userToken(user.getUserToken() != null ? user.getUserToken() : null)
                .userStatus(user.getUserStatus())
                .build();
    }
}
