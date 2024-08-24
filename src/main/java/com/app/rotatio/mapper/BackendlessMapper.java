package com.app.rotatio.mapper;

import com.app.rotatio.domain.BackendlessLoginUser;
import com.app.rotatio.domain.BackendlessUser;
import com.app.rotatio.domain.User;
import com.app.rotatio.domain.dto.backendless.BackendlessLoginUserDto;
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

    public BackendlessLoginUser mapToLoginUser(BackendlessLoginUserDto userDto) {
        return BackendlessLoginUser.builder()
                .login(userDto.login())
                .password(userDto.password())
                .build();
    }

    public BackendlessLoginUserDto mapToLoginUserDto(BackendlessLoginUser loginUser) {
        return new BackendlessLoginUserDto(
                loginUser.getLogin(),
                loginUser.getPassword()
        );
    }
}
