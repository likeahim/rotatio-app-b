package com.app.rotatio.service;

import com.app.rotatio.controller.exception.UserNotFoundException;
import com.app.rotatio.controller.exception.WorkingDayExecuteDateConflictException;
import com.app.rotatio.controller.exception.WorkingDayNotFoundException;
import com.app.rotatio.domain.*;
import com.app.rotatio.repository.WorkingDayRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkingDayServiceTest {

    @InjectMocks
    private WorkingDayService workingDayService;
    @Mock
    private WorkingDayRepository workingDayRepository;
    @Mock
    private UserService userService;

    private WorkingDay workingDay;

    @BeforeEach
    void setUp() {
        workingDay = WorkingDay.builder()
                .created(LocalDate.now())
                .build();
    }

    @Test
    void shouldSaveWorkingDay() {
        //Given
        when(workingDayRepository.save(workingDay)).thenReturn(workingDay);
        //When
        WorkingDay savedWorkingDay = workingDayService.saveWorkingDay(workingDay);
        //Then
        assertNotNull(savedWorkingDay);
        assertEquals(workingDay.getId(), savedWorkingDay.getId());
        verify(workingDayRepository, times(1)).save(workingDay);
    }

    @Test
    void shouldCreateNewWorkingDay() throws WorkingDayExecuteDateConflictException {
        //Given
        when(workingDayRepository.existsByExecuteDate(workingDay.getExecuteDate())).thenReturn(false);
        when(workingDayRepository.save(workingDay)).thenReturn(workingDay);
        //When
        WorkingDay newWorkingDay = workingDayService.createNewWorkingDay(workingDay);
        //Then
        assertNotNull(newWorkingDay);
        assertEquals(workingDay.getExecuteDate(), newWorkingDay.getExecuteDate());
        verify(workingDayRepository, times(1)).save(workingDay);
    }

    @Test
    void shouldThrowExceptionWhenExecuteDateConflict() {
        //Given
        when(workingDayRepository.existsByExecuteDate(workingDay.getExecuteDate())).thenReturn(true);
        //When
        //Then
        assertThrows(WorkingDayExecuteDateConflictException.class, () -> workingDayService.createNewWorkingDay(workingDay));
    }

    @Test
    void shouldDeleteById() {
        //Given
        //When
        workingDayService.deleteById(workingDay.getId());
        //Then
        verify(workingDayRepository, times(1)).deleteById(workingDay.getId());
    }

    @Test
    void shouldGetWorkingDayById() throws WorkingDayNotFoundException {
        //Given
        when(workingDayRepository.findById(workingDay.getId())).thenReturn(Optional.of(workingDay));
        //When
        WorkingDay foundWorkingDay = workingDayService.getWorkingDayById(workingDay.getId());
        //Then
        assertNotNull(foundWorkingDay);
        assertEquals(workingDay.getId(), foundWorkingDay.getId());
    }

    @Test
    void shouldThrowExceptionWhenWorkingDayNotFoundById() {
        //Given
        when(workingDayRepository.findById(workingDay.getId())).thenReturn(Optional.empty());
        //When
        //Then
        assertThrows(WorkingDayNotFoundException.class, () -> workingDayService.getWorkingDayById(workingDay.getId()));
    }

    @Test
    void shouldGetByExecuteDate() throws WorkingDayNotFoundException {
        //Given
        when(workingDayRepository.findByExecuteDate(workingDay.getExecuteDate())).thenReturn(Optional.ofNullable(workingDay));
        //When
        WorkingDay foundWorkingDay = workingDayService.getByExecuteDate(workingDay.getExecuteDate());
        //Then
        assertNotNull(foundWorkingDay);
        assertEquals(workingDay.getExecuteDate(), foundWorkingDay.getExecuteDate());
    }

    @Test
    void shouldGetAllWorkingDays() {
        //Given
        when(workingDayRepository.findAll()).thenReturn(List.of(workingDay));
        //When
        List<WorkingDay> workingDays = workingDayService.getAllWorkingDays();
        //Then
        assertNotNull(workingDays);
        assertEquals(1, workingDays.size());
    }

    @Test
    void shouldGetAllByUser() throws UserNotFoundException {
        //Given
        User user = new User();
        user.setId(1L);
        when(userService.getUserById(1L)).thenReturn(user);
        when(workingDayRepository.findByUser(user)).thenReturn(List.of(workingDay));
        //When
        List<WorkingDay> workingDays = workingDayService.getAllByUser(1L);
        //Then
        assertNotNull(workingDays);
        assertEquals(1, workingDays.size());
    }

    @Test
    void shouldGetAllByPlanned() {
        //Given
        when(workingDayRepository.findByPlanned(true)).thenReturn(List.of(workingDay));
        //When
        List<WorkingDay> workingDays = workingDayService.getAllByPlanned(true);
        //Then
        assertNotNull(workingDays);
        assertEquals(1, workingDays.size());
    }

    @Test
    void shouldDeleteAll() {
        //Given
        //When
        workingDayService.deleteAll();
        //Then
        verify(workingDayRepository, times(1)).deleteAll();
    }

    @Test
    void shouldConvertLongListToWorkingDaysList() throws WorkingDayNotFoundException {
        //Given
        List<Long> workingDayIds = List.of(1L);
        when(workingDayRepository.findById(1L)).thenReturn(Optional.of(workingDay));
        //When
        List<WorkingDay> workingDays = workingDayService.longToWorkingDaysList(workingDayIds);
        //Then
        assertNotNull(workingDays);
        assertEquals(1, workingDays.size());
    }

    @Test
    void shouldConvertWorkingDaysListToLongList() {
        //Given
        List<WorkingDay> workingDays = List.of(workingDay);
        //When
        List<Long> workingDayIds = workingDayService.workingDaysToLongList(workingDays);
        //Then
        assertNotNull(workingDayIds);
        assertEquals(1, workingDayIds.size());
        assertEquals(workingDay.getId(), workingDayIds.get(0));
    }
}