package com.app.rotatio.domain;

import com.app.rotatio.repository.TaskRepository;
import com.app.rotatio.repository.WorkingDayRepository;
import com.app.rotatio.repository.WorkplaceRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TaskEntityTests {

    @Autowired
    private TaskRepository taskRepository;

    private Task task;

    @BeforeEach
    void setUp() {
        task = Task.builder()
                .name("Test task")
                .description("Test description")
                .isPerformed(true)
                .build();
    }

    @Nested
    class TasksRepositorySimpleTests {

        @Test
        void shouldCreateTask() {
            //Given
            Long taskId = task.getId();
            //When
            taskRepository.save(task);
            //Then
            assertNotNull(task.getId());
            assertNotEquals(taskId, task.getId());
            assertTrue(taskRepository.findById(task.getId()).isPresent());
        }

        @Test
        void shouldThrowException() {
            //Given
            taskRepository.save(task);
            Task secondTask = Task.builder()
                    .name("Test task")
                    .build();

            //When&Then
            assertThrows(Exception.class, () -> taskRepository.saveAndFlush(secondTask));
        }

        @Test
        void shouldUpdateTask() {
            //Given
            taskRepository.save(task);
            task.setDescription("Updated description");
            task.setName("Updated name");
            task.setPerformed(false);
            //When
            Task savedTask = taskRepository.save(task);
            //Then
            assertEquals("Updated description", savedTask.getDescription());
            assertEquals("Updated name", savedTask.getName());
            assertFalse(savedTask.isPerformed());
        }

        @Test
        void shouldFetchTaskById() {
            //Given
            taskRepository.save(task);
            Long taskId = task.getId();
            //When
            Task taskById = taskRepository.findById(taskId).orElseThrow();
            //Then
            assertEquals(taskId, taskById.getId());
            assertNotNull(taskById);
            assertTrue(taskById.isPerformed());
        }

        @Test
        void shouldFetchTaskByName() {
            //Given
            taskRepository.save(task);
            String taskName = task.getName();
            //When
            Task taskByName = taskRepository.findByName(taskName).orElseThrow();
            //Then
            assertEquals(taskName, taskByName.getName());
            assertNotNull(taskByName);
        }

        @Test
        void shouldFetchAllTasks() {
            //Given
            Task secondTask = Task.builder()
                    .name("Second task")
                    .build();
            taskRepository.save(task);
            taskRepository.save(secondTask);
            //When
            List<Task> tasks = taskRepository.findAll();
            //Then
            assertEquals(2, tasks.size());
            assertTrue(tasks.contains(task));
            assertTrue(tasks.contains(secondTask));
        }

        @Test
        void shouldDeleteTask() {
            //Given
            taskRepository.save(task);
            Long taskId = task.getId();
            //When
            taskRepository.delete(task);
            //Then
            Optional<Task> taskById = taskRepository.findById(taskId);
            assertFalse(taskById.isPresent());
        }
    }

    @Nested
    class TasksAndWorkingDayRelationalTests {

        @Autowired
        private WorkingDayRepository workingDayRepository;

        private WorkingDay workingDay;
        private LocalDate date;

        @BeforeEach
        void setUp() {
            date = LocalDate.now();
            workingDay = WorkingDay.builder()
                    .created(date)
                    .planned(false)
                    .build();
            task.setWorkingDay(workingDay);
        }

        @Test
        void shouldCreateTaskWithWorkingDay() {
            //Given
            Long workingDayId = workingDay.getId();
            //When
            taskRepository.save(task);
            //Then
            Task foundTask = taskRepository.findById(task.getId()).orElseThrow();
            LocalDate created = foundTask.getWorkingDay().getCreated();
            assertEquals(date, created);
            assertNotNull(foundTask.getWorkingDay());
            assertNull(workingDayId);
            assertNotEquals(0, foundTask.getWorkingDay().getId());
        }

        @Test
        void shouldDeleteOnlyTask() {
            //Given
            Task savedTask = taskRepository.save(task);
            Long workingDayId = savedTask.getWorkingDay().getId();
            //When
            taskRepository.delete(task);
            //Then
            Optional<Task> taskById = taskRepository.findById(task.getId());
            assertFalse(taskById.isPresent());
            assertNotNull(workingDayId);
            assertTrue(workingDayRepository.findById(workingDayId).isPresent());
        }

        @Test
        void shouldFetchTaskByWorkingDay() {
            //Given
            taskRepository.save(task);
            Long taskId = task.getId();
            WorkingDay savedWorkingDay = task.getWorkingDay();
            //When
            Task taskByWorkingDay = taskRepository.findByWorkingDay(savedWorkingDay).orElseThrow();
            //Then
            assertEquals(taskId, taskByWorkingDay.getId());
            assertNotNull(taskByWorkingDay);
            assertNotNull(taskByWorkingDay.getWorkingDay());
        }

        @Test
        void shouldFetchTasksListByWorkingDay() {
            //Given
            Task anotherTask = Task.builder()
                    .name("Second task")
                    .workingDay(workingDay)
                    .build();
            taskRepository.save(anotherTask);
            taskRepository.save(task);
            WorkingDay savedWorkingDay = task.getWorkingDay();
            //When
            List<Task> allTasksByWorkingDay = taskRepository.findAllByWorkingDay(savedWorkingDay);
            //Then
            List<Task> secondTaskOnly = allTasksByWorkingDay.stream()
                    .filter(t -> t.getName().equals("Second task"))
                    .toList();
            assertEquals(2, allTasksByWorkingDay.size());
            assertEquals(anotherTask.getId(), secondTaskOnly.get(0).getId());
        }
    }

    @Nested
    class TasksAndWorkplacesRelationalTests {

        @Autowired
        WorkplaceRepository workplaceRepository;

        @BeforeEach
        void setUp() {
            task.setWorkplaces(new ArrayList<>());
            Workplace workplace = Workplace.builder()
                    .designation("Test designation")
                    .build();
            task.getWorkplaces().add(workplace);
        }

        @Test
        void shouldCreateTaskWithWorkplace() {
            //Given
            //When
            taskRepository.save(task);
            //Then
            List<Workplace> workplaceList = task.getWorkplaces().stream()
                    .filter(w -> w.getId() != null)
                    .toList();
            assertEquals(1, workplaceList.size());
        }

        @Test
        void shouldDeleteTaskOnly() {
            //Given
            taskRepository.save(task);
            Long taskId = task.getId();
            Long workplaceId = task.getWorkplaces().get(0).getId();
            //When
            taskRepository.delete(task);
            //Then
            Optional<Task> optionalTask = taskRepository.findById(taskId);
            assertFalse(optionalTask.isPresent());
            assertTrue(workplaceRepository.findById(workplaceId).isPresent());
        }

        @Test
        void shouldFetchTaskWithWorkplace() {
            //Given
            taskRepository.save(task);
            Long taskId = task.getId();
            //When
            Task foundTask = taskRepository.findById(taskId).orElseThrow();
            //Then
            assertEquals(1, foundTask.getWorkplaces().size());
            assertEquals("Test designation", foundTask.getWorkplaces().get(0).getDesignation());
        }
    }
}