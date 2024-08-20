package com.app.rotatio.domain;

import com.app.rotatio.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TaskEntityTests {

    @Autowired
    private TaskRepository taskRepository;

    private Task task;

    @BeforeEach
    void setUp() {
        task = Task.builder()
                .name("Test task")
                .description("Test description")
                .performed(true)
                .build();
    }

    @Nested
    class TasksRepositorySimpleTests {

        @Test
        void shouldCreateTask() {
            //Given
            Long taskId = task.getId();
            //When
            taskRepository.save(task);
            //Then
            assertNotNull(task.getId());
            assertNotEquals(taskId, task.getId());
            assertTrue(taskRepository.findById(task.getId()).isPresent());
        }

        @Test
        void shouldThrowException() {
            //Given
            taskRepository.save(task);
            Task secondTask = Task.builder()
                    .name("Test task")
                    .build();

            //When&Then
            assertThrows(Exception.class, () -> taskRepository.saveAndFlush(secondTask));
        }

        @Test
        void shouldUpdateTask() {
            //Given
            taskRepository.save(task);
            task.setDescription("Updated description");
            task.setName("Updated name");
            task.setPerformed(false);
            //When
            Task savedTask = taskRepository.save(task);
            //Then
            assertEquals("Updated description", savedTask.getDescription());
            assertEquals("Updated name", savedTask.getName());
            assertFalse(savedTask.isPerformed());
        }

        @Test
        void shouldFetchTaskById() {
            //Given
            taskRepository.save(task);
            Long taskId = task.getId();
            //When
            Task taskById = taskRepository.findById(taskId).orElseThrow();
            //Then
            assertEquals(taskId, taskById.getId());
            assertNotNull(taskById);
            assertTrue(taskById.isPerformed());
        }

        @Test
        void shouldFetchAllTasks() {
            //Given
            Task secondTask = Task.builder()
                    .name("Second task")
                    .build();
            taskRepository.save(task);
            taskRepository.save(secondTask);
            //When
            List<Task> tasks = taskRepository.findAll();
            //Then
            assertEquals(2, tasks.size());
            assertTrue(tasks.contains(task));
            assertTrue(tasks.contains(secondTask));
        }

        @Test
        void shouldDeleteTask() {
            //Given
            taskRepository.save(task);
            Long taskId = task.getId();
            //When
            taskRepository.delete(task);
            //Then
            Optional<Task> taskById = taskRepository.findById(taskId);
            assertFalse(taskById.isPresent());
        }
    }

    @Nested
    class OverrideMethodsTests {

        @Test
        void shouldEqualsTasksPositive() {
            //Given
            Task saved = taskRepository.save(task);
            //When
            boolean equals = task.equals(saved);
            //Then
            assertTrue(equals);
        }

        @Test
        void shouldEqualsTasksNegative() {
            //Given
            taskRepository.save(task);
            Task otherTask = taskRepository.save(Task.builder()
                    .name("other task").build());
            //When
            boolean equals = task.equals(otherTask);
            //Then
            assertFalse(equals);
        }

        @Test
        void shouldReturnSameHashCodes() {
            //Given
            Task saved = taskRepository.save(task);
            Task otherTask = saved;
            //When
            int savedHash = saved.hashCode();
            int otherTaskHash = otherTask.hashCode();
            //Then
            assertEquals(savedHash, otherTaskHash);
            assertEquals(otherTask, saved);
        }

        @Test
        void shouldReturnOtherHashCodes() {
            //Given
            Task saved = taskRepository.save(task);
            Task otherTaskSaved = taskRepository.save(Task.builder()
                    .name("other task").build());
            //When
            int savedHash = saved.hashCode();
            int otherTaskHash = otherTaskSaved.hashCode();
            //Then
            assertNotEquals(savedHash, otherTaskHash);
        }
    }
}