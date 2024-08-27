package com.app.rotatio.service;

import com.app.rotatio.controller.exception.UserLoginProcessException;
import com.app.rotatio.controller.exception.UserNotFoundException;
import com.app.rotatio.domain.BackendlessLoginUser;
import com.app.rotatio.domain.BackendlessUser;
import com.app.rotatio.domain.User;
import com.app.rotatio.mapper.BackendlessMapper;
import com.app.rotatio.repository.UserRepository;
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
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BackendlessService backendlessService;

    @Mock
    private BackendlessMapper mapper;

    @InjectMocks
    private UserService userService;

    private User user;
    private BackendlessUser backendlessUser;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setObjectId("12345");
        user.setUserStatus("ENABLED");

        backendlessUser = BackendlessUser.builder()
                .email("test@example.com")
                .password("password")
                .build();
    }

    @Test
    void shouldSaveUser() {
        //Given
        when(userRepository.save(user)).thenReturn(user);
        //When
        User savedUser = userService.saveUser(user);
        //Then
        assertNotNull(savedUser);
        assertEquals("test@example.com", savedUser.getEmail());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void shouldRegisterAndSaveUser() {
        //Given
        when(mapper.mapToBackendlessUser(user)).thenReturn(backendlessUser);
        when(backendlessService.registerUser(backendlessUser)).thenReturn(backendlessUser);
        when(mapper.mapBackendlessToUser(backendlessUser)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        //When
        User registeredUser = userService.registerAndSaveUser(user);
        //Then
        assertNotNull(registeredUser);
        assertEquals("test@example.com", registeredUser.getEmail());
        verify(mapper, times(1)).mapToBackendlessUser(user);
        verify(backendlessService, times(1)).registerUser(backendlessUser);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void shouldDeleteUser() throws UserNotFoundException {
        //Given
        when(userRepository.findByObjectId("12345")).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        //When
        User deletedUser = userService.delete("12345");
        //Then
        assertNotNull(deletedUser);
        assertEquals("DELETED ON BACKENDLESS", deletedUser.getUserStatus());
        verify(backendlessService, times(1)).deleteUser("12345");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingUser() {
        //Given
        when(userRepository.findByObjectId("invalid")).thenReturn(Optional.empty());

        //When
        //Then
        assertThrows(UserNotFoundException.class, () -> userService.delete("invalid"));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldGetUserById() throws UserNotFoundException {
        //Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        //When
        User foundUser = userService.getUserById(1L);
        //Then
        assertNotNull(foundUser);
        assertEquals("test@example.com", foundUser.getEmail());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundById() {
        //Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        //When
        //Then
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    void shouldFetchUserByBackendlessObjectId() throws UserNotFoundException {
        //Given
        when(backendlessService.getUser("12345")).thenReturn(backendlessUser);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        //When
        User foundUser = userService.fetchUserByBackendlessObjectId("12345");
        //Then
        assertNotNull(foundUser);
        assertEquals("test@example.com", foundUser.getEmail());
    }

    @Test
    void shouldLogAndSaveUser() throws UserNotFoundException, UserLoginProcessException {
        //Given
        BackendlessLoginUser loginUser = BackendlessLoginUser.builder()
                .login("test@example.com")
                .password("password")
                .build();
        BackendlessUser loggingUser = BackendlessUser.builder()
                .email("test@example.com")
                .userToken("user-token")
                .build();
        when(backendlessService.loginUser(loginUser)).thenReturn(loggingUser);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        //When
        User loggedUser = userService.logAndSaveUser(loginUser);
        //Then
        assertNotNull(loggedUser);
        assertEquals("user-token", loggedUser.getUserToken());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void shouldLogoutUser() throws UserNotFoundException {
        //Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(mapper.mapToBackendlessUser(user)).thenReturn(backendlessUser);
        //When
        userService.logout(1L);
        //Then
        verify(backendlessService, times(1)).logoutUser(backendlessUser);
        verify(userRepository, times(1)).save(user);
        assertNull(user.getUserToken());
    }

    @Test
    void shouldRestorePasswordByUserMail() throws UserNotFoundException {
        //Given
        //When
        userService.restorePasswordByUserMail(backendlessUser.getEmail());
        //Then
        verify(backendlessService, times(1)).restorePassword("test@example.com");
    }

    @Test
    void shouldGetUserByEmail() throws UserNotFoundException {
        //Given
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        //When
        User foundUser = userService.getUserByEmail("test@example.com");
        //Then
        assertNotNull(foundUser);
        assertEquals("test@example.com", foundUser.getEmail());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFoundByEmail() {
        //Given
        when(userRepository.findByEmail("invalid@example.com")).thenReturn(Optional.empty());

        //When
        //Then
        assertThrows(UserNotFoundException.class, () -> userService.getUserByEmail("invalid@example.com"));
    }

    @Test
    void shouldVerifyUserTokensAndObservers() {
        //Given
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(backendlessService.getUser(user.getObjectId())).thenReturn(backendlessUser);
        when(userRepository.save(user)).thenReturn(user);
        //When
        userService.verifyUserTokensAndObservers();
        //Then
        assertEquals(null, user.getUserToken());
        verify(userRepository, times(1)).save(user);
    }
}