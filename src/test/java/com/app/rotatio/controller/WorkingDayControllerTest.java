package com.app.rotatio.controller;

import com.app.rotatio.controller.exception.WorkingDayNotFoundException;
import com.app.rotatio.domain.WorkingDay;
import com.app.rotatio.domain.dto.WorkingDayDto;
import com.app.rotatio.mapper.WorkingDayMapper;
import com.app.rotatio.service.WorkingDayService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
@WebMvcTest(WorkingDayController.class)
public class WorkingDayControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WorkingDayService workingDayService;

    @MockBean
    private WorkingDayMapper workingDayMapper;

    private WorkingDayDto workingDayDto;
    private WorkingDay workingDay;
    private String json;
    private ObjectMapper mapper;
    private List<WorkingDay> workingDays;
    private List<WorkingDayDto> workingDayDtos;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        workingDayDto = new WorkingDayDto(1L, LocalDate.now(), LocalDate.now().plusDays(1), false,
                false, 1L, new ArrayList<>());
        workingDay = WorkingDay.builder()
                .id(1L)
                .created(LocalDate.now())
                .build();
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        json = mapper.writeValueAsString(workingDayDto);
        workingDays = new ArrayList<>(List.of(workingDay));
        workingDayDtos = new ArrayList<>(List.of(workingDayDto));
    }

    @Test
    void shouldCreateWorkingDay() throws Exception {
        //Given
        when(workingDayMapper.mapToWorkingDay(any(WorkingDayDto.class))).thenReturn(workingDay);
        when(workingDayService.createNewWorkingDay(any(WorkingDay.class))).thenReturn(workingDay);
        when(workingDayMapper.mapToWorkingDayDto(any(WorkingDay.class))).thenReturn(workingDayDto);
        //When
        //Then
        mockMvc.perform(post("/v1/rotatio/workingDays")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.planned").value(false));
    }

    @Test
    void shouldGetWorkingDays() throws Exception {
        //Given
        when(workingDayService.getAllWorkingDays()).thenReturn(workingDays);
        when(workingDayMapper.mapToWorkingDayDtoList(any())).thenReturn(workingDayDtos);
        //When
        //Then
        mockMvc.perform(get("/v1/rotatio/workingDays"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].archived").value(false))
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void shouldGetWorkingDayById() throws Exception {
        //Given
        when(workingDayService.getWorkingDayById(anyLong())).thenReturn(workingDay);
        when(workingDayMapper.mapToWorkingDayDto(any(WorkingDay.class))).thenReturn(workingDayDto);
        //When
        //Then
        mockMvc.perform(get("/v1/rotatio/workingDays/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1));
    }

    @Test
    void shouldGetWorkingDaysByUserId() throws Exception {
        //Given
        when(workingDayService.getAllByUser(anyLong())).thenReturn(workingDays);
        when(workingDayMapper.mapToWorkingDayDtoList(any())).thenReturn(workingDayDtos);
        String date = LocalDate.now().plusDays(1).toString();
        //When
        //Then
        mockMvc.perform(get("/v1/rotatio/workingDays/byUser/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(1))
                .andExpect(jsonPath("$[0].executeDate").value(date));
    }

    @Test
    void shouldGetWorkingDaysByPlanned() throws Exception {
        //Given
        WorkingDayDto secondDay = new WorkingDayDto(2L, LocalDate.now(), LocalDate.now(),
                false, false, 1L, new ArrayList<>());
        workingDayDtos.add(secondDay);
        when(workingDayService.getAllByPlanned(anyBoolean())).thenReturn(workingDays);
        when(workingDayMapper.mapToWorkingDayDtoList(any())).thenReturn(workingDayDtos);
        //When
        //Then
        mockMvc.perform(get("/v1/rotatio/workingDays/planned/false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].planned").value(false))
                .andExpect(jsonPath("$[1].planned").value(false));
    }

    @Test
    void shouldGetWorkingDayByExecuteDate() throws Exception {
        //Given
        when(workingDayService.getByExecuteDate(any(LocalDate.class))).thenReturn(workingDay);
        when(workingDayMapper.mapToWorkingDayDto(any(WorkingDay.class))).thenReturn(workingDayDto);
        String date = LocalDate.now().plusDays(1).toString();
        //When
        //Then
        mockMvc.perform(get("/v1/rotatio/workingDays/execute/" + date))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.executeDate").value(date));
    }

    @Test
    void shouldUpdateWorkingDay() throws Exception {
        //Given
        WorkingDayDto updated = new WorkingDayDto(1L, workingDayDto.created(), workingDayDto.executeDate(),
                true, false, 1L, new ArrayList<>());
        when(workingDayMapper.mapToWorkingDay(any(WorkingDayDto.class))).thenReturn(workingDay);
        when(workingDayService.saveWorkingDay(any(WorkingDay.class))).thenReturn(workingDay);
        when(workingDayMapper.mapToWorkingDayDto(any(WorkingDay.class))).thenReturn(updated);
        //When
        //Then
        mockMvc.perform(put("/v1/rotatio/workingDays")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.planned").value(true))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void shouldDeleteWorkingDay() throws Exception {
        //Given
        Mockito.doNothing().when(workingDayService).deleteById(anyLong());
        //When
        //Then
        mockMvc.perform(delete("/v1/rotatio/workingDays/1"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnNotFoundWhenWorkingDayDoesNotExist() throws Exception {
        //Given
        when(workingDayService.getWorkingDayById(1L)).thenThrow(new WorkingDayNotFoundException());
        //When
        //Then
        mockMvc.perform(get("/v1/rotatio/workingDays/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Such a working day does not exist"));
    }

    @Test
    void shouldReturnBadRequestWhenInvalidInput() throws Exception {
        //Given
        WorkingDayDto invalid = new WorkingDayDto(1L, null, LocalDate.now().plusDays(1), false,
                false, 1L, new ArrayList<>());
        String invalidJson = mapper.writeValueAsString(invalid);
        //When
        //Then
        mockMvc.perform(post("/v1/rotatio/workingDays")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldHandleServiceException() throws Exception {
        //Given
        when(workingDayService.getWorkingDayById(anyLong())).thenThrow(new RuntimeException("Unexpected error"));
        //When
        //Then
        mockMvc.perform(get("/v1/rotatio/workingDays/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Unexpected error"));
    }
}