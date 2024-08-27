package com.app.rotatio.facade;

import com.app.rotatio.controller.exception.UserLoginProcessException;
import com.app.rotatio.controller.exception.UserNotFoundException;
import com.app.rotatio.domain.BackendlessLoginUser;
import com.app.rotatio.domain.User;
import com.app.rotatio.domain.dto.backendless.BackendlessLoginUserDto;
import com.app.rotatio.service.UserService;
import com.app.rotatio.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserFacade {

    private final UserService userService;
    private final UserValidator userValidator;

    public User saveUser(User user) {
        return userService.saveUser(user);
    }

    public User registerAndSaveUser(User user) {
        return userService.registerAndSaveUser(user);
    }

    public User loginUser(BackendlessLoginUser loginUser) throws UserNotFoundException, UserLoginProcessException {
        userValidator.validateLoginProcess(loginUser);
        return userService.logAndSaveUser(loginUser);
    }

    public void logout(Long id) throws UserNotFoundException {
        userValidator.logLogoutProcess(id);
        userService.logout(id);
    }

    public User getUserById(Long id) throws UserNotFoundException {
        userValidator.validateUserExistsById(id);
        return userService.getUserById(id);
    }

    public void restorePasswordByUserMail(String email) throws UserNotFoundException {
        userValidator.validateUserExists(email);
        userService.restorePasswordByUserMail(email);
    }

    public User deleteUser(String objectId) throws UserNotFoundException {
        return userService.delete(objectId);
    }

    public User updateUser(User user) throws UserNotFoundException {
        userValidator.validateUserExistsById(user.getId());
        return userService.updateUser(user);
    }

    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    public User fetchUserByObjectId(String objectId) throws UserNotFoundException {
        return userService.fetchUserByBackendlessObjectId(objectId);
    }

    public BackendlessLoginUser mapToBackendlessUser(final BackendlessLoginUserDto userDto) {
        return userService.mapToBackendlessUser(userDto);
    }

    public User fetchUserByEmail(String email) throws UserNotFoundException {
        return userService.getUserByEmail(email);
    }
}
