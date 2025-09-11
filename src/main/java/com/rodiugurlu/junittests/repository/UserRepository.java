package com.rodiugurlu.junittests.repository;

import com.rodiugurlu.junittests.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
}
