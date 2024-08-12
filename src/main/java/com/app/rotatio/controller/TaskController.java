package com.app.rotatio.controller;

import com.app.rotatio.domain.Task;
import com.app.rotatio.domain.WorkingDay;
import com.app.rotatio.domain.dto.TaskDto;
import com.app.rotatio.mapper.TaskMapper;
import com.app.rotatio.service.TaskService;
import com.app.rotatio.service.WorkingDayService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/rotatio/tasks")
@RequiredArgsConstructor
@CrossOrigin("*")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @SneakyThrows
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto taskDto) {
        Task saved = taskService.saveTask(taskMapper.mapToTask(taskDto));
        return ResponseEntity.ok(taskMapper.mapToTaskDto(saved));
    }

    @GetMapping
    public ResponseEntity<List<TaskDto>> getAllTasks() {
        List<Task> allTasks = taskService.getAllTasks();
        return ResponseEntity.ok(taskMapper.mapToTaskDtoList(allTasks));
    }

    @SneakyThrows
    @GetMapping(value = "{taskId}")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long taskId) {
        Task task = taskService.getTaskById(taskId);
        return ResponseEntity.ok(taskMapper.mapToTaskDto(task));
    }

    @SneakyThrows
    @GetMapping(value = "{taskName}")
    public ResponseEntity<TaskDto> getTaskByName(@PathVariable String taskName) {
        Task task = taskService.getTaskByName(taskName);
        return ResponseEntity.ok(taskMapper.mapToTaskDto(task));
    }

    @SneakyThrows
    @GetMapping("/byWorkingDay/{workingDayId}")
    public ResponseEntity<List<TaskDto>> getTasksByWorkingDay(@PathVariable Long workingDayId) {
        List<Task> allTasksByWorkingDay = taskService.getAllTasksByWorkingDay(workingDayId);
        return ResponseEntity.ok(taskMapper.mapToTaskDtoList(allTasksByWorkingDay));
    }

    @SneakyThrows
    @DeleteMapping(value = "{taskId}")
    public ResponseEntity<TaskDto> deleteTask(@PathVariable Long taskId) {
        Task task = taskService.getTaskById(taskId);
        Task deleted = taskService.delete(task);
        return ResponseEntity.ok(taskMapper.mapToTaskDto(deleted));
    }

    @SneakyThrows
    @PutMapping(value = "/update")
    public ResponseEntity<TaskDto> updateTask(@RequestBody TaskDto taskDto) {
        Task saved = taskService.saveTask(taskMapper.mapToTask(taskDto));
        return ResponseEntity.ok(taskMapper.mapToTaskDto(saved));
    }
}
