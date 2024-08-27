package com.app.rotatio.mapper;

import com.app.rotatio.domain.BackendlessLoginUser;
import com.app.rotatio.domain.BackendlessUser;
import com.app.rotatio.domain.User;
import com.app.rotatio.domain.dto.backendless.BackendlessLoginUserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BackendlessMapperTest {

    @Autowired
    private BackendlessMapper backendlessMapper;

    private BackendlessUser backendlessUser;
    private User user;
    private BackendlessLoginUserDto backendlessLoginUserDto;

    @BeforeEach
    public void setUp() {
        backendlessUser = BackendlessUser.builder()
                .id(1L)
                .firstName("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .password("password")
                .objectId("objectId")
                .userStatus("active")
                .userToken("token")
                .build();

        user = User.builder()
                .id(1L)
                .firstName("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .password("password")
                .objectId("objectId")
                .userStatus("active")
                .userToken("token")
                .plannedDays(new ArrayList<>())
                .build();

        backendlessLoginUserDto = new BackendlessLoginUserDto("john.doe@example.com", "password");
    }

    @Test
    void shouldMapBackendlessToUser() {
        // When
        User result = backendlessMapper.mapBackendlessToUser(backendlessUser);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(backendlessUser.getId());
        assertThat(result.getFirstName()).isEqualTo(backendlessUser.getFirstName());
        assertThat(result.getLastname()).isEqualTo(backendlessUser.getLastname());
        assertThat(result.getEmail()).isEqualTo(backendlessUser.getEmail());
        assertThat(result.getPassword()).isEqualTo(backendlessUser.getPassword());
        assertThat(result.getObjectId()).isEqualTo(backendlessUser.getObjectId());
        assertThat(result.getUserStatus()).isEqualTo(backendlessUser.getUserStatus());
        assertThat(result.getUserToken()).isEqualTo(backendlessUser.getUserToken());
        assertThat(result.getPlannedDays()).isEmpty();
    }

    @Test
    void shouldMapToBackendlessUser() {
        // When
        BackendlessUser result = backendlessMapper.mapToBackendlessUser(user);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(user.getId());
        assertThat(result.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(result.getLastname()).isEqualTo(user.getLastname());
        assertThat(result.getEmail()).isEqualTo(user.getEmail());
        assertThat(result.getPassword()).isEqualTo(user.getPassword());
        assertThat(result.getObjectId()).isEqualTo(user.getObjectId());
        assertThat(result.getUserStatus()).isEqualTo(user.getUserStatus());
        assertThat(result.getUserToken()).isEqualTo(user.getUserToken());
    }
}