package com.app.rotatio.service;

import com.app.rotatio.controller.exception.UserLoginProcessException;
import com.app.rotatio.controller.exception.UserNotFoundException;
import com.app.rotatio.domain.BackendlessLoginUser;
import com.app.rotatio.domain.BackendlessUser;
import com.app.rotatio.domain.User;
import com.app.rotatio.domain.dto.backendless.BackendlessLoginUserDto;
import com.app.rotatio.mapper.BackendlessMapper;
import com.app.rotatio.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BackendlessService backendlessService;
    private final BackendlessMapper mapper;

    public User saveUser(final User user) {
        return userRepository.save(user);
    }

    public User registerAndSaveUser(final User user) {
        BackendlessUser backendlessUser = mapper.mapToBackendlessUser(user);
        BackendlessUser registeredUser = backendlessService.registerUser(backendlessUser);
        return saveUser(mapper.mapBackendlessToUser(registeredUser));
    }

    public User delete(final String objectId) throws UserNotFoundException {
        User user = userRepository.findByObjectId(objectId).orElseThrow(UserNotFoundException::new);
        user.setUserStatus("DELETED ON BACKENDLESS");
        backendlessService.deleteUser(objectId);
        return userRepository.save(user);
    }

    public User getUserById(final Long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public User fetchUserByBackendlessObjectId(final String objectId) throws UserNotFoundException {
        BackendlessUser backendlessUser = backendlessService.getUser(objectId);
        return getUserByEmail(backendlessUser.getEmail());
    }

    public User logAndSaveUser(final BackendlessLoginUser loginUser) throws UserNotFoundException, UserLoginProcessException {
        BackendlessUser backendlessUser = backendlessService.loginUser(loginUser);
        User userByEmail = getUserByEmail(backendlessUser.getEmail());
        userByEmail.setUserToken(backendlessUser.getUserToken());
        return saveUser(userByEmail);
    }

    public BackendlessLoginUser mapToBackendlessUser(final BackendlessLoginUserDto userDto) {
        return backendlessService.mapToLoginUser(userDto);
    }

    public void logout(final Long id) throws UserNotFoundException {
        User user = getUserById(id);
        BackendlessUser backendlessUser = mapper.mapToBackendlessUser(user);
        backendlessService.logoutUser(backendlessUser);
        user.setUserToken(null);
        userRepository.save(user);
    }

    public void restorePasswordByUserMail(final String email) throws UserNotFoundException {
        backendlessService.restorePassword(email);
    }

    public User getUserByEmail(final String email) throws UserNotFoundException {
        try {
            return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        } catch (Exception e) {
            throw new UserNotFoundException();
        }
    }

    public User updateUser(final User user) throws UserNotFoundException {
        if (userRepository.existsById(user.getId())) {
            BackendlessUser updatedBackendless = backendlessService.update(mapper.mapToBackendlessUser(user));
            User updatedUser = mapper.mapBackendlessToUser(updatedBackendless);
            return userRepository.save(updatedUser);
        } else {
            throw new UserNotFoundException();
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostConstruct
    void verifyUserTokensAndObservers() {
        List<User> users = getAllUsers();
        if (!users.isEmpty()) {
            for (User user : users) {
                if (user.getUserStatus().equals("ENABLED")) {
                    BackendlessUser backendlessUser = backendlessService.getUser(user.getObjectId());
                    if (user.getUserToken() == null || !user.getUserToken().equals(backendlessUser.getUserToken())) {
                        user.setUserToken(backendlessUser.getUserToken());
                        saveUser(user);
                    }
                }
            }
            log.info("Database users tokens updated with Backendless server");
        }
    }
}
