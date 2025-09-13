package com.rodiugurlu.junittests.service;

import com.rodiugurlu.junittests.entity.User;

import java.util.UUID;

public interface IUserService {
    User createUser(String email, String name, String password, String role);
    User getUserById(int id);
    User getUserByEmail(String email);
    void deleteUser(int id);
    User changePassword(int userId, String oldPassword, String newPassword);

}
