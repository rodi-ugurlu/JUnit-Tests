package com.rodiugurlu.junittests.service;

import com.rodiugurlu.junittests.entity.Todo;
import com.rodiugurlu.junittests.exception.NotFoundException;
import com.rodiugurlu.junittests.repository.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceImplTest {
    @Mock
    private TodoRepository todoRepository;
    @InjectMocks
    private TodoServiceImpl todoService;
    private Todo testTodo;
    private Todo testTodo2;

    @BeforeEach
    void setUp() {
        testTodo = new Todo(1, "Alışveriş yap", false);
        testTodo2 = new Todo(2, "Kitap oku", true);
    }

    @Test
    @DisplayName("Yeni todo başarıyla oluşturulmalı")
    void create_ShouldReturnSavedTodo() {
        //GIVEN
        when(todoRepository.save(any(Todo.class))).thenReturn(testTodo);

        //WHEN
        Todo result = todoService.create(testTodo);

        assertNotNull(result);
        assertEquals(testTodo.getId(),result.getId());
        assertEquals("Alışveriş yap",result.getTitle());
        verify(todoRepository, times(1)).save(any(Todo.class));

    }

    // 2. TEST: findAll() metodu testi
    @Test
    @DisplayName("Tüm todolar başarıyla getirilmeli")
    void findAll_ShouldReturnAllTodos() {
        // GIVEN
        List<Todo> todoList = Arrays.asList(testTodo, testTodo2);
        when(todoRepository.findAll()).thenReturn(todoList);

        // WHEN
        List<Todo> result = todoService.findAll();

        // THEN
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(todoRepository, times(1)).findAll(); // 1 kere çağrıldı mı?
    }

    // 3. TEST: markDone() metodu - Başarılı senaryo
    @Test
    @DisplayName("Todo başarıyla tamamlandı olarak işaretlenmeli")
    void markDone_ExistingId_ShouldMarkAsDone() {
        // GIVEN
        when(todoRepository.findById(1)).thenReturn(Optional.of(testTodo));
        when(todoRepository.save(any(Todo.class))).thenReturn(testTodo);

        // WHEN
        Todo result = todoService.markDone(1);

        // THEN
        assertTrue(result.isDone()); // Done true olmalı
        verify(todoRepository, times(1)).findById(1); // findById çağrıldı mı?
        verify(todoRepository, times(1)).save(testTodo); // save çağrıldı mı?
    }

    // 4. TEST: markDone() metodu - Hata senaryosu
    @Test
    @DisplayName("Varolmayan todo tamamlanmaya çalışılırsa hata fırlatmalı")
    void markDone_NonExistingId_ShouldThrowException() {
        // GIVEN
        when(todoRepository.findById(999)).thenReturn(Optional.empty());

        // WHEN & THEN
        NotFoundException exception = assertThrows(NotFoundException.class, () -> todoService.markDone(999));

        assertEquals("Todo not found: 999", exception.getMessage());
        verify(todoRepository, times(1)).findById(999);
        verify(todoRepository, never()).save(any()); // save hiç çağrılmamalı
    }

    // 5. TEST: delete() metodu - Başarılı senaryo
    @Test
    @DisplayName("Todo başarıyla silinmeli")
    void delete_ExistingId_ShouldDeleteTodo() {
        // GIVEN
        when(todoRepository.existsById(1)).thenReturn(true);
        doNothing().when(todoRepository).deleteById(1);

        // WHEN & THEN
        assertDoesNotThrow(() -> todoService.delete(1)); // Hata fırlatmamalı

        verify(todoRepository, times(1)).existsById(1);
        verify(todoRepository, times(1)).deleteById(1);
    }

    // 6. TEST: delete() metodu - Hata senaryosu
    @Test
    @DisplayName("Varolmayan todo silinmeye çalışılırsa hata fırlatmalı")
    void delete_NonExistingId_ShouldThrowException() {
        // GIVEN
        when(todoRepository.existsById(999)).thenReturn(false);

        // WHEN & THEN
        NotFoundException exception = assertThrows(NotFoundException.class, () -> todoService.delete(999));

        assertEquals("Todo not found: 999", exception.getMessage());
        verify(todoRepository, times(1)).existsById(999);
        verify(todoRepository, never()).deleteById(anyInt()); // delete hiç çağrılmamalı
    }
}