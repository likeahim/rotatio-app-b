package com.app.rotatio.service;

import com.app.rotatio.controller.exception.WorkerNotFoundException;
import com.app.rotatio.controller.exception.WorkingDayNotFoundException;
import com.app.rotatio.domain.Worker;
import com.app.rotatio.domain.WorkerStatus;
import com.app.rotatio.domain.WorkingDay;
import com.app.rotatio.repository.WorkerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkerServiceTest {

    @Mock
    private WorkerRepository workerRepository;

    @Mock
    private WorkingDayService workingDayService;

    @InjectMocks
    private WorkerService workerService;

    private Worker worker;

    @BeforeEach
    void setUp() {
        worker = Worker.builder()
                .id(1L)
                .workerNumber(1234L)
                .firstName("John")
                .lastName("Doe")
                .status(WorkerStatus.PRESENT)
                .presenceFrom(LocalDate.now().minusDays(10))
                .absenceFrom(LocalDate.now().plusDays(30))
                .build();
    }

    @Test
    void shouldSaveWorker() {
        //Given
        when(workerRepository.save(worker)).thenReturn(worker);
        //When
        Worker savedWorker = workerService.saveWorker(worker);
        //Then
        assertNotNull(savedWorker);
        assertEquals(worker.getId(), savedWorker.getId());
        verify(workerRepository, times(1)).save(worker);
    }

    @Test
    void shouldDeleteWorker() throws WorkerNotFoundException {
        //Given
        when(workerRepository.findById(worker.getId())).thenReturn(Optional.of(worker));
        when(workerRepository.save(worker)).thenReturn(worker);
        //When
        Worker deletedWorker = workerService.deleteWorker(worker.getId());
        //Then
        assertNotNull(deletedWorker);
        assertEquals(WorkerStatus.UNEMPLOYED, deletedWorker.getStatus());
        assertEquals(LocalDate.now(), deletedWorker.getAbsenceFrom());
        verify(workerRepository, times(1)).save(worker);
    }

    @Test
    void shouldUpdateStatus() throws WorkerNotFoundException {
        //Given
        when(workerRepository.findById(worker.getId())).thenReturn(Optional.of(worker));
        when(workerRepository.save(worker)).thenReturn(worker);
        //When
        Worker updatedWorker = workerService.updateStatus(worker.getId(), WorkerStatus.UNEMPLOYED.getValue());
        //Then
        assertNotNull(updatedWorker);
        assertEquals(WorkerStatus.UNEMPLOYED, updatedWorker.getStatus());
        verify(workerRepository, times(1)).save(worker);
    }

    @Test
    void shouldUpdatePresenceFrom() throws WorkerNotFoundException {
        //Given
        LocalDate newDate = LocalDate.of(2024, 1, 1);
        when(workerRepository.findById(worker.getId())).thenReturn(Optional.of(worker));
        when(workerRepository.save(worker)).thenReturn(worker);
        //When
        Worker updatedWorker = workerService.updatePresenceFrom(worker.getId(), newDate);
        //Then
        assertNotNull(updatedWorker);
        assertEquals(newDate, updatedWorker.getPresenceFrom());
        verify(workerRepository, times(1)).save(worker);
    }

    @Test
    void shouldUpdateAbsenceFrom() throws WorkerNotFoundException {
        //Given
        LocalDate newDate = LocalDate.of(2024, 6, 1);
        when(workerRepository.findById(worker.getId())).thenReturn(Optional.of(worker));
        when(workerRepository.save(worker)).thenReturn(worker);
        //When
        Worker updatedWorker = workerService.updateAbsenceFrom(worker.getId(), newDate);
        //Then
        assertNotNull(updatedWorker);
        assertEquals(newDate, updatedWorker.getAbsenceFrom());
        verify(workerRepository, times(1)).save(worker);
    }

    @Test
    void shouldGetWorkerById() throws WorkerNotFoundException {
        //Given
        when(workerRepository.findById(worker.getId())).thenReturn(Optional.of(worker));
        //When
        Worker foundWorker = workerService.getWorkerById(worker.getId());
        //Then
        assertNotNull(foundWorker);
        assertEquals(worker.getId(), foundWorker.getId());
    }

    @Test
    void shouldThrowExceptionWhenWorkerNotFoundById() {
        //Given
        when(workerRepository.findById(worker.getId())).thenReturn(Optional.empty());
        //When
        //Then
        assertThrows(WorkerNotFoundException.class, () -> workerService.getWorkerById(worker.getId()));
    }

    @Test
    void shouldGetAllWorkers() {
        //Given
        when(workerRepository.findAll()).thenReturn(List.of(worker));
        //When
        List<Worker> workers = workerService.getAllWorkers();
        //Then
        assertNotNull(workers);
        assertEquals(1, workers.size());
    }

    @Test
    void shouldGetWorkersByStatus() {
        //Given
        when(workerRepository.findAllByStatus(WorkerStatus.PRESENT)).thenReturn(List.of(worker));
        //Ween
        List<Worker> workers = workerService.getWorkersByStatus(WorkerStatus.PRESENT.getValue());
        //Then
        assertNotNull(workers);
        assertEquals(1, workers.size());
    }

    @Test
    void shouldGetWorkersByPresenceFromBefore() {
        //Given
        LocalDate date = worker.getAbsenceFrom();
        when(workerRepository.findAllByPresenceFromBefore(date)).thenReturn(List.of(worker));
        //When
        List<Worker> workers = workerService.getWorkersByPresenceFromBefore(date);
        //Then
        assertNotNull(workers);
        assertEquals(1, workers.size());
    }

    @Test
    void shouldGetWorkersByPresenceTo() {
        //Given
        LocalDate date = worker.getAbsenceFrom();
        when(workerRepository.findAllByAbsenceFrom(date)).thenReturn(List.of(worker));
        //When
        List<Worker> workers = workerService.getWorkersByPresenceTo(date);
        //Then
        assertNotNull(workers);
        assertEquals(1, workers.size());
    }

    @Test
    void shouldFetchEmptyListByPresenceFromBefore() {
        //Given
        LocalDate date = worker.getAbsenceFrom();
        when(workerRepository.findAllByPresenceFromBefore(date)).thenReturn(List.of());
        //When
        List<Worker> workers = workerService.getWorkersByPresenceFromBefore(date);
        //Then
        assertNotNull(workers);
        assertEquals(0, workers.size());
    }

    @Test
    void shouldFetchEmptyListByPresenceTo() {
        //Given
        LocalDate date = worker.getAbsenceFrom();
        when(workerRepository.findAllByAbsenceFrom(date)).thenReturn(List.of());
        //When
        List<Worker> workers = workerService.getWorkersByPresenceTo(date);
        //Then
        assertNotNull(workers);
        assertEquals(0, workers.size());
    }

    @Test
    void shouldGetWorkersByWorkingDay() throws WorkingDayNotFoundException {
        //Given
        WorkingDay workingDay = new WorkingDay();
        workingDay.setId(1L);
        when(workingDayService.getWorkingDayById(1L)).thenReturn(workingDay);
        when(workerRepository.findAllByWorkingDay(workingDay)).thenReturn(List.of(worker));
        //When
        List<Worker> workers = workerService.getWorkersByWorkingDay(1L);
        //Then
        assertNotNull(workers);
        assertEquals(1, workers.size());
    }

    @Test
    void shouldConvertLongListToWorkersList() throws WorkerNotFoundException {
        //Given
        List<Long> workerIds = List.of(1L);
        when(workerRepository.findById(1L)).thenReturn(Optional.of(worker));
        //When
        List<Worker> workers = workerService.longToWorkersList(workerIds);
        //Then
        assertNotNull(workers);
        assertEquals(1, workers.size());
    }

    @Test
    void shouldConvertWorkersListToLongList() {
        //Given
        List<Worker> workers = List.of(worker);
        //When
        List<Long> workerIds = workerService.workersToLongList(workers);
        //Then
        assertNotNull(workerIds);
        assertEquals(1, workerIds.size());
        assertEquals(worker.getId(), workerIds.get(0));
    }
}