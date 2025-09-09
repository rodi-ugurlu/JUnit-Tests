package com.rodiugurlu.junittests.service;

import com.rodiugurlu.junittests.entity.Todo;

import java.util.List;

public interface ITodoService {
    Todo create(Todo todo);
    List<Todo> findAll();
    Todo markDone(int id);
    void delete(int id);
}
