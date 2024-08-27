package com.app.rotatio.controller;

import com.app.rotatio.controller.exception.UserNotFoundException;
import com.app.rotatio.domain.BackendlessLoginUser;
import com.app.rotatio.domain.User;
import com.app.rotatio.domain.dto.UserDto;
import com.app.rotatio.domain.dto.backendless.BackendlessLoginUserDto;
import com.app.rotatio.facade.UserFacade;
import com.app.rotatio.mapper.UserMapper;
import com.app.rotatio.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitWebConfig
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserFacade userFacade;

    @MockBean
    private UserMapper userMapper;

    private UserDto userDto;
    private User user;
    private ObjectMapper mapper;
    private String json;
    private List<User> users;
    private List<UserDto> userDtos;

    @BeforeEach
    void setUp() throws Exception {
        userDto = new UserDto(
                1L,
                "John",
                "Doe",
                "john.doe@example.com",
                "password",
                "ENABLED",
                "objectId",
                "userToken",
                new ArrayList<>()
        );
        user = User.builder()
                .id(1L)
                .firstName("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .password("password")
                .build();
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        json = mapper.writeValueAsString(userDto);
        users = List.of(user);
        userDtos = List.of(userDto);
    }

    @Test
    void shouldRegisterUser() throws Exception {
        // Given
        when(userMapper.mapToUser(userDto)).thenReturn(user);
        when(userFacade.registerAndSaveUser(any(User.class))).thenReturn(user);
        when(userMapper.mapToUserDto(any(User.class))).thenReturn(userDto);
        // When & Then
        mockMvc.perform(post("/v1/rotatio/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void shouldLoginUser() throws Exception {
        // Given
        BackendlessLoginUserDto loginUserDto = new BackendlessLoginUserDto("john.doe@example.com", "password");
        BackendlessLoginUser backendlessUser = new BackendlessLoginUser();
        when(userFacade.mapToBackendlessUser(loginUserDto)).thenReturn(backendlessUser);
        when(userFacade.loginUser(any())).thenReturn(user);
        when(userMapper.mapToUserDto(any(User.class))).thenReturn(userDto);
        String loginJson = mapper.writeValueAsString(loginUserDto);
        // When & Then
        mockMvc.perform(post("/v1/rotatio/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        // Given
        user.setFirstName("Mark");
        UserDto updatedUserDto = new UserDto(
                1L,
                "Mark",
                "Doe",
                "john.doe@example.com",
                "password",
                "ENABLED",
                "objectId",
                "userToken",
                new ArrayList<>()
        );
        String updatedJson = mapper.writeValueAsString(updatedUserDto);
        when(userMapper.mapToUser(updatedUserDto)).thenReturn(user);
        when(userFacade.updateUser(any(User.class))).thenReturn(user);
        when(userMapper.mapToUserDto(any(User.class))).thenReturn(updatedUserDto);
        // When & Then
        mockMvc.perform(put("/v1/rotatio/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Mark"));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        // Given
        when(userFacade.deleteUser(anyString())).thenReturn(user);
        when(userMapper.mapToUserDto(any(User.class))).thenReturn(userDto);
        // When & Then
        mockMvc.perform(delete("/v1/rotatio/users/{objectId}", "objectId"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void shouldGetAllUsers() throws Exception {
        // Given
        when(userFacade.getAllUsers()).thenReturn(users);
        when(userMapper.mapToUserDtoList(any())).thenReturn(userDtos);
        // When & Then
        mockMvc.perform(get("/v1/rotatio/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void shouldGetUserById() throws Exception {
        // Given
        when(userFacade.getUserById(anyLong())).thenReturn(user);
        when(userMapper.mapToUserDto(any(User.class))).thenReturn(userDto);
        // When & Then
        mockMvc.perform(get("/v1/rotatio/users/{userId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void shouldGetUserByEmail() throws Exception {
        // Given
        when(userFacade.fetchUserByEmail(anyString())).thenReturn(user);
        when(userMapper.mapToUserDto(any(User.class))).thenReturn(userDto);
        // When & Then
        mockMvc.perform(get("/v1/rotatio/users/byEmail/{email}", "john.doe@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    void shouldLogoutUser() throws Exception {
        // Given
        // When & Then
        mockMvc.perform(get("/v1/rotatio/users/logout/{userId}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRestorePassword() throws Exception {
        // Given
        // When & Then
        mockMvc.perform(get("/v1/rotatio/users/password")
                        .param("email", "john.doe@example.com"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotFoundWhenUserDoesNotExist() throws Exception {
        // Given
        when(userFacade.getUserById(anyLong())).thenThrow(UserNotFoundException.class);
        // When & Then
        mockMvc.perform(get("/v1/rotatio/users/{userId}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnBadRequestWhenInvalidInput() throws Exception {
        // Given
        // When & Then
        mockMvc.perform(post("/v1/rotatio/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldHandleServiceException() throws Exception {
        // Given
        when(userFacade.getUserById(anyLong())).thenThrow(new RuntimeException("Unexpected error"));
        // When & Then
        mockMvc.perform(get("/v1/rotatio/users/{userId}", 1L))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Unexpected error"));
    }
}