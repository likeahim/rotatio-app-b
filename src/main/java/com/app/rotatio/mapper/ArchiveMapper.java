package com.app.rotatio.mapper;

import com.app.rotatio.domain.Archive;
import com.app.rotatio.domain.dto.ArchiveDto;
import org.springframework.stereotype.Service;

@Service
public class ArchiveMapper {

    public Archive mapToArchive(final ArchiveDto dto) {
        return Archive.builder()
                .id(dto.id())
                .workingDayId(dto.workingDayId())
                .workersData(dto.workersData())
                .build();
    }

    public ArchiveDto mapToArchiveDto(final Archive archive) {
        return new ArchiveDto(
                archive.getId(),
                archive.getWorkingDayId(),
                archive.getWorkersData());
    }
}
