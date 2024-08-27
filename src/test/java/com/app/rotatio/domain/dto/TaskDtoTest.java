package com.app.rotatio.domain.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TaskDtoTest {

    @Test
    void testEquals_sameObject() {
        TaskDto task1 = new TaskDto(1L, "Task 1", "Description 1", true);
        TaskDto task2 = task1;
        assertTrue(task1.equals(task2));
    }

    @Test
    void testEquals_nullObject() {
        TaskDto task1 = new TaskDto(1L, "Task 1", "Description 1", true);
        assertThat(task1).isNotEqualTo(null);
    }

    @Test
    void testEquals_differentClass() {
        TaskDto task1 = new TaskDto(1L, "Task 1", "Description 1", true);
        String anotherType = "Not a TaskDto";
        assertThat(task1).isNotEqualTo(anotherType);
    }

    @Test
    void testEquals_equalObjects() {
        TaskDto task1 = new TaskDto(1L, "Task 1", "Description 1", true);
        TaskDto task2 = new TaskDto(1L, "Task 1", "Description 1", true);
        assertThat(task1).isEqualTo(task2);
    }

    @Test
    void testEquals_differentId() {
        TaskDto task1 = new TaskDto(1L, "Task 1", "Description 1", true);
        TaskDto task2 = new TaskDto(2L, "Task 1", "Description 1", true);
        assertThat(task1).isNotEqualTo(task2);
    }

    @Test
    void testEquals_differentName() {
        TaskDto task1 = new TaskDto(1L, "Task 1", "Description 1", true);
        TaskDto task2 = new TaskDto(1L, "Task 2", "Description 1", true);
        assertThat(task1).isNotEqualTo(task2);
    }

    @Test
    void testHashCode_equalObjects() {
        TaskDto task1 = new TaskDto(1L, "Task 1", "Description 1", true);
        TaskDto task2 = new TaskDto(1L, "Task 1", "Description 1", true);
        assertThat(task1.hashCode()).isEqualTo(task2.hashCode());
    }

    @Test
    void testHashCode_differentObjects() {
        TaskDto task1 = new TaskDto(1L, "Task 1", "Description 1", true);
        TaskDto task2 = new TaskDto(2L, "Task 1", "Description 2", false);
        assertThat(task1.hashCode()).isNotEqualTo(task2.hashCode());
    }
}