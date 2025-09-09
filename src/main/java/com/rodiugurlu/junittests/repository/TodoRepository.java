package com.rodiugurlu.junittests.repository;

import com.rodiugurlu.junittests.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo,Integer> {
}
