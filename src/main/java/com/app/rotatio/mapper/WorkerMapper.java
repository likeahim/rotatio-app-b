package com.app.rotatio.mapper;

import com.app.rotatio.domain.Worker;
import com.app.rotatio.domain.WorkerStatus;
import com.app.rotatio.domain.dto.WorkerDto;
import com.app.rotatio.service.TaskService;
import com.app.rotatio.service.WorkingDayService;
import com.app.rotatio.service.WorkplaceService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkerMapper {

    private final WorkingDayService workingDayService;
    private final TaskService taskService;
    private final WorkplaceService workplaceService;

    @SneakyThrows
    public Worker mapToWorker(final WorkerDto workerDto) {
        return Worker.builder()
                .id(workerDto.id())
                .workerNumber(workerDto.workerNumber())
                .firstName(workerDto.firstName())
                .lastName(workerDto.lastname())
                .status(WorkerStatus.fromValue(workerDto.status()))
                .presenceFrom(workerDto.presenceFrom() != null ? workerDto.presenceFrom() : null)
                .absenceFrom(workerDto.absenceFrom() != null ? workerDto.absenceFrom() : null)
                .workingDay(workerDto.workingDayId() != null ?
                        workingDayService.getWorkingDayById(workerDto.workingDayId()) : null)
                .task(workerDto.taskId() != null ?
                        taskService.getTaskById(workerDto.taskId()) : null)
                .workplace(workerDto.workplaceId() != null ?
                        workplaceService.getWorkplaceById(workerDto.workplaceId()) : null)
                .build();
    }

    public WorkerDto mapToWorkerDto(final Worker worker) {
        return new WorkerDto(
                worker.getId(),
                worker.getWorkerNumber(),
                worker.getFirstName(),
                worker.getLastName(),
                worker.getStatus().getValue(),
                worker.getPresenceFrom()  != null ? worker.getPresenceFrom() : null,
                worker.getAbsenceFrom() != null ? worker.getAbsenceFrom() : null,
                worker.getWorkingDay() != null ? worker.getWorkingDay().getId() : null,
                worker.getTask() != null ? worker.getTask().getId() : null,
                worker.getWorkplace() != null ? worker.getWorkplace().getId() : null
        );
    }

    public List<WorkerDto> mapToWorkerDtoList(final List<Worker> workers) {
        return workers.stream()
                .map(this::mapToWorkerDto)
                .toList();
    }
}
