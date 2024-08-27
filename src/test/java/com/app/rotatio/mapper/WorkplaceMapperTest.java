package com.app.rotatio.mapper;

import com.app.rotatio.controller.exception.TaskNotFoundException;
import com.app.rotatio.controller.exception.WorkingDayNotFoundException;
import com.app.rotatio.domain.Workplace;
import com.app.rotatio.domain.dto.WorkplaceDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WorkplaceMapperTest {

    private WorkplaceMapper workplaceMapper;

    @BeforeEach
    void setUp() {
        workplaceMapper = new WorkplaceMapper();
    }

    @Test
    void shouldMapToWorkplaceDto() {
        // Given
        Workplace workplace = Workplace.builder()
                .id(1L)
                .designation("Office A")
                .active(true)
                .nowUsed(false)
                .build();

        // When
        WorkplaceDto workplaceDto = workplaceMapper.mapToWorkplaceDto(workplace);

        // Then
        assertNotNull(workplaceDto);
        assertEquals(1L, workplaceDto.id());
        assertEquals("Office A", workplaceDto.designation());
        assertTrue(workplaceDto.active());
        assertFalse(workplaceDto.nowUsed());
    }

    @Test
    void shouldMapToWorkplace() throws TaskNotFoundException, WorkingDayNotFoundException {
        // Given
        WorkplaceDto workplaceDto = new WorkplaceDto(1L, "Office A", true, false);

        // When
        Workplace workplace = workplaceMapper.mapToWorkplace(workplaceDto);

        // Then
        assertNotNull(workplace);
        assertEquals(1L, workplace.getId());
        assertEquals("Office A", workplace.getDesignation());
        assertTrue(workplace.isActive());
        assertFalse(workplace.isNowUsed());
    }

    @Test
    void shouldMapToWorkplaceDtoList() {
        // Given
        Workplace workplace1 = Workplace.builder()
                .id(1L)
                .designation("Office A")
                .active(true)
                .nowUsed(false)
                .build();

        Workplace workplace2 = Workplace.builder()
                .id(2L)
                .designation("Office B")
                .active(false)
                .nowUsed(true)
                .build();

        List<Workplace> workplaces = List.of(workplace1, workplace2);

        // When
        List<WorkplaceDto> workplaceDtoList = workplaceMapper.mapToWorkplaceDtoList(workplaces);

        // Then
        assertNotNull(workplaceDtoList);
        assertEquals(2, workplaceDtoList.size());

        WorkplaceDto workplaceDto1 = workplaceDtoList.get(0);
        assertEquals(1L, workplaceDto1.id());
        assertEquals("Office A", workplaceDto1.designation());
        assertTrue(workplaceDto1.active());
        assertFalse(workplaceDto1.nowUsed());

        WorkplaceDto workplaceDto2 = workplaceDtoList.get(1);
        assertEquals(2L, workplaceDto2.id());
        assertEquals("Office B", workplaceDto2.designation());
        assertFalse(workplaceDto2.active());
        assertTrue(workplaceDto2.nowUsed());
    }

    @Test
    void shouldReturnEmptyListWhenMappingEmptyWorkplaceList() {
        // Given
        List<Workplace> workplaces = Collections.emptyList();

        // When
        List<WorkplaceDto> workplaceDtoList = workplaceMapper.mapToWorkplaceDtoList(workplaces);

        // Then
        assertNotNull(workplaceDtoList);
        assertTrue(workplaceDtoList.isEmpty());
    }
}