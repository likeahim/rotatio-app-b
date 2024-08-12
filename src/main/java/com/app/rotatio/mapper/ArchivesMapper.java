package com.app.rotatio.mapper;

import com.app.rotatio.domain.WorkingDay;
import com.app.rotatio.domain.WorkingDayArchives;
import com.app.rotatio.domain.dto.WorkingDayArchivesDto;
import com.app.rotatio.service.WorkingDayService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArchivesMapper {

    private final WorkingDayService workingDayService;

    public WorkingDayArchivesDto mapToArchivesDto(final WorkingDayArchives archives) {
        return new WorkingDayArchivesDto(
                archives.getId(),
                archives.getExecuted(),
                archives.getId()
        );
    }

    @SneakyThrows
    public WorkingDayArchives mapToArchives(final WorkingDayArchivesDto workingDayArchivesDto) {
        WorkingDay workingDayById = workingDayService.getWorkingDayById(workingDayArchivesDto.id());
        return WorkingDayArchives.builder()
                .id(workingDayArchivesDto.id())
                .executed(workingDayArchivesDto.executed())
                .workingDay(workingDayById)
                .build();
    }

    public List<WorkingDayArchivesDto> mapToArchivesDtoList(final List<WorkingDayArchives> archives) {
        return archives.stream()
                .map(this::mapToArchivesDto)
                .toList();
    }
}
