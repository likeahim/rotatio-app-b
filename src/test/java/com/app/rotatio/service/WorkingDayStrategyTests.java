package com.app.rotatio.service;

import com.app.rotatio.domain.*;
import com.app.rotatio.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class WorkingDayStrategyTests {

    @Autowired
    private WorkingDayService workingDayService;

    @Autowired
    private TaskRepository taskRepository;

    private WorkingDay workingDay;
    private Task task;
    private Workplace workplace;
    private Worker worker;

    @BeforeEach
    void setUp() {
        workingDay = WorkingDay.builder()
                .created(LocalDate.now())
                .build();
        workplace = Workplace.builder()
                .designation("Task place")
                .build();
        task = Task.builder()
                .name("Testing")
                .build();
        worker = Worker.builder()
                .workerNumber(111L)
                .firstName("John")
                .lastName("Tester")
                .status(WorkerStatus.PRESENT)
                .build();
    }

    @Test
    void shouldAddItemToListBasedOnItsType() {
        //Given
        //When
        workingDayService.addItemToList(workingDay, workplace);
        workingDayService.addItemToList(workingDay, task);
        workingDayService.addItemToList(workingDay, worker);
        //Then
        assertEquals(1, workingDay.getWorkplaces().size());
        assertEquals(1, workingDay.getTasks().size());
        assertEquals(1, workingDay.getWorkers().size());
    }
    @Test
    void shouldRemoveItemFromListBasedOnItsType() {
        //Given
        Task secondTask = Task.builder()
                                  .name("Packing")
                                  .build();
        workingDayService.addItemToList(workingDay, workplace);
        workingDayService.addItemToList(workingDay, task);
        workingDayService.addItemToList(workingDay, secondTask);
        workingDayService.addItemToList(workingDay, worker);
        int tasksNumberBeforeRemove = workingDay.getTasks().size();
        //When
        workingDayService.removeItemFromList(workingDay, secondTask);
        workingDayService.removeItemFromList(workingDay, workplace);
        workingDayService.removeItemFromList(workingDay, worker);
        //Then
        assertTrue(workingDay.getWorkplaces().isEmpty());
        assertEquals(1, workingDay.getTasks().size());
        assertEquals(0, workingDay.getWorkers().size());
        assertNotEquals(tasksNumberBeforeRemove, workingDay.getTasks().size());
    }

    @Test
    void shouldSetWorkingDayInAddedItem() {
        //Given
        //When
        workingDayService.addItemToList(workingDay, workplace);
        workingDayService.saveWorkingDay(workingDay);
        //Then
        assertNotNull(workplace.getWorkingDay());
    }

    @Test
    void shouldSetWorkingDayAsNullInRemovedItem() {
        //Given
        workingDayService.addItemToList(workingDay, workplace);
        workingDayService.addItemToList(workingDay, task);
        //When
        workingDayService.removeItemFromList(workingDay, task);
        workingDayService.saveWorkingDay(workingDay);
        //Then
        assertNull(task.getWorkingDay());
        assertNotNull(workplace.getWorkingDay());
    }

    @Test
    void shouldPersistItemBySavingWorkingDay() {
        //Given
        workingDayService.addItemToList(workingDay, workplace);
        //When
        workingDayService.saveWorkingDay(workingDay);
        //Then
        Long workplaceId = workplace.getId();
        assertNotNull(workplaceId);
    }

    @Test
    void shouldNotDeleteItemByRemovingFromWorkingDay() {
        //Given
        workingDayService.addItemToList(workingDay, task);
        workingDayService.saveWorkingDay(workingDay);
        Long taskId = task.getId();
        //When
        workingDayService.removeItemFromList(workingDay, task);
        workingDayService.saveWorkingDay(workingDay);
        //Then
        assertNotNull(taskId);
        Optional<Task> taskById = taskRepository.findById(taskId);
        assertNull(task.getWorkingDay());
        assertTrue(taskById.isPresent());
    }
}