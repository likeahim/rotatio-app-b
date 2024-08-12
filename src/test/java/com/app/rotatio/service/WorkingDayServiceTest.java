package com.app.rotatio.service;

import com.app.rotatio.domain.*;
import com.app.rotatio.repository.UserRepository;
import com.app.rotatio.repository.WorkingDayRepository;
import com.app.rotatio.service.strategy.StrategyService;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkingDayServiceTest {

    @InjectMocks
    private WorkingDayService workingDayService;
    @Mock
    private WorkingDayRepository workingDayRepository;

    private WorkingDay workingDay;

    @BeforeEach
    void setUp() {
        workingDay = WorkingDay.builder()
                .created(LocalDate.now())
                .build();
    }
    @AfterEach
    void cleanUp() {
        workingDayService.deleteAll();
    }

    @Test
    void shouldSaveWorkingDay() {
        //Given
        WorkingDay savedWorkingDay = workingDay;
        savedWorkingDay.setId(10L);
        when(workingDayRepository.save(workingDay)).thenReturn(savedWorkingDay);
        //When
        WorkingDay saved = workingDayService.save(workingDay);
        //Then
        assertEquals(10L, saved.getId());
        verify(workingDayRepository).save(workingDay);
    }

    @Test
    void shouldDeleteWorkingDay() {
        //Given
        workingDayService.save(workingDay);
        //When
        workingDayService.delete(workingDay);
        //Then
        verify(workingDayRepository).delete(workingDay);
    }

    @Test
    void shouldFetchAllWorkingDays() {
        //Given
        workingDayService.save(workingDay);
        List<WorkingDay> workingDays = List.of(workingDay);
        when(workingDayRepository.findAll()).thenReturn(workingDays);
        //When
        List<WorkingDay> allWorkingDays = workingDayService.getAllWorkingDays();
        //Then
        assertEquals(workingDays.size(), allWorkingDays.size());
        assertEquals(1, allWorkingDays.size());
        verify(workingDayRepository).findAll();
    }

    @Test
    void shouldFetchWorkingDaysByUser() {
        //Given
        User user = User.builder()
                .id(1L)
                .email("john@test.com")
                .password("testing123")
                .build();
        workingDay.setUser(user);
        when(workingDayRepository.findByUser(user)).thenReturn(List.of(workingDay));
        //When
        List<WorkingDay> workingDays = workingDayService.fetchByUser(user);
        //Then
        verify(workingDayRepository).findByUser(user);
        assertEquals(1, workingDays.size());
    }

    @Test
    void shouldFetchWorkingDaysByPlanned() {
        //Given
        WorkingDay secondWorkingDay = WorkingDay.builder()
                .created(LocalDate.now())
                .planned(true)
                .build();
        when(workingDayRepository.findByPlanned(true)).thenReturn(List.of(secondWorkingDay));
        when(workingDayRepository.findByPlanned(false)).thenReturn(List.of(workingDay));
        //When
        List<WorkingDay> plannedWorkingDays = workingDayService.fetchByPlanned(true);
        List<WorkingDay> unplannedWorkingDays = workingDayService.fetchByPlanned(false);
        //Then
        verify(workingDayRepository).findByPlanned(true);
        verify(workingDayRepository).findByPlanned(false);
        assertEquals(1, unplannedWorkingDays.size());
        assertEquals(1, plannedWorkingDays.size());
    }

    @Test
    void shouldFetchWorkingDaysByExecuteDateBefore() {
        //Given
        workingDay.setExecuteDate(LocalDate.now().plusDays(1));
        LocalDate execute = LocalDate.now().plusDays(2);
        when(workingDayRepository.findByExecuteDateBefore(execute)).thenReturn(List.of(workingDay));
        //When
        List<WorkingDay> workingDays = workingDayService.fetchByExecuteDateBefore(execute);
        //Then
        verify(workingDayRepository).findByExecuteDateBefore(execute);
        assertEquals(1, workingDays.size());
    }

    @Test
    void shouldFetchWorkingDaysByArchived() {
        //Given
        workingDay.setArchived(true);
        when(workingDayRepository.findByArchived(true)).thenReturn(List.of(workingDay));
        when(workingDayRepository.findByArchived(false)).thenReturn(new ArrayList<>());
        //When
        List<WorkingDay> archived = workingDayService.fetchByArchived(true);
        List<WorkingDay> notArchived = workingDayService.fetchByArchived(false);
        //Then
        assertEquals(1, archived.size());
        assertEquals(0, notArchived.size());
        verify(workingDayRepository).findByArchived(true);
        verify(workingDayRepository).findByArchived(false);
    }
}