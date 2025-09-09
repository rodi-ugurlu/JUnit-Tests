package com.rodiugurlu.junittests.service;

import com.rodiugurlu.junittests.entity.Todo;
import com.rodiugurlu.junittests.exception.NotFoundException;
import com.rodiugurlu.junittests.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements ITodoService {
    private final TodoRepository repo;

    @Override
    public Todo create(Todo todo) {
        return repo.save(todo);
    }

    @Override
    public List<Todo> findAll() {
        return repo.findAll();
    }

    @Override
    public Todo markDone(int id) {
        Todo t = repo.findById(id).orElseThrow(() -> new NotFoundException("Todo not found: " + id));
        t.setDone(true);
        return repo.save(t);
    }

    @Override
    public void delete(int id) {
        if (!repo.existsById(id)) throw new NotFoundException("Todo not found: " + id);
        repo.deleteById(id);
    }
}
