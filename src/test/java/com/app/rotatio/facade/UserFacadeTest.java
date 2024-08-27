package com.app.rotatio.facade;

import com.app.rotatio.controller.exception.UserLoginProcessException;
import com.app.rotatio.controller.exception.UserNotFoundException;
import com.app.rotatio.domain.BackendlessLoginUser;
import com.app.rotatio.domain.User;
import com.app.rotatio.domain.dto.backendless.BackendlessLoginUserDto;
import com.app.rotatio.service.UserService;
import com.app.rotatio.validator.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserFacadeTest {

    @Mock
    private UserService userService;

    @Mock
    private UserValidator userValidator;

    @InjectMocks
    private UserFacade userFacade;

    private User user;
    private BackendlessLoginUser loginUser;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setObjectId("12345");
        user.setUserStatus("ENABLED");

        loginUser = BackendlessLoginUser.builder()
                .login("test@example.com")
                .password("password")
                .build();
    }

    @Test
    void shouldRegisterAndSaveUser() {
        // Given
        when(userService.registerAndSaveUser(user)).thenReturn(user);

        // When
        User registeredUser = userFacade.registerAndSaveUser(user);

        // Then
        assertNotNull(registeredUser);
        assertEquals("test@example.com", registeredUser.getEmail());
        verify(userService, times(1)).registerAndSaveUser(user);
    }

    @Test
    void shouldLoginUser() throws UserNotFoundException, UserLoginProcessException {
        // Given
        doNothing().when(userValidator).validateLoginProcess(loginUser);
        when(userService.logAndSaveUser(loginUser)).thenReturn(user);

        // When
        User loggedUser = userFacade.loginUser(loginUser);

        // Then
        assertNotNull(loggedUser);
        assertEquals("test@example.com", loggedUser.getEmail());
        verify(userValidator, times(1)).validateLoginProcess(loginUser);
        verify(userService, times(1)).logAndSaveUser(loginUser);
    }

    @Test
    void shouldLogoutUser() throws UserNotFoundException {
        // Given
        doNothing().when(userValidator).logLogoutProcess(1L);
        doNothing().when(userService).logout(1L);

        // When
        userFacade.logout(1L);

        // Then
        verify(userValidator, times(1)).logLogoutProcess(1L);
        verify(userService, times(1)).logout(1L);
    }

    @Test
    void shouldGetUserById() throws UserNotFoundException {
        // Given
        doNothing().when(userValidator).validateUserExistsById(1L);
        when(userService.getUserById(1L)).thenReturn(user);

        // When
        User foundUser = userFacade.getUserById(1L);

        // Then
        assertNotNull(foundUser);
        assertEquals("test@example.com", foundUser.getEmail());
        verify(userValidator, times(1)).validateUserExistsById(1L);
        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void shouldRestorePasswordByUserMail() throws UserNotFoundException {
        // Given
        doNothing().when(userValidator).validateUserExists("test@example.com");
        doNothing().when(userService).restorePasswordByUserMail("test@example.com");

        // When
        userFacade.restorePasswordByUserMail("test@example.com");

        // Then
        verify(userValidator, times(1)).validateUserExists("test@example.com");
        verify(userService, times(1)).restorePasswordByUserMail("test@example.com");
    }

    @Test
    void shouldDeleteUser() throws UserNotFoundException {
        // Given
        when(userService.delete("12345")).thenReturn(user);

        // When
        User deletedUser = userFacade.deleteUser("12345");

        // Then
        assertNotNull(deletedUser);
        assertEquals("test@example.com", deletedUser.getEmail());
        verify(userService, times(1)).delete("12345");
    }

    @Test
    void shouldUpdateUser() throws UserNotFoundException {
        // Given
        doNothing().when(userValidator).validateUserExistsById(user.getId());
        when(userService.updateUser(user)).thenReturn(user);

        // When
        User updatedUser = userFacade.updateUser(user);

        // Then
        assertNotNull(updatedUser);
        assertEquals("test@example.com", updatedUser.getEmail());
        verify(userValidator, times(1)).validateUserExistsById(user.getId());
        verify(userService, times(1)).updateUser(user);
    }

    @Test
    void shouldGetAllUsers() {
        // Given
        when(userService.getAllUsers()).thenReturn(List.of(user));

        // When
        List<User> allUsers = userFacade.getAllUsers();

        // Then
        assertNotNull(allUsers);
        assertEquals(1, allUsers.size());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void shouldFetchUserByObjectId() throws UserNotFoundException {
        // Given
        when(userService.fetchUserByBackendlessObjectId("12345")).thenReturn(user);

        // When
        User foundUser = userFacade.fetchUserByObjectId("12345");

        // Then
        assertNotNull(foundUser);
        assertEquals("test@example.com", foundUser.getEmail());
        verify(userService, times(1)).fetchUserByBackendlessObjectId("12345");
    }

    @Test
    void shouldMapToBackendlessUser() {
        // Given
        BackendlessLoginUserDto userDto = new BackendlessLoginUserDto("test@example.com", "password");
        when(userService.mapToBackendlessUser(userDto)).thenReturn(loginUser);

        // When
        BackendlessLoginUser mappedUser = userFacade.mapToBackendlessUser(userDto);

        // Then
        assertNotNull(mappedUser);
        assertEquals("test@example.com", mappedUser.getLogin());
        verify(userService, times(1)).mapToBackendlessUser(userDto);
    }

    @Test
    void shouldFetchUserByEmail() throws UserNotFoundException {
        // Given
        when(userService.getUserByEmail("test@example.com")).thenReturn(user);

        // When
        User foundUser = userFacade.fetchUserByEmail("test@example.com");

        // Then
        assertNotNull(foundUser);
        assertEquals("test@example.com", foundUser.getEmail());
        verify(userService, times(1)).getUserByEmail("test@example.com");
    }
}