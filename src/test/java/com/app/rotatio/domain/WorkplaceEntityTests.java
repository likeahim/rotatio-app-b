package com.app.rotatio.domain;

import com.app.rotatio.repository.TaskRepository;
import com.app.rotatio.repository.WorkerRepository;
import com.app.rotatio.repository.WorkingDayRepository;
import com.app.rotatio.repository.WorkplaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
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
class WorkplaceEntityTests {

    @Autowired
    private WorkplaceRepository workplaceRepository;
    @Autowired
    private WorkingDayRepository workingDayRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private WorkerRepository workerRepository;

    private Workplace workplace;

    @BeforeEach
    void setUp() {
        workplace = Workplace.builder()
                .designation("Test")
                .build();
    }

    @Nested
    class WorkplaceRepositorySimpleTests {

        @Test
        void shouldCreateWorkplace() {
            //Given
            Long idBeforeSave = workplace.getId();
            //When
            workplaceRepository.save(workplace);
            //Then
            assertNotEquals(idBeforeSave, workplace.getId());
            assertTrue(workplaceRepository.findById(workplace.getId()).isPresent());
        }

        @Test
        void shouldUpdateWorkplace() {
            //Given
            workplaceRepository.save(workplace);
            String designationBefore = workplace.getDesignation();
            boolean activeBefore = workplace.isActive();
            boolean nowUsedBefore = workplace.isNowUsed();
            workplace.setDesignation("Updated");
            workplace.setActive(true);
            workplace.setNowUsed(true);
            //When
            workplaceRepository.save(workplace);
            //Then
            assertNotEquals(designationBefore, workplace.getDesignation());
            assertNotEquals(activeBefore, workplace.isActive());
            assertNotEquals(nowUsedBefore, workplace.isNowUsed());
        }

        @Test
        void shouldDeleteWorkplace() {
            //Given
            workplaceRepository.save(workplace);
            Long workplaceId = workplace.getId();
            //When
            workplaceRepository.delete(workplace);
            //Then
            assertNotNull(workplaceId);
            assertFalse(workplaceRepository.findById(workplaceId).isPresent());
        }
    }

    @Nested
    class WorkplaceFetchingTests {

        private Workplace secondWorkplace;

        @BeforeEach
        void setUp() {
            secondWorkplace = Workplace.builder()
                    .nowUsed(true)
                    .active(true)
                    .designation("Second workplace")
                    .build();
        }

        @Test
        void shouldFetchWorkplace() {
            //Given
            workplaceRepository.save(workplace);
            Long workplaceId = workplace.getId();
            //When
            Optional<Workplace> foundById = workplaceRepository.findById(workplaceId);
            //Then
            assertTrue(foundById.isPresent());
            assertEquals("Test", foundById.get().getDesignation());
        }

        @Test
        void shouldFetchAllWorkplaces() {
            //Given
            workplaceRepository.save(workplace);
            workplaceRepository.save(secondWorkplace);
            Long workplaceId = workplace.getId();
            Long secondWorkplaceId = secondWorkplace.getId();
            //When
            List<Workplace> allWorkplaces = workplaceRepository.findAll();
            //Then
            long count = allWorkplaces.stream()
                    .filter(w -> w.getId().equals(workplaceId))
                    .count();
            assertEquals(2, allWorkplaces.size());
            assertTrue(allWorkplaces.contains(workplace));
            assertTrue(allWorkplaces.contains(secondWorkplace));
            assertEquals(1, count);
        }

        @Test
        void shouldFetchActiveWorkplaces() {
            //Given
            workplaceRepository.save(workplace);
            workplaceRepository.save(secondWorkplace);
            //When
            List<Workplace> activeWorkplaces = workplaceRepository.findByActive(true);
            //Then
            assertEquals(1, activeWorkplaces.size());
            assertFalse(activeWorkplaces.contains(workplace));
        }

        @Test
        void shouldFetchNotActiveWorkplaces() {
            //Given
            workplaceRepository.save(workplace);
            secondWorkplace.setActive(false);
            workplaceRepository.save(secondWorkplace);
            //When
            List<Workplace> notActiveWorkplaces = workplaceRepository.findByActive(false);
            //Then
            assertEquals(2, notActiveWorkplaces.size());
            assertTrue(notActiveWorkplaces.contains(workplace));
            assertTrue(notActiveWorkplaces.contains(secondWorkplace));
        }

        @Test
        void shouldFetchNowUsedWorkplaces() {
            //Given
            workplaceRepository.save(workplace);
            secondWorkplace.setNowUsed(true);
            workplaceRepository.save(secondWorkplace);
            //When
            List<Workplace> nowUsedWorkplaces = workplaceRepository.findByNowUsed(true);
            //Then
            assertEquals(1, nowUsedWorkplaces.size());
            assertFalse(nowUsedWorkplaces.contains(workplace));
            assertTrue(nowUsedWorkplaces.contains(secondWorkplace));
        }

        @Test
        void shouldFetchNotUsedWorkplaces() {
            //Given
            workplaceRepository.save(workplace);
            secondWorkplace.setNowUsed(true);
            workplaceRepository.save(secondWorkplace);
            //When
            List<Workplace> notUsedWorkplaces = workplaceRepository.findByNowUsed(false);
            //Then
            assertEquals(1, notUsedWorkplaces.size());
            assertTrue(notUsedWorkplaces.contains(workplace));
            assertFalse(notUsedWorkplaces.contains(secondWorkplace));
        }

        @Test
        void shouldFetchWorkplacesByDesignation() {
            //Given
            workplaceRepository.save(workplace);
            workplaceRepository.save(secondWorkplace);
            //When
            List<Workplace> workplaces = workplaceRepository.findByDesignation("Second workplace");
            //Then
            assertEquals(1, workplaces.size());
            assertFalse(workplaces.contains(workplace));
            assertTrue(workplaces.contains(secondWorkplace));
        }

        @Test
        void shouldFetchEmptyListByDesignation() {
            //Given
            workplaceRepository.save(workplace);
            workplaceRepository.save(secondWorkplace);
            //When
            List<Workplace> workplaces = workplaceRepository.findByDesignation("false name");
            //Then
            assertTrue(workplaces.isEmpty());
        }
    }

    @Nested
    class WorkplaceAndTaskRelationalTests {

        private Task task;

        @BeforeEach
        void setUp() {
            task = Task.builder()
                    .name("Testing")
                    .build();
        }

        @Test
        void shouldNotCreateTaskWithWorkplaceAndThrowsException() {
            //Given
            workplace.setTask(task);
            //When
            workplaceRepository.save(workplace);
            //Then
            assertThrows(Exception.class, () -> taskRepository.findAll());
        }

        @Test
        void shouldNotDeleteTaskWithWorkplace() {
            //Given
            taskRepository.save(task);
            workplace.setTask(task);
            workplaceRepository.save(workplace);
            //When
            workplaceRepository.delete(workplace);
            //Then
            List<Task> allTasks = taskRepository.findAll();
            assertEquals(1, allTasks.size());
            assertTrue(allTasks.contains(task));
            assertFalse(workplaceRepository.findById(workplace.getId()).isPresent());
        }

        @Test
        void shouldFetchWorkplacesByTask() {
            //Given
            taskRepository.save(task);
            workplace.setTask(task);
            workplaceRepository.save(workplace);
            //When
            List<Workplace> workplacesByTask = workplaceRepository.findByTask(task);
            //Then
            assertEquals(1, workplacesByTask.size());
            assertTrue(workplacesByTask.contains(workplace));
        }

        @Test
        void shouldFetchEmptyListByTask() {
            //Given
            taskRepository.save(task);
            workplaceRepository.save(workplace);
            //When
            List<Workplace> workplacesByTask = workplaceRepository.findByTask(task);
            //Then
            assertEquals(0, workplacesByTask.size());
            assertNull(workplace.getTask());
        }
    }

    @Nested
    class WorkplaceAndWorkingDayRelationalTests {

        private WorkingDay workingDay;

        @BeforeEach
        void setUp() {
            workingDay = WorkingDay.builder()
                    .created(LocalDate.now())
                    .build();
        }

        @Test
        void shouldNotCreateWorkingDayWithWorkplaceAndThrowsException() {
            //Given
            workplace.setWorkingDay(workingDay);
            //When
            workplaceRepository.save(workplace);
            //Then
            assertThrows(Exception.class, () -> workingDayRepository.findAll());
        }

        @Test
        void shouldNotDeleteWorkingDayWithWorkplace() {
            //Given
            workingDayRepository.save(workingDay);
            workplace.setWorkingDay(workingDay);
            workplaceRepository.save(workplace);
            //When
            workplaceRepository.delete(workplace);
            //Then
            List<WorkingDay> allWorkingDays = workingDayRepository.findAll();
            assertEquals(0, workplaceRepository.findAll().size());
            assertEquals(1, allWorkingDays.size());
            assertTrue(allWorkingDays.contains(workingDay));
        }

        @Test
        void shouldFetchWorkplacesByWorkingDay() {
            //Given
            workingDayRepository.save(workingDay);
            workplace.setWorkingDay(workingDay);
            workplaceRepository.save(workplace);
            //When
            List<Workplace> workplacesByWorkingDay = workplaceRepository.findByWorkingDay(workingDay);
            //Then
            assertEquals(1, workplacesByWorkingDay.size());
            assertTrue(workplacesByWorkingDay.contains(workplace));
        }

        @Test
        void shouldFetchEmptyListByWorkingDay() {
            //Given
            workingDayRepository.save(workingDay);
            workplaceRepository.save(workplace);
            //When
            List<Workplace> workplacesByWorkingDay = workplaceRepository.findByWorkingDay(workingDay);
            //Then
            assertEquals(0, workplacesByWorkingDay.size());
            assertNull(workplace.getWorkingDay());
        }
    }

    @Nested
    class WorkplaceAndWorkerRelationalTests {

        private Worker worker;

        @BeforeEach
        void setUp() {
            worker = Worker.builder()
                    .workerNumber(11111L)
                    .firstName("John")
                    .lastName("Tester")
                    .status(WorkerStatus.PRESENT)
                    .build();
        }

        @Test
        void shouldCreateEmptyListWithWorkplace() {
            //Given
            workplaceRepository.save(workplace);
            //When
            List<Worker> workers = workplaceRepository.findById(workplace.getId()).orElseThrow().getWorkers();
            //Then
            assertEquals(0, workers.size());
        }

        @Test
        void shouldNotCreateWorkerWithWorkplaceAndThrowsException() {
            //Given
            workplace.getWorkers().add(worker);
            //When
            workplaceRepository.save(workplace);
            //Then
            Long workerId = workplace.getWorkers().get(0).getId();
            assertNull(workerId);
        }

        @Test
        void shouldNotDeleteWorkerWithWorkplace() {
            //Given
            workerRepository.save(worker);
            workplace.getWorkers().add(worker);
            workplaceRepository.save(workplace);
            //When
            workplaceRepository.delete(workplace);
            //Then
            Optional<Worker> workerById = workerRepository.findById(worker.getId());
            assertTrue(workerById.isPresent());
        }
    }
}