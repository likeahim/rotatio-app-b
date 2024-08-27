package com.app.rotatio.mapper;

import com.app.rotatio.controller.exception.WorkingDayNotFoundException;
import com.app.rotatio.domain.Task;
import com.app.rotatio.domain.dto.TaskDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskMapperTest {

    private final TaskMapper taskMapper = new TaskMapper();

    @Test
    void shouldMapToTaskDto() {
        // Given
        Task task = Task.builder()
                .id(1L)
                .name("Sample Task")
                .description("Sample Description")
                .performed(true)
                .build();

        // When
        TaskDto taskDto = taskMapper.mapToTaskDto(task);

        // Then
        assertNotNull(taskDto);
        assertEquals(1L, taskDto.id());
        assertEquals("Sample Task", taskDto.name());
        assertEquals("Sample Description", taskDto.description());
        assertTrue(taskDto.isPerformed());
    }

    @Test
    void shouldMapToTaskDtoWithNullDescription() {
        // Given
        Task task = Task.builder()
                .id(1L)
                .name("Sample Task")
                .description(null)  // Null description
                .performed(true)
                .build();

        // When
        TaskDto taskDto = taskMapper.mapToTaskDto(task);

        // Then
        assertNotNull(taskDto);
        assertEquals(1L, taskDto.id());
        assertEquals("Sample Task", taskDto.name());
        assertEquals("", taskDto.description());  // Default to empty string
        assertTrue(taskDto.isPerformed());
    }

    @Test
    void shouldMapToTask() throws WorkingDayNotFoundException {
        // Given
        TaskDto taskDto = new TaskDto(1L, "Sample Task", "Sample Description", true);

        // When
        Task task = taskMapper.mapToTask(taskDto);

        // Then
        assertNotNull(task);
        assertEquals(1L, task.getId());
        assertEquals("Sample Task", task.getName());
        assertEquals("Sample Description", task.getDescription());
        assertTrue(task.isPerformed());
    }

    @Test
    void shouldMapToTaskWithNullDescription() throws WorkingDayNotFoundException {
        // Given
        TaskDto taskDto = new TaskDto(1L, "Sample Task", null, true);  // Null description

        // When
        Task task = taskMapper.mapToTask(taskDto);

        // Then
        assertNotNull(task);
        assertEquals(1L, task.getId());
        assertEquals("Sample Task", task.getName());
        assertEquals("", task.getDescription());  // Default to empty string
        assertTrue(task.isPerformed());
    }

    @Test
    void shouldMapToTaskDtoList() {
        // Given
        Task task1 = Task.builder()
                .id(1L)
                .name("Task 1")
                .description("Description 1")
                .performed(true)
                .build();
        Task task2 = Task.builder()
                .id(2L)
                .name("Task 2")
                .description("Description 2")
                .performed(false)
                .build();
        List<Task> tasks = List.of(task1, task2);

        // When
        List<TaskDto> taskDtoList = taskMapper.mapToTaskDtoList(tasks);

        // Then
        assertNotNull(taskDtoList);
        assertEquals(2, taskDtoList.size());

        TaskDto taskDto1 = taskDtoList.get(0);
        assertEquals(1L, taskDto1.id());
        assertEquals("Task 1", taskDto1.name());
        assertEquals("Description 1", taskDto1.description());
        assertTrue(taskDto1.isPerformed());

        TaskDto taskDto2 = taskDtoList.get(1);
        assertEquals(2L, taskDto2.id());
        assertEquals("Task 2", taskDto2.name());
        assertEquals("Description 2", taskDto2.description());
        assertFalse(taskDto2.isPerformed());
    }

    @Test
    void shouldReturnEmptyListWhenMappingEmptyTaskList() {
        // Given
        List<Task> tasks = List.of();

        // When
        List<TaskDto> taskDtoList = taskMapper.mapToTaskDtoList(tasks);

        // Then
        assertNotNull(taskDtoList);
        assertTrue(taskDtoList.isEmpty());
    }
}