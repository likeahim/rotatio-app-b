package com.app.rotatio.validator;

import com.app.rotatio.controller.exception.UserLoginProcessException;
import com.app.rotatio.controller.exception.UserNotFoundException;
import com.app.rotatio.domain.BackendlessLoginUser;
import com.app.rotatio.domain.User;
import com.app.rotatio.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserValidatorTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserValidator userValidator;

    @Test
    void validateUserExists_shouldThrowUserNotFoundExceptionWhenUserNotFound() {
        // Given
        String email = "test@example.com";
        when(repository.findByEmail(email)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(UserNotFoundException.class, () -> userValidator.validateUserExists(email));
        verify(repository, times(1)).findByEmail(email);
    }

    @Test
    void validateUserExists_shouldNotThrowWhenUserExists() {
        // Given
        String email = "test@example.com";
        User user = new User();
        when(repository.findByEmail(email)).thenReturn(Optional.of(user));

        // When / Then
        assertDoesNotThrow(() -> userValidator.validateUserExists(email));
        verify(repository, times(1)).findByEmail(email);
    }

    @Test
    void validateLoginProcess_shouldThrowUserLoginProcessExceptionWhenLoginIsNull() {
        // Given
        BackendlessLoginUser loginUser = new BackendlessLoginUser(null, "password");

        // When / Then
        assertThrows(UserLoginProcessException.class, () -> userValidator.validateLoginProcess(loginUser));
    }

    @Test
    void validateLoginProcess_shouldThrowUserLoginProcessExceptionWhenPasswordIsNull() {
        // Given
        BackendlessLoginUser loginUser = new BackendlessLoginUser("login@example.com", null);

        // When / Then
        assertThrows(UserLoginProcessException.class, () -> userValidator.validateLoginProcess(loginUser));
    }

    @Test
    void validateLoginProcess_shouldNotThrowWhenLoginAndPasswordAreValid() {
        // Given
        BackendlessLoginUser loginUser = new BackendlessLoginUser("login@example.com", "password");

        // When / Then
        assertDoesNotThrow(() -> userValidator.validateLoginProcess(loginUser));
    }

    @Test
    void logLogoutProcess_shouldThrowUserNotFoundExceptionWhenUserNotFound() {
        // Given
        Long userId = 1L;
        when(repository.findById(userId)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(UserNotFoundException.class, () -> userValidator.logLogoutProcess(userId));
        verify(repository, times(1)).findById(userId);
    }

    @Test
    void logLogoutProcess_shouldLogInfoWhenUserExists() throws UserNotFoundException {
        // Given
        Long userId = 1L;
        User user = new User();
        user.setEmail("test@example.com");
        when(repository.findById(userId)).thenReturn(Optional.of(user));

        // When
        userValidator.logLogoutProcess(userId);

        // Then
        verify(repository, times(1)).findById(userId);
    }

    @Test
    void validateUserExistsById_shouldThrowUserNotFoundExceptionWhenUserNotFound() {
        // Given
        Long userId = 1L;
        when(repository.existsById(userId)).thenReturn(false);

        // When / Then
        assertThrows(UserNotFoundException.class, () -> userValidator.validateUserExistsById(userId));
        verify(repository, times(1)).existsById(userId);
    }

    @Test
    void validateUserExistsById_shouldNotThrowWhenUserExists() {
        // Given
        Long userId = 1L;
        when(repository.existsById(userId)).thenReturn(true);

        // When / Then
        assertDoesNotThrow(() -> userValidator.validateUserExistsById(userId));
        verify(repository, times(1)).existsById(userId);
    }
}