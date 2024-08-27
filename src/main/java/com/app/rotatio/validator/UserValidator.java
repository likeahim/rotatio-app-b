package com.app.rotatio.validator;

import com.app.rotatio.controller.exception.UserLoginProcessException;
import com.app.rotatio.controller.exception.UserNotFoundException;
import com.app.rotatio.domain.BackendlessLoginUser;
import com.app.rotatio.domain.User;
import com.app.rotatio.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository repository;

    public void validateUserExists(String email) throws UserNotFoundException {
        if (repository.findByEmail(email).isEmpty()) {
            throw new UserNotFoundException();
        }
    }

    public void validateLoginProcess(BackendlessLoginUser loginUser) throws UserLoginProcessException {
        log.info("Starting login process for user with email: {}", loginUser.getLogin());

        if (loginUser.getLogin() == null || loginUser.getLogin().isEmpty()) {
            log.error("Login process failed for user with email: {}. Reason: Username cannot be null or empty", loginUser.getLogin());
            throw new UserLoginProcessException();
        }
        if (loginUser.getPassword() == null || loginUser.getPassword().isEmpty()) {
            log.error("Login process failed for user with email: {}. Reason: Password cannot be null or empty", loginUser.getLogin());
            throw new UserLoginProcessException();
        }
    }

    public void logLogoutProcess(Long userId) throws UserNotFoundException {
        Optional<User> user = repository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
        log.info("Starting logout process for user with email: {}", user.get().getEmail());
    }

    public void validateUserExistsById(Long id) throws UserNotFoundException {
        if (!repository.existsById(id)) {
            throw new UserNotFoundException();
        }
    }
}
