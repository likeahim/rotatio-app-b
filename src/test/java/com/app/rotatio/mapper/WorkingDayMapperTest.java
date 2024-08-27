package com.app.rotatio.mapper;

import com.app.rotatio.controller.exception.UserNotFoundException;
import com.app.rotatio.domain.User;
import com.app.rotatio.domain.Worker;
import com.app.rotatio.domain.WorkingDay;
import com.app.rotatio.domain.dto.WorkingDayDto;
import com.app.rotatio.service.UserService;
import com.app.rotatio.service.WorkerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkingDayMapperTest {

    @InjectMocks
    private WorkingDayMapper workingDayMapper;
    @Mock
    private UserService userService;
    @Mock
    private WorkerService workerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        workingDayMapper = new WorkingDayMapper(userService, workerService);
    }

    @Test
    void shouldMapToWorkingDay() throws UserNotFoundException {
        // Given
        WorkingDayDto workingDayDto = new WorkingDayDto(1L, LocalDate.now(), LocalDate.now(), true, false, 2L, List.of(3L, 4L));

        User user = User.builder().id(2L).build();
        List<Worker> workers = List.of(Worker.builder().id(3L).build(), Worker.builder().id(4L).build());

        when(userService.getUserById(2L)).thenReturn(user);
        when(workerService.longToWorkersList(List.of(3L, 4L))).thenReturn(workers);

        // When
        WorkingDay workingDay = workingDayMapper.mapToWorkingDay(workingDayDto);

        // Then
        assertNotNull(workingDay);
        assertEquals(1L, workingDay.getId());
        assertEquals(2L, workingDay.getUser().getId());
        assertEquals(2, workingDay.getWorkers().size());
        assertTrue(workingDay.isPlanned());
        assertFalse(workingDay.isArchived());
        verify(userService, times(1)).getUserById(2L);
        verify(workerService, times(1)).longToWorkersList(List.of(3L, 4L));
    }

    @Test
    void shouldMapToWorkingDayDto() {
        // Given
        User user = User.builder().id(2L).build();
        List<Worker> workers = List.of(Worker.builder().id(3L).build(), Worker.builder().id(4L).build());

        WorkingDay workingDay = WorkingDay.builder()
                .id(1L)
                .created(LocalDate.now())
                .executeDate(LocalDate.now())
                .planned(true)
                .archived(false)
                .user(user)
                .workers(workers)
                .build();

        when(workerService.workersToLongList(workers)).thenReturn(List.of(3L, 4L));

        // When
        WorkingDayDto workingDayDto = workingDayMapper.mapToWorkingDayDto(workingDay);

        // Then
        assertNotNull(workingDayDto);
        assertEquals(1L, workingDayDto.id());
        assertEquals(2L, workingDayDto.userId());
        assertEquals(2, workingDayDto.workers().size());
        assertTrue(workingDayDto.planned());
        assertFalse(workingDayDto.archived());
        verify(workerService, times(1)).workersToLongList(workers);
    }

    @Test
    void shouldMapToWorkingDayDtoList() {
        // Given
        User user = User.builder().id(2L).build();
        List<Worker> workers = List.of(Worker.builder().id(3L).build(), Worker.builder().id(4L).build());

        WorkingDay workingDay1 = WorkingDay.builder()
                .id(1L)
                .created(LocalDate.now())
                .executeDate(LocalDate.now())
                .planned(true)
                .archived(false)
                .user(user)
                .workers(workers)
                .build();

        WorkingDay workingDay2 = WorkingDay.builder()
                .id(2L)
                .created(LocalDate.now())
                .executeDate(LocalDate.now())
                .planned(false)
                .archived(true)
                .user(user)
                .workers(workers)
                .build();

        List<WorkingDay> workingDays = List.of(workingDay1, workingDay2);
        when(workerService.workersToLongList(anyList())).thenReturn(List.of(3L, 4L));

        // When
        List<WorkingDayDto> workingDayDtoList = workingDayMapper.mapToWorkingDayDtoList(workingDays);

        // Then
        assertNotNull(workingDayDtoList);
        assertEquals(2, workingDayDtoList.size());

        WorkingDayDto workingDayDto1 = workingDayDtoList.get(0);
        assertEquals(1L, workingDayDto1.id());
        assertEquals(2L, workingDayDto1.userId());
        assertEquals(2, workingDayDto1.workers().size());
        assertTrue(workingDayDto1.planned());
        assertFalse(workingDayDto1.archived());

        WorkingDayDto workingDayDto2 = workingDayDtoList.get(1);
        assertEquals(2L, workingDayDto2.id());
        assertEquals(2L, workingDayDto2.userId());
        assertEquals(2, workingDayDto2.workers().size());
        assertFalse(workingDayDto2.planned());
        assertTrue(workingDayDto2.archived());

        verify(workerService, times(2)).workersToLongList(anyList());
    }

    @Test
    void shouldReturnEmptyListWhenMappingEmptyWorkingDayList() {
        // Given
        List<WorkingDay> workingDays = Collections.emptyList();

        // When
        List<WorkingDayDto> workingDayDtoList = workingDayMapper.mapToWorkingDayDtoList(workingDays);

        // Then
        assertNotNull(workingDayDtoList);
        assertTrue(workingDayDtoList.isEmpty());
    }
}