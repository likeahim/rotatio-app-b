package com.app.rotatio.mapper;

import com.app.rotatio.domain.BackendlessUser;
import com.app.rotatio.domain.User;
import com.app.rotatio.domain.dto.backendless.BackendlessUserDto;
import com.app.rotatio.domain.dto.backendless.BackendlessUserToRegisterDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class BackendlessMapper {

    public User mapBackendlessToUser(BackendlessUserDto backendlessUserDto) {
        return User.builder()
                .firstName(backendlessUserDto.firstName())
                .lastname(backendlessUserDto.lastName())
                .email(backendlessUserDto.email())
                .objectId(backendlessUserDto.objectId())
                .userStatus(backendlessUserDto.userStatus())
                .userToken(backendlessUserDto.userToken() != null ? backendlessUserDto.userToken() : null)
                .plannedDays(new ArrayList<>())
                .build();
    }

    public BackendlessUser mapToBackendlessUser(User user) {
        return BackendlessUser.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastname())
                .email(user.getEmail())
                .objectId(user.getObjectId())
                .userToken(user.getUserToken() != null ? user.getUserToken() : null)
                .userStatus(user.getUserStatus())
                .build();
    }
}
