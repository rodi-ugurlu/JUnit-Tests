package com.rodiugurlu.junittests.controller;

import com.rodiugurlu.junittests.entity.Todo;
import com.rodiugurlu.junittests.service.ITodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TodoController {
    private final ITodoService service;

    @GetMapping
    public List<Todo> list() { return service.findAll(); }

    @PostMapping
    public ResponseEntity<Todo> create(@RequestBody Todo todo) {
        Todo saved = service.create(todo);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping("/{id}/done")
    public Todo markDone(@PathVariable int id) { return service.markDone(id); }
}
