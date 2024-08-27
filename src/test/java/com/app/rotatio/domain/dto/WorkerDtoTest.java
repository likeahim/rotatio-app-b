package com.app.rotatio.domain.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class WorkerDtoTest {

    @Test
    void testEquals_sameObject() {
        WorkerDto worker1 = new WorkerDto(1L, 123L, "John", "Doe", 1,
                10L, 20L, 30L);
        WorkerDto worker2 = worker1;
        assertTrue(worker1.equals(worker2));
    }

    @Test
    void testEquals_nullObject() {
        WorkerDto worker1 = new WorkerDto(1L, 123L, "John", "Doe", 1,
                10L, 20L, 30L);
        assertThat(worker1).isNotEqualTo(null);
    }

    @Test
    void testEquals_differentClass() {
        WorkerDto worker1 = new WorkerDto(1L, 123L, "John", "Doe", 1,
                10L, 20L, 30L);
        String anotherType = "Not a WorkerDto";
        assertThat(worker1).isNotEqualTo(anotherType);
    }

    @Test
    void testEquals_equalObjects() {
        WorkerDto worker1 = new WorkerDto(1L, 123L, "John", "Doe", 1,
                10L, 20L, 30L);
        WorkerDto worker2 = new WorkerDto(1L, 123L, "John", "Doe", 1,
                10L, 20L, 30L);
        assertThat(worker1).isEqualTo(worker2);
    }

    @Test
    void testEquals_differentId() {
        WorkerDto worker1 = new WorkerDto(1L, 123L, "John", "Doe", 1,
                10L, 20L, 30L);
        WorkerDto worker2 = new WorkerDto(2L, 123L, "John", "Doe", 1,
                10L, 20L, 30L);
        assertThat(worker1).isNotEqualTo(worker2);
    }

    @Test
    void testEquals_differentWorkerNumber() {
        WorkerDto worker1 = new WorkerDto(1L, 123L, "John", "Doe", 1,
                10L, 20L, 30L);
        WorkerDto worker2 = new WorkerDto(1L, 124L, "John", "Doe", 1,
                10L, 20L, 30L);
        assertThat(worker1).isNotEqualTo(worker2);
    }

    @Test
    void testEquals_differentFirstName() {
        WorkerDto worker1 = new WorkerDto(1L, 123L, "John", "Doe", 1,
                10L, 20L, 30L);
        WorkerDto worker2 = new WorkerDto(1L, 123L, "Jane", "Doe", 1,
                10L, 20L, 30L);
        assertThat(worker1).isNotEqualTo(worker2);
    }

    @Test
    void testEquals_differentLastName() {
        WorkerDto worker1 = new WorkerDto(1L, 123L, "John", "Doe", 1,
                10L, 20L, 30L);
        WorkerDto worker2 = new WorkerDto(1L, 123L, "John", "Smith", 1,
                10L, 20L, 30L);
        assertThat(worker1).isNotEqualTo(worker2);
    }

    @Test
    void testEquals_differentStatus() {
        WorkerDto worker1 = new WorkerDto(1L, 123L, "John", "Doe", 1,
                10L, 20L, 30L);
        WorkerDto worker2 = new WorkerDto(1L, 123L, "John", "Doe", 2,
                10L, 20L, 30L);
        assertThat(worker1).isNotEqualTo(worker2);
    }

    @Test
    void testHashCode_equalObjects() {
        WorkerDto worker1 = new WorkerDto(1L, 123L, "John", "Doe", 1,
                10L, 20L, 30L);
        WorkerDto worker2 = new WorkerDto(1L, 123L, "John", "Doe", 1,
                10L, 20L, 30L);
        assertThat(worker1.hashCode()).isEqualTo(worker2.hashCode());
    }

    @Test
    void testHashCode_differentObjects() {
        WorkerDto worker1 = new WorkerDto(1L, 123L, "John", "Doe", 1,
                10L, 20L, 30L);
        WorkerDto worker2 = new WorkerDto(2L, 124L, "Jane", "Smith", 2,
                11L, 21L, 31L);
        assertThat(worker1.hashCode()).isNotEqualTo(worker2.hashCode());
    }
}