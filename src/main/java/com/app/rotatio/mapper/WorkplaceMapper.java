package com.app.rotatio.mapper;

import com.app.rotatio.controller.exception.TaskNotFoundException;
import com.app.rotatio.controller.exception.WorkingDayNotFoundException;
import com.app.rotatio.domain.Workplace;
import com.app.rotatio.domain.dto.WorkplaceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkplaceMapper {

    public WorkplaceDto mapToWorkplaceDto(Workplace workplace) {
        return new WorkplaceDto(
                workplace.getId(),
                workplace.getDesignation(),
                workplace.isActive(),
                workplace.isNowUsed()
        );
    }

    public Workplace mapToWorkplace(WorkplaceDto workplaceDto) throws TaskNotFoundException, WorkingDayNotFoundException {
        return Workplace.builder()
                .id(workplaceDto.id())
                .designation(workplaceDto.designation())
                .active(workplaceDto.active())
                .nowUsed(workplaceDto.nowUsed())
                .build();
    }

    public List<WorkplaceDto> mapToWorkplaceDtoList(List<Workplace> workplaces) {
        if(workplaces.isEmpty()) {
            return Collections.emptyList();
        }
        return workplaces.stream()
                .map(this::mapToWorkplaceDto)
                .toList();
    }
}
