package com.app.rotatio.mapper;

import com.app.rotatio.controller.exception.WorkingDayNotFoundException;
import com.app.rotatio.domain.Task;
import com.app.rotatio.domain.dto.TaskDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskMapper {

    public TaskDto mapToTaskDto(final Task task) {
        return new TaskDto(
                task.getId(),
                task.getName(),
                task.getDescription() != null ? task.getDescription() : "",
                task.isPerformed()
        );
    }

        public Task mapToTask(final TaskDto taskDto) throws WorkingDayNotFoundException {
            return Task.builder()
                    .id(taskDto.id())
                    .name(taskDto.name())
                    .description(taskDto.description() != null ? taskDto.description() : "")
                    .performed(taskDto.isPerformed())
                    .build();
    }

    public List<TaskDto> mapToTaskDtoList(final List<Task> tasks) {
        if(tasks.isEmpty()) {
            return Collections.emptyList();
        }

        return tasks.stream()
                .map(this::mapToTaskDto)
                .toList();
    }
}
