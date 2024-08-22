package com.app.rotatio.service;

import com.app.rotatio.controller.exception.TaskAlreadyExistsException;
import com.app.rotatio.controller.exception.TaskNotFoundException;
import com.app.rotatio.domain.Task;
import com.app.rotatio.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setId(1L);
        task.setName("Sample Task");
        task.setPerformed(false);
    }

    @Test
    void shouldSaveTaskSuccessfully() throws TaskAlreadyExistsException {
        // Given
        when(taskRepository.findAll()).thenReturn(List.of());
        when(taskRepository.save(task)).thenReturn(task);

        // When
        Task savedTask = taskService.saveTask(task);

        // Then
        assertNotNull(savedTask);
        assertEquals(task.getName(), savedTask.getName());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void shouldThrowTaskAlreadyExistsExceptionWhenTaskExists() {
        // Given
        when(taskRepository.findAll()).thenReturn(List.of(task));

        // When & Then
        assertThrows(TaskAlreadyExistsException.class, () -> taskService.saveTask(task));
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void shouldDeleteTaskSuccessfully() throws TaskNotFoundException {
        // Given
        task.setPerformed(true);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskRepository.existsById(task.getId())).thenReturn(true);

        // When
        Task deletedTask = taskService.delete(task);

        // Then
        assertFalse(deletedTask.isPerformed());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void shouldThrowExceptionIfTaskToDeleteNotFound() throws TaskNotFoundException {
        //Given
        Task toDelete = Task.builder().id(2L).name("Sample Task").build();
        //When
        when(taskRepository.existsById(2L)).thenReturn(false);
        assertThrows(TaskNotFoundException.class, () -> taskService.delete(toDelete));
    }

    @Test
    void shouldUpdatePerformedSuccessfully() throws TaskNotFoundException {
        // Given
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        // When
        Task updatedTask = taskService.updatePerformed(1L, true);

        // Then
        assertTrue(updatedTask.isPerformed());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void shouldThrowTaskNotFoundExceptionWhenTaskDoesNotExist() {
        // Given
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(TaskNotFoundException.class, () -> taskService.updatePerformed(1L, true));
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void shouldGetTaskByIdSuccessfully() throws TaskNotFoundException {
        // Given
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // When
        Task foundTask = taskService.getTaskById(1L);

        // Then
        assertNotNull(foundTask);
        assertEquals(task.getName(), foundTask.getName());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowTaskNotFoundExceptionWhenTaskByIdNotFound() {
        // Given
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(1L));
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void shouldGetAllTasksSuccessfully() {
        // Given
        when(taskRepository.findAll()).thenReturn(List.of(task));

        // When
        List<Task> tasks = taskService.getAllTasks();

        // Then
        assertNotNull(tasks);
        assertFalse(tasks.isEmpty());
        assertEquals(1, tasks.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void shouldGetAllTasksByPerformedSuccessfully() {
        // Given
        when(taskRepository.findByPerformed(false)).thenReturn(List.of(task));

        // When
        List<Task> tasks = taskService.getAllTasksByPerformed(false);

        // Then
        assertNotNull(tasks);
        assertFalse(tasks.isEmpty());
        assertEquals(1, tasks.size());
        verify(taskRepository, times(1)).findByPerformed(false);
    }
}