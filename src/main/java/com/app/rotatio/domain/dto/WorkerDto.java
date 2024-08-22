package com.app.rotatio.domain.dto;

import com.app.rotatio.domain.WorkerStatus;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

public record WorkerDto(
        Long id,
        @NotNull
        Long workerNumber,
        @NotNull
        String firstName,
        @NotNull
        String lastname,
        @NotNull
        int status,
        LocalDate presenceFrom,
        LocalDate absenceFrom,
        Long workingDayId,
        Long taskId,
        Long workplaceId
) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkerDto workerDto = (WorkerDto) o;
        return status == workerDto.status && Objects.equals(id, workerDto.id) &&
               Objects.equals(taskId, workerDto.taskId) &&
               Objects.equals(lastname, workerDto.lastname) &&
               Objects.equals(firstName, workerDto.firstName) &&
               Objects.equals(workplaceId, workerDto.workplaceId) &&
               Objects.equals(workerNumber, workerDto.workerNumber) &&
               Objects.equals(workingDayId, workerDto.workingDayId) &&
               Objects.equals(absenceFrom, workerDto.absenceFrom) &&
               Objects.equals(presenceFrom, workerDto.presenceFrom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, workerNumber, firstName, lastname, status,
                presenceFrom, absenceFrom, workingDayId, taskId, workplaceId);
    }
}
