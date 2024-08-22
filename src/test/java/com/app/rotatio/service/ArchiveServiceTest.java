package com.app.rotatio.service;

import com.app.rotatio.controller.exception.ArchiveNotFoundException;
import com.app.rotatio.controller.exception.ArchiveProcessException;
import com.app.rotatio.controller.exception.WorkingDayNotFoundException;
import com.app.rotatio.domain.*;
import com.app.rotatio.repository.ArchiveRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArchiveServiceTest {

    @Mock
    private ArchiveRepository archiveRepository;

    @Mock
    private WorkingDayService workingDayService;

    @InjectMocks
    private ArchiveService archiveService;

    private WorkingDay workingDay;
    private Archive archive;
    private Worker worker;
    private Task task;
    private Workplace workplace;

    @BeforeEach
    void setUp() {
        task = Task.builder()
                .id(1L)
                .name("Task1")
                .description("Description")
                .build();
        workplace = Workplace.builder()
                .id(1L)
                .designation("Workplace1")
                .active(true)
                .build();
        worker = Worker.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .task(task)
                .workplace(workplace)
                .build();
        workingDay = WorkingDay.builder()
                .id(1L)
                .workers(List.of(worker))
                .created(LocalDate.now())
                .planned(true)
                .build();
        archive = Archive.builder()
                .workingDayId(1L)
                .workersData("John Doe, Task1, Workplace1")
                .build();
    }

    @Test
    void shouldSaveArchiveSuccessfully() {
        // Given
        when(archiveRepository.save(any(Archive.class))).thenReturn(archive);

        // When
        Archive savedArchive = archiveService.saveArchive(archive);

        // Then
        assertNotNull(savedArchive);
        assertEquals(archive.getWorkingDayId(), savedArchive.getWorkingDayId());
        verify(archiveRepository, times(1)).save(archive);
    }

    @Test
    void shouldGetArchiveByIdSuccessfully() throws ArchiveNotFoundException {
        // Given
        when(archiveRepository.findById(1L)).thenReturn(Optional.of(archive));

        // When
        Archive foundArchive = archiveService.getArchiveById(1L);

        // Then
        assertNotNull(foundArchive);
        assertEquals(archive.getWorkingDayId(), foundArchive.getWorkingDayId());
        verify(archiveRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowArchiveNotFoundExceptionWhenArchiveNotFound() {
        // Given
        when(archiveRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ArchiveNotFoundException.class, () -> archiveService.getArchiveById(1L));
        verify(archiveRepository, times(1)).findById(1L);
    }

    @Test
    void shouldDeleteArchiveByIdSuccessfully() {
        // When
        archiveService.deleteArchiveById(1L);

        // Then
        verify(archiveRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldArchiveSuccessfully() throws WorkingDayNotFoundException, ArchiveProcessException {
        // Given
        when(workingDayService.getWorkingDayById(1L)).thenReturn(workingDay);
        when(archiveRepository.save(any(Archive.class))).thenReturn(archive);

        // When
        Archive createdArchive = archiveService.archive(1L);

        // Then
        assertNotNull(createdArchive);
        assertEquals("John Doe, Task1, Workplace1", createdArchive.getWorkersData());
        verify(workingDayService, times(1)).getWorkingDayById(1L);
        verify(archiveRepository, times(1)).save(any(Archive.class));
    }

    @Test
    void shouldThrowArchiveProcessExceptionOnFailure() throws WorkingDayNotFoundException {
        // Given
        when(workingDayService.getWorkingDayById(1L)).thenReturn(null);

        // When & Then
        assertThrows(ArchiveProcessException.class, () -> archiveService.archive(1L));
        verify(workingDayService, times(1)).getWorkingDayById(1L);
        verify(archiveRepository, times(0)).save(any(Archive.class));
    }
}