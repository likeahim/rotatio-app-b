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
class WorkerEntityTests {

    @Autowired
    private WorkerRepository workerRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private WorkingDayRepository workingDayRepository;
    @Autowired
    private WorkplaceRepository workplaceRepository;

    private Worker worker;

    @BeforeEach
    void setUp() {
        worker = Worker.builder()
                .workerNumber(752410L)
                .firstName("John")
                .lastName("Worker")
                .status(WorkerStatus.PRESENT)
                .build();
    }

    @Nested
    class WorkerRepositorySimpleTests {

        @Test
        void shouldCreateWorker() {
            //Given
            //When
            workerRepository.save(worker);
            //Then
            assertNotEquals(null, worker.getId());
            assertTrue(workerRepository.findById(worker.getId()).isPresent());
        }

        @Test
        void shouldUpdateWorker() {
            //Given
            workerRepository.save(worker);
            worker.setStatus(WorkerStatus.ABSENT);
            worker.setLastName("Updater");
            //When
            workerRepository.save(worker);
            //Then

            assertNotEquals("Smith", worker.getLastName());
            assertEquals(WorkerStatus.ABSENT, worker.getStatus());
        }

        @Test
        void shouldDeleteWorker() {
            //Given
            workerRepository.save(worker);
            Long workerId = worker.getId();
            //When
            workerRepository.delete(worker);
            //Then
            Optional<Worker> workerById = workerRepository.findById(workerId);
            List<Worker> all = workerRepository.findAll();
            assertFalse(workerById.isPresent());
        }

        @Nested
        class WorkersFetchingTests {

            private Worker secondWorker;

            @BeforeEach
            void setUp() {
                secondWorker = Worker.builder()
                        .workerNumber(90008L)
                        .firstName("Anne")
                        .lastName("Tester")
                        .status(WorkerStatus.PRESENT)
                        .build();
            }
            @Test
            void shouldFetchWorkerById() {
                //Given
                workerRepository.save(worker);
                //When
                Optional<Worker> workerById = workerRepository.findById(worker.getId());
                //Then
                assertTrue(workerById.isPresent());
            }

            @Test
            void shouldFetchAllWorkers() {
                //Given
                Worker secondWorker = Worker.builder()
                        .workerNumber(90000L)
                        .firstName("Mark")
                        .lastName("Second")
                        .status(WorkerStatus.PRESENT)
                        .build();
                workerRepository.save(worker);
                workerRepository.save(secondWorker);
                //When
                List<Worker> workers = workerRepository.findAll();
                //Then
                assertTrue(workers.contains(worker));
                assertTrue(workers.contains(secondWorker));
            }

            @Test
            void shouldFetchWorkerListByStatus() {
                //Given
                workerRepository.save(worker);
                workerRepository.save(secondWorker);
                //When
                List<Worker> allByStatus = workerRepository.findAllByStatus(WorkerStatus.PRESENT);
                //Then
                assertFalse(allByStatus.isEmpty());
                assertEquals(2, allByStatus.size());
                assertTrue(allByStatus.contains(worker));
            }

            @Test
            void shouldFetchWorkerListByWorkingDay() {
                //Given
                WorkingDay workingDay = WorkingDay.builder()
                        .created(LocalDate.now())
                        .build();
                workingDayRepository.save(workingDay);
                worker.setWorkingDay(workingDay);
                workerRepository.save(worker);
                //When
                List<Worker> allByWorkingDay = workerRepository.findAllByWorkingDay(workingDay);
                //Then
                assertFalse(allByWorkingDay.isEmpty());
                assertNotEquals(2, allByWorkingDay.size());
            }
        }

        @Nested
        class WorkerAndWorkingDayRelationalTests {

            private WorkingDay workingDay;

            @BeforeEach
            void setUp() {
                workingDay = WorkingDay.builder()
                        .created(LocalDate.now())
                        .build();
            }

            @Test
            void shouldNotCreateWorkingDayAndThrowsException() {
                //Given
                worker.setWorkingDay(workingDay);
                //When
                workerRepository.save(worker);
                //Then
                assertThrows(Exception.class, () -> workingDayRepository.findAll());
                assertNull(workingDay.getId());
            }

            @Test
            void shouldNotDeleteWorkingDayWithWorker() {
                //Given
                workingDayRepository.save(workingDay);
                worker.setWorkingDay(workingDay);
                workerRepository.save(worker);
                //When
                workerRepository.delete(worker);
                //Then
                List<WorkingDay> allWorkingDays = workingDayRepository.findAll();
                assertFalse(allWorkingDays.isEmpty());
                assertEquals(1, allWorkingDays.size());
                assertTrue(allWorkingDays.contains(workingDay));
            }
        }

        @Nested
        class WorkerAndTaskRelationalTests {

            private Task task;

            @BeforeEach
            void setUp() {
                task = Task.builder().name("Packing").build();
            }

            @Test
            void shouldNotDeleteTaskWithWorker() {
                //Given
                taskRepository.save(task);
                worker.setTask(task);
                workerRepository.save(worker);
                //When
                workerRepository.delete(worker);
                //Then
                List<Task> allTasks = taskRepository.findAll();
                assertFalse(allTasks.isEmpty());
                assertEquals(1, allTasks.size());
                assertTrue(allTasks.contains(task));
            }
        }

        @Nested
        class WorkerAndWorkplaceRelationalTests {

            private Workplace workplace;

            @BeforeEach
            void setUp() {
                workplace = Workplace.builder()
                        .designation("Test")
                        .build();
            }

            @Test
            void shouldNotDeleteWorkplaceWithWorker() {
                //Given
                workplaceRepository.save(workplace);
                worker.setWorkplace(workplace);
                workerRepository.save(worker);
                //When
                workerRepository.delete(worker);
                //Then
                List<Workplace> allWorkplaces = workplaceRepository.findAll();
                assertFalse(allWorkplaces.isEmpty());
                assertEquals(1, allWorkplaces.size());
                assertTrue(allWorkplaces.contains(workplace));
            }
        }
    }
}