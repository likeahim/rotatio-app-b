package com.app.rotatio.service;

import com.app.rotatio.controller.exception.UserNotFoundException;
import com.app.rotatio.domain.User;
import com.app.rotatio.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User saveUser(final User user) {
        return userRepository.save(user);
    }

    public User delete(final User user) {
        user.setUserStatus("DISABLED");
        return userRepository.save(user);
    }

    public User getUserById(final Long id) throws UserNotFoundException {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    public Optional<User> getUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
