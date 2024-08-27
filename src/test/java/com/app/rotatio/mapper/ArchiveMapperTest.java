package com.app.rotatio.mapper;

import com.app.rotatio.domain.Archive;
import com.app.rotatio.domain.dto.ArchiveDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ArchiveMapperTest {

    @Autowired
    private ArchiveMapper archiveMapper;

    private Archive archive;
    private ArchiveDto archiveDto;

    @BeforeEach
    public void setUp() {
        archive = Archive.builder()
                .id(1L)
                .workingDayId(101L)
                .workersData("John Doe Test Task Test Workplace")
                .build();

        archiveDto = new ArchiveDto(
                1L,
                101L,
                "John Doe Test Task Test Workplace"
        );
    }

    @Test
    void shouldMapToArchive() {
        // When
        Archive result = archiveMapper.mapToArchive(archiveDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(archiveDto.id());
        assertThat(result.getWorkingDayId()).isEqualTo(archiveDto.workingDayId());
        assertThat(result.getWorkersData()).isEqualTo(archiveDto.workersData());
    }

    @Test
    void shouldMapToArchiveDto() {
        // When
        ArchiveDto result = archiveMapper.mapToArchiveDto(archive);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(archive.getId());
        assertThat(result.workingDayId()).isEqualTo(archive.getWorkingDayId());
        assertThat(result.workersData()).isEqualTo(archive.getWorkersData());
    }

    @Test
    void shouldMapToArchiveDtoList() {
        // Given
        List<Archive> archives = Arrays.asList(archive);

        // When
        List<ArchiveDto> result = archiveMapper.mapToArchiveDtoList(archives);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        ArchiveDto resultItem = result.get(0);
        assertThat(resultItem.id()).isEqualTo(archive.getId());
        assertThat(resultItem.workingDayId()).isEqualTo(archive.getWorkingDayId());
        assertThat(resultItem.workersData()).isEqualTo(archive.getWorkersData());
    }
}