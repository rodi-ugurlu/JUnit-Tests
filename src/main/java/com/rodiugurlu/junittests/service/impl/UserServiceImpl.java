package com.rodiugurlu.junittests.service.impl;

import com.rodiugurlu.junittests.entity.User;
import com.rodiugurlu.junittests.repository.UserRepository;
import com.rodiugurlu.junittests.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    private final UserRepository userRepository;


    @Override
    public User createUser(String email, String name, String password, String role) {
        userRepository.findByEmail(email).ifPresent(existing -> {
            throw new IllegalArgumentException("User with email " + email + " already exists");
        });
        User user = new User(email, name, password, role);
        return userRepository.save(user);
    }

    @Override
    public User getUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with id " + id));
    }

    @Override
    public User getUserByEmail(String email) {
        Optional<User> optional = userRepository.findByEmail(email);
        return optional.get();
    }

    @Override
    public void deleteUser(int id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }
    @Override
    public User changePassword(int userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id " + userId));

        if (!user.getPassword().equals(oldPassword)) {
            throw new IllegalArgumentException("Old password does not match");
        }

        user.setPassword(newPassword);
        return userRepository.save(user);
    }
}
