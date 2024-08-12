package com.app.rotatio.domain;

import com.app.rotatio.repository.WorkingDayArchivesRepository;
import com.app.rotatio.repository.WorkingDayRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class WorkingDayArchivesEntityTests {

    @Autowired
    private WorkingDayArchivesRepository repository;
    @Autowired
    private WorkingDayRepository workingDayRepository;

    private WorkingDayArchives archives;

    @BeforeEach
    void setUp() {
        archives = new WorkingDayArchives();
    }

    @Test
    void shouldCreateArchives() {
        //Given
        Long beforeSaveId = archives.getId();
        //When
        repository.save(archives);
        //Then
        assertNotEquals(beforeSaveId, archives.getId());
        assertFalse(repository.findAll().isEmpty());
    }

    @Test
    void shouldUpdateArchives() {
        //Given
        repository.save(archives);
        archives.setExecuted(LocalDate.now().plusDays(1));
        //When
        repository.save(archives);
        //Then
        assertEquals(LocalDate.now().plusDays(1), archives.getExecuted());
    }

    @Test
    void shouldDeleteArchives() {
        //Given
        repository.save(archives);
        Long archivesId = archives.getId();
        //When
        repository.delete(archives);
        //Then
        assertFalse(repository.findById(archivesId).isPresent());
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void shouldFetchAllArchives() {
        //Given
        repository.save(archives);
        repository.save(new WorkingDayArchives());
        //When
        List<WorkingDayArchives> allArchives = repository.findAll();
        //Then
        assertEquals(2, allArchives.size());
        assertTrue(allArchives.contains(archives));
    }

    @Test
    void shouldFetchEmptyList() {
        //Given
        //When
        List<WorkingDayArchives> all = repository.findAll();
        //Then
        assertEquals(0, all.size());
    }

    @Test
    void shouldFetchArchiveById() {
        //Given
        archives.setExecuted(LocalDate.now());
        repository.save(archives);
        Long archivesId = archives.getId();
        //When
        Optional<WorkingDayArchives> byId = repository.findById(archivesId);
        //Then
        assertTrue(byId.isPresent());
        assertEquals(LocalDate.now(), byId.get().getExecuted());
    }

    @Test
    void shouldFetchEmptyOptional() {
        //Given
        repository.save(archives);
        Long archivesId = archives.getId();
        //When
        Optional<WorkingDayArchives> byId = repository.findById(archivesId + 10);
        //Then
        assertFalse(byId.isPresent());
    }

    @Test
    void shouldFetchArchivesListByExecuted() {
        //Given
        archives.setExecuted(LocalDate.now());
        repository.save(archives);
        //When
        List<WorkingDayArchives> byExecuted = repository.findByExecuted(LocalDate.now());
        //Then
        assertEquals(1, byExecuted.size());
        assertTrue(byExecuted.contains(archives));
    }

    @Test
    void shouldFetchArchivesByWorkingDay() {
        //Given
        WorkingDay workingDay = WorkingDay.builder()
                .created(LocalDate.now())
                .build();
        workingDayRepository.save(workingDay);
        archives.setWorkingDay(workingDay);
        repository.save(archives);
        //When
        Optional<WorkingDayArchives> byWorkingDay = repository.findByWorkingDay(workingDay);
        //Then
        assertTrue(byWorkingDay.isPresent());
        assertEquals(workingDay, byWorkingDay.get().getWorkingDay());
    }
}