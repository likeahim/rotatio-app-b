package com.app.rotatio.controller;

import com.app.rotatio.controller.exception.WorkerNotFoundException;
import com.app.rotatio.domain.Worker;
import com.app.rotatio.domain.WorkerStatus;
import com.app.rotatio.domain.dto.WorkerDto;
import com.app.rotatio.mapper.WorkerMapper;
import com.app.rotatio.service.WorkerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitWebConfig
@WebMvcTest(WorkerController.class)
public class WorkerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WorkerService workerService;

    @MockBean
    private WorkerMapper workerMapper;

    private WorkerDto workerDto;
    private Worker worker;
    private String json;
    private ObjectMapper mapper;
    private List<Worker> workers;
    private List<WorkerDto> workerDtos;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        workerDto = new WorkerDto(
                1L, 11L, "John", "Smith", 1, LocalDate.now(),
                LocalDate.now(), 1L, 1L, 1L
        );
        worker = Worker.builder()
                .id(1L)
                .workerNumber(11L)
                .firstName("John")
                .lastName("Smith")
                .status(WorkerStatus.PRESENT)
                .build();
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        json = mapper.writeValueAsString(workerDto);
        workers = new ArrayList<>(List.of(worker));
        workerDtos = new ArrayList<>(List.of(workerDto));
    }

    @Test
    void shouldCreateWorker() throws Exception {
        //Given
        when(workerMapper.mapToWorker(workerDto)).thenReturn(worker);
        when(workerService.saveWorker(any(Worker.class))).thenReturn(worker);
        when(workerMapper.mapToWorkerDto(any(Worker.class))).thenReturn(workerDto);
        //When
        //Then
        mockMvc.perform(post("/v1/rotatio/workers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void shouldUpdateWorker() throws Exception {
        //Given
        worker.setFirstName("Mark");
        WorkerDto updated = workerDto = new WorkerDto(
                1L, 11L, "Mark", "Smith", 1, null,
                null, 1L, 1L, 1L
        );
        String updatedJson = mapper.writeValueAsString(updated);
        when(workerMapper.mapToWorker(workerDto)).thenReturn(worker);
        when(workerService.saveWorker(any(Worker.class))).thenReturn(worker);
        when(workerMapper.mapToWorkerDto(any(Worker.class))).thenReturn(updated);
        //When
        //Then
        mockMvc.perform(put("/v1/rotatio/workers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Mark"));
    }

    @Test
    void shouldUpdateWorkerStatus() throws Exception {
        //Given
        when(workerService.updateStatus(anyLong(), anyInt())).thenReturn(worker);
        when(workerMapper.mapToWorkerDto(any(Worker.class))).thenReturn(workerDto);
        //When
        //Then
        mockMvc.perform(patch("/v1/rotatio/workers/updateStatus/1/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(1));
    }

    @Test
    void shouldDeleteWorker() throws Exception {
        //Given
        WorkerDto unemployed = new WorkerDto(
                1L, 11L, "Mark", "Smith", 3, null,
                null, 1L, 1L, 1L
        );
        when(workerService.deleteWorker(anyLong())).thenReturn(worker);
        when(workerMapper.mapToWorkerDto(any(Worker.class))).thenReturn(unemployed);
        //When
        //Then
        mockMvc.perform(delete("/v1/rotatio/workers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(3));
    }

    @Test
    void shouldGetAllWorkers() throws Exception {
        //Given
        when(workerService.getAllWorkers()).thenReturn(workers);
        when(workerMapper.mapToWorkerDtoList(any())).thenReturn(workerDtos);
        //When
        //Then
        mockMvc.perform(get("/v1/rotatio/workers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void shouldGetWorkerById() throws Exception {
        //Given
        when(workerService.getWorkerById(anyLong())).thenReturn(worker);
        when(workerMapper.mapToWorkerDto(any(Worker.class))).thenReturn(workerDto);
        //When
        //Then
        mockMvc.perform(get("/v1/rotatio/workers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastname").value("Smith"));
    }

    @Test
    void shouldGetWorkersByStatus() throws Exception {
        //Given
        String date = LocalDate.now().toString();
        when(workerService.getWorkersByStatus(anyInt())).thenReturn(workers);
        when(workerMapper.mapToWorkerDtoList(any())).thenReturn(workerDtos);
        //When
        //Then
        mockMvc.perform(get("/v1/rotatio/workers/byStatus/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value(1))
                .andExpect(jsonPath("$[0].workerNumber").value(11))
                .andExpect(jsonPath("$[0].presenceFrom").value(date))
                .andExpect(jsonPath("$[0].absenceFrom").value(date))
                .andExpect(jsonPath("$[0].workingDayId").value(1))
                .andExpect(jsonPath("$[0].taskId").value(1))
                .andExpect(jsonPath("$[0].workplaceId").value(1));
    }

    @Test
    void shouldGetWorkersByPresenceBefore() throws Exception {
        //Given
        when(workerService.getWorkersByPresenceFromBefore(any(LocalDate.class))).thenReturn(workers);
        when(workerMapper.mapToWorkerDtoList(any())).thenReturn(workerDtos);
        String date = LocalDate.now().toString();
        String datePlusFive = LocalDate.now().plusDays(5).toString();
        //When
        //Then
        mockMvc.perform(get("/v1/rotatio/workers/byPresenceBefore/" + datePlusFive))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].presenceFrom").value(date))
                .andExpect(jsonPath("$[0].firstName").value("John"));
    }

    @Test
    void shouldGetWorkersByAbsenceFrom() throws Exception {
        //Given
        when(workerService.getWorkersByPresenceTo(any(LocalDate.class))).thenReturn(workers);
        when(workerMapper.mapToWorkerDtoList(any())).thenReturn(workerDtos);
        String date = LocalDate.now().toString();
        String datePlusFive = LocalDate.now().plusDays(5).toString();
        //When
        //Then
        mockMvc.perform(get("/v1/rotatio/workers/byAbsenceFrom/" + datePlusFive))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].absenceFrom").value(date));
    }

    @Test
    void shouldGetWorkersByWorkingDay() throws Exception {
        //Given
        when(workerService.getWorkersByWorkingDay(anyLong())).thenReturn(workers);
        when(workerMapper.mapToWorkerDtoList(any())).thenReturn(workerDtos);
        //When
        //Then
        mockMvc.perform(get("/v1/rotatio/workers/byWorkingDay/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void shouldReturnNotFoundWhenWorkerDoesNotExist() throws Exception {
        //Given
        when(workerService.getWorkerById(1L)).thenThrow(new WorkerNotFoundException());
        //When
        //Then
        mockMvc.perform(get("/v1/rotatio/workers/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Such a worker not found"));
    }

    @Test
    void shouldReturnBadRequestWhenInvalidInput() throws Exception {
        //Given
        //When
        //Then
        mockMvc.perform(post("/v1/rotatio/workers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldHandleServiceException() throws Exception {
        //Given
        when(workerService.getWorkerById(anyLong())).thenThrow(new RuntimeException("Unexpected error"));
        //When
        //Then
        mockMvc.perform(get("/v1/rotatio/workers/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Unexpected error"));
    }
}