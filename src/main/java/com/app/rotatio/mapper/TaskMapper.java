package com.app.rotatio.mapper;

import com.app.rotatio.controller.exception.WorkingDayNotFoundException;
import com.app.rotatio.domain.Task;
import com.app.rotatio.domain.Workplace;
import com.app.rotatio.domain.dto.TaskDto;
import com.app.rotatio.service.WorkingDayService;
import com.app.rotatio.service.WorkplaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskMapper {

    private final WorkingDayService workingDayService;
    private final WorkplaceService workplaceService;

    public TaskDto mapToTaskDto(final Task task) {
        return new TaskDto(
                task.getId(),
                task.getName(),
                task.getDescription(),
                task.isPerformed(),
                task.getWorkingDay().getId(),
                task.getWorkplaces().stream()
                        .map(Workplace::getId)
                        .toList()
        );
    }

        public Task mapToTask(final TaskDto taskDto) throws WorkingDayNotFoundException {
            return Task.builder()
                    .id(taskDto.id())
                    .name(taskDto.name())
                    .description(taskDto.description())
                    .isPerformed(taskDto.isPerformed())
                    .workingDay(workingDayService.getWorkingDayById(taskDto.id()))
                    .workplaces(workplaceService.longToWorkplacesList(taskDto.workplaces()))
                    .build();
    }

    public List<TaskDto> mapToTaskDtoList(final List<Task> tasks) {
        return tasks.stream()
                .map(this::mapToTaskDto)
                .toList();
    }
}
