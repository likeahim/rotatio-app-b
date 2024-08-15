package com.app.rotatio.mapper;

import com.app.rotatio.controller.exception.UserNotFoundException;
import com.app.rotatio.domain.WorkingDay;
import com.app.rotatio.domain.dto.WorkingDayDto;
import com.app.rotatio.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkingDayMapper {

    private final UserService userService;

    public WorkingDay mapToWorkingDay(WorkingDayDto workingDayDto) throws UserNotFoundException {
        return WorkingDay.builder()
                .id(workingDayDto.id())
                .created(workingDayDto.created())
                .executeDate(workingDayDto.executeDate() != null ? workingDayDto.executeDate() : null)
                .planned(workingDayDto.planned())
                .user(workingDayDto.userId() != null ? userService.getUserById(workingDayDto.userId()) : null)
                .build();
    }

    public WorkingDayDto mapToWorkingDayDto(WorkingDay workingDay) {
        return new WorkingDayDto(
                workingDay.getId(),
                workingDay.getCreated(),
                workingDay.getExecuteDate() != null ? workingDay.getExecuteDate() : null,
                workingDay.isPlanned(),
                workingDay.getUser().getId() != null ? workingDay.getUser().getId() : null
        );
    }

    public List<WorkingDayDto> mapToWorkingDayDtoList(List<WorkingDay> workingDays) {
        if (workingDays.isEmpty()) {
            return Collections.emptyList();
        }
        return workingDays.stream()
                .map(this::mapToWorkingDayDto)
                .toList();
    }
}
