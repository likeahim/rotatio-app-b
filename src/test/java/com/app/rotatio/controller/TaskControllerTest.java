package com.app.rotatio.controller;

import com.app.rotatio.controller.exception.TaskNotFoundException;
import com.app.rotatio.domain.Task;
import com.app.rotatio.domain.Workplace;
import com.app.rotatio.domain.dto.TaskDto;
import com.app.rotatio.domain.dto.WorkplaceDto;
import com.app.rotatio.mapper.TaskMapper;
import com.app.rotatio.service.TaskService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitWebConfig
@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @MockBean
    private TaskMapper taskMapper;

    private TaskDto taskDto;
    private Task task;
    private String json;
    private ObjectMapper mapper;
    private List<Task> tasks;
    private List<TaskDto> taskDtos;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        taskDto = new TaskDto(1L, "Testing", "Test description", false);
        task = Task.builder()
                .id(1L)
                .name("Testing")
                .description("Test desctiption")
                .performed(true)
                .build();

        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        json = mapper.writeValueAsString(taskDto);
        tasks = new ArrayList<>(List.of(task));
        taskDtos = new ArrayList<>(List.of(taskDto));
    }

    @Test
    void shouldCreateTask() throws Exception {

        when(taskMapper.mapToTask(any(TaskDto.class))).thenReturn(task);
        when(taskService.saveTask(any(Task.class))).thenReturn(task);
        when(taskMapper.mapToTaskDto(any(Task.class))).thenReturn(taskDto);

        mockMvc.perform(post("/v1/rotatio/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Testing"));
    }

    @Test
    void shouldGetAllTasks() throws Exception {

        when(taskService.getAllTasks()).thenReturn(Collections.singletonList(new Task()));
        when(taskMapper.mapToTaskDtoList(any(List.class))).thenReturn(taskDtos);

        mockMvc.perform(get("/v1/rotatio/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].description").value("Test description"));
    }

    @Test
    void shouldGetTaskById() throws Exception {

        when(taskService.getTaskById(1L)).thenReturn(task);
        when(taskMapper.mapToTaskDto(task)).thenReturn(taskDto);

        mockMvc.perform(get("/v1/rotatio/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.isPerformed").value(false));
    }

    @Test
    void shouldDeleteTask() throws Exception {
        TaskDto deleted = new TaskDto(1L, "Testing", "Test description", false);

        when(taskService.getTaskById(1L)).thenReturn(task);
        when(taskService.delete(task)).thenReturn(task);
        when(taskMapper.mapToTaskDto(task)).thenReturn(deleted);

        mockMvc.perform(delete("/v1/rotatio/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.isPerformed").value(false));
    }

    @Test
    void shouldUpdateTask() throws Exception {
        TaskDto updated = new TaskDto(1L, "Updated", "Updated description", false);
        String updatedJson = mapper.writeValueAsString(updated);
        when(taskMapper.mapToTask(any(TaskDto.class))).thenReturn(task);
        when(taskService.saveTask(any(Task.class))).thenReturn(task);
        when(taskMapper.mapToTaskDto(any(Task.class))).thenReturn(updated);

        mockMvc.perform(put("/v1/rotatio/tasks/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Updated description"))
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    void shouldUpdatePerformedTask() throws Exception {
        TaskDto updatedByPerformed = new TaskDto(1L, "Testing", "Test description", true);
        String performedUpdated = mapper.writeValueAsString(updatedByPerformed);

        when(taskService.updatePerformed(1L, true)).thenReturn(task);
        when(taskMapper.mapToTaskDto(task)).thenReturn(updatedByPerformed);

        mockMvc.perform(patch("/v1/rotatio/tasks/updatePerformed/1/true")
                .contentType(MediaType.APPLICATION_JSON)
                .content(performedUpdated))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.isPerformed").value(true));
    }

    @Test
    void shouldGetTasksByPerformed() throws Exception {

        when(taskService.getAllTasksByPerformed(true)).thenReturn(Collections.singletonList(new Task()));
        when(taskMapper.mapToTaskDtoList(any(List.class))).thenReturn(taskDtos);

        mockMvc.perform(get("/v1/rotatio/tasks/byPerformed/false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].isPerformed").value(false));
    }

    @Test
    void shouldReturnNotFoundWhenTaskDoesNotExist() throws Exception {
        when(taskService.getTaskById(1L)).thenThrow(new TaskNotFoundException());

        mockMvc.perform(get("/v1/rotatio/tasks/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Such a task not found"));
    }

    @Test
    void shouldReturnBadRequestWhenCreatingTaskWithInvalidData() throws Exception {
        TaskDto invalid = new TaskDto(1L, null, "Test description", false);
        String invalidJson = mapper.writeValueAsString(invalid);
        mockMvc.perform(post("/v1/rotatio/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenUpdatingTaskWithInvalidData() throws Exception {
        TaskDto invalid = new TaskDto(1L, null, "Test description", false);
        String invalidJson = mapper.writeValueAsString(invalid);

        mockMvc.perform(put("/v1/rotatio/tasks/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}