package com.app.rotatio.service;

import com.app.rotatio.controller.exception.WorkplaceNotFoundException;
import com.app.rotatio.domain.Workplace;
import com.app.rotatio.repository.WorkplaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkplaceServiceTest {

    @Mock
    private WorkplaceRepository workplaceRepository;

    @InjectMocks
    private WorkplaceService workplaceService;

    private Workplace workplace;

    @BeforeEach
    void setUp() {
        workplace = Workplace.builder()
                .id(1L)
                .designation("Testing")
                .active(true)
                .nowUsed(true)
                .build();
    }

    @Test
    void shouldSaveWorkplace() {
        //Given
        when(workplaceRepository.save(workplace)).thenReturn(workplace);
        //When
        Workplace savedWorkplace = workplaceService.saveWorkplace(workplace);
        //Then
        assertNotNull(savedWorkplace);
        assertEquals(workplace.getId(), savedWorkplace.getId());
        verify(workplaceRepository, times(1)).save(workplace);
    }

    @Test
    void shouldDeleteWorkplace() {
        //Given
        when(workplaceRepository.save(workplace)).thenReturn(workplace);
        //When
        Workplace deletedWorkplace = workplaceService.deleteWorkplace(workplace);
        //Then
        assertNotNull(deletedWorkplace);
        assertFalse(deletedWorkplace.isActive());
        assertFalse(deletedWorkplace.isNowUsed());
        verify(workplaceRepository, times(1)).save(workplace);
    }

    @Test
    void shouldGetWorkplaceById() throws WorkplaceNotFoundException {
        //Given
        when(workplaceRepository.findById(workplace.getId())).thenReturn(Optional.of(workplace));
        //When
        Workplace foundWorkplace = workplaceService.getWorkplaceById(workplace.getId());
        //Then
        assertNotNull(foundWorkplace);
        assertEquals(workplace.getId(), foundWorkplace.getId());
    }

    @Test
    void shouldThrowExceptionWhenWorkplaceNotFoundById() {
        //Given
        when(workplaceRepository.findById(workplace.getId())).thenReturn(Optional.empty());
        //When
        //Then
        assertThrows(WorkplaceNotFoundException.class, () -> workplaceService.getWorkplaceById(workplace.getId()));
    }

    @Test
    void shouldGetAllWorkplaces() {
        //Given
        when(workplaceRepository.findAll()).thenReturn(List.of(workplace));
        //When
        List<Workplace> workplaces = workplaceService.getAllWorkplaces();
        //Then
        assertNotNull(workplaces);
        assertEquals(1, workplaces.size());
    }

    @Test
    void shouldGetAllByActive() {
        //Given
        when(workplaceRepository.findByActive(true)).thenReturn(List.of(workplace));
        //When
        List<Workplace> activeWorkplaces = workplaceService.getAllByActive(true);
        //Then
        assertNotNull(activeWorkplaces);
        assertEquals(1, activeWorkplaces.size());
    }

    @Test
    void shouldFetchEmptyListByActive() {
        //Given
        when(workplaceRepository.findByActive(false)).thenReturn(List.of());
        //When
        List<Workplace> notActiveWorkplaces = workplaceService.getAllByActive(false);
        //Then
        assertNotNull(notActiveWorkplaces);
        assertEquals(0, notActiveWorkplaces.size());
    }

    @Test
    void shouldGetAllByNowUsed() {
        //Given
        when(workplaceRepository.findByNowUsed(true)).thenReturn(List.of(workplace));
        //When
        List<Workplace> nowUsedWorkplaces = workplaceService.getAllByNowUsed(true);
        //Then
        assertNotNull(nowUsedWorkplaces);
        assertEquals(1, nowUsedWorkplaces.size());
    }

    @Test
    void shouldFetchEmptyListByNowUsed() {
        //Given
        when(workplaceRepository.findByNowUsed(false)).thenReturn(List.of());
        //When
        List<Workplace> notNowUsedWorkplaces = workplaceService.getAllByNowUsed(false);
        //Then
        assertNotNull(notNowUsedWorkplaces);
        assertEquals(0, notNowUsedWorkplaces.size());
    }
}