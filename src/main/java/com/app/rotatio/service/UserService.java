package com.app.rotatio.service;

import com.app.rotatio.controller.exception.UserNotFoundException;
import com.app.rotatio.domain.BackendlessUser;
import com.app.rotatio.domain.User;
import com.app.rotatio.domain.dto.backendless.BackendlessUserDto;
import com.app.rotatio.domain.dto.backendless.BackendlessUserToLoginDto;
import com.app.rotatio.domain.dto.backendless.BackendlessUserToRegisterDto;
import com.app.rotatio.mapper.BackendlessMapper;
import com.app.rotatio.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
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

    public User registerAndSaveUser(final BackendlessUserToRegisterDto userDto) {
        BackendlessUserDto backendlessUserDto = backendlessService.registerUser(userDto);
        User user = mapper.mapBackendlessToUser(backendlessUserDto);
        return saveUser(user);
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
        BackendlessUserDto userDto = backendlessService.getUser(objectId);
        return getUserByEmail(userDto.email());
    }

    public User logAndSaveUser(final BackendlessUserToLoginDto userToLoginDto) throws UserNotFoundException {
        BackendlessUserDto backendlessUserDto = backendlessService.loginUser(userToLoginDto);
        User userByEmail = getUserByEmail(backendlessUserDto.email());
        userByEmail.setUserToken(backendlessUserDto.userToken());
        return saveUser(userByEmail);
    }

    public void logout(final Long id) throws UserNotFoundException {
        User user = getUserById(id);
        BackendlessUser backendlessUser = mapper.mapToBackendlessUser(user);
        backendlessService.logoutUser(backendlessUser);
        user.setUserToken(null);
        userRepository.save(user);
    }

    public void restorePasswordByUserMail(final Long id) throws UserNotFoundException {
        User userByEmail = getUserById(id);
        backendlessService.restorePassword(userByEmail.getEmail());
    }

    public User getUserByEmail(final String email) throws UserNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostConstruct
    private void verifyUserTokens() {
        List<User> users = getAllUsers();
        if (!users.isEmpty()) {
            for (User user : users) {
                BackendlessUserDto userDto = backendlessService.getUser(user.getObjectId());
                if (user.getUserToken() == null || !user.getUserToken().equals(userDto.userToken())) {
                    user.setUserToken(userDto.userToken());
                    saveUser(user);
                }
            }
        }
        log.info("Database users tokens updated with Backendless server");
    }
}
