package com.app.rotatio.service;

import com.app.rotatio.domain.User;
import com.app.rotatio.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    public User saveUser(final User user) {
        return userRepository.save(user);
    }

    public User delete(final User user) {
        user.setEnabled(false);
        return userRepository.save(user);
    }

    public Optional<User> getUserById(final Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
