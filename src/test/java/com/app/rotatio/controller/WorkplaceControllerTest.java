package com.app.rotatio.controller;

import com.app.rotatio.controller.exception.WorkplaceNotFoundException;
import com.app.rotatio.domain.Workplace;
import com.app.rotatio.domain.dto.WorkplaceDto;
import com.app.rotatio.mapper.WorkplaceMapper;
import com.app.rotatio.service.WorkplaceService;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitWebConfig
@WebMvcTest(WorkplaceController.class)
public class WorkplaceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WorkplaceService workplaceService;

    @MockBean
    private WorkplaceMapper workplaceMapper;

    private WorkplaceDto workplaceDto;
    private Workplace workplace;
    private String json;
    private ObjectMapper mapper;
    private List<Workplace> workplaces;
    private List<WorkplaceDto> workplaceDtos;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        workplaceDto = new WorkplaceDto(1L, "Test", true, true);
        workplace = Workplace.builder()
                .id(1L)
                .designation("Test")
                .active(true)
                .nowUsed(true)
                .build();

        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        json = mapper.writeValueAsString(workplaceDto);
        workplaces = new ArrayList<>(List.of(workplace));
        workplaceDtos = new ArrayList<>(List.of(workplaceDto));
    }

    @Test
    void shouldCreateWorkplace() throws Exception {

        when(workplaceMapper.mapToWorkplace(any(WorkplaceDto.class))).thenReturn(workplace);
        when(workplaceService.saveWorkplace(any(Workplace.class))).thenReturn(workplace);
        when(workplaceMapper.mapToWorkplaceDto(any(Workplace.class))).thenReturn(workplaceDto);

        mockMvc.perform(post("/v1/rotatio/workplaces")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.designation").value("Test"));
    }

    @Test
    void shouldUpdateWorkplace() throws Exception {

        WorkplaceDto updatedWorkplaceDto = new WorkplaceDto(1L, "Test", false, true);
        when(workplaceMapper.mapToWorkplace(any(WorkplaceDto.class))).thenReturn(workplace);
        when(workplaceService.saveWorkplace(any(Workplace.class))).thenReturn(workplace);
        when(workplaceMapper.mapToWorkplaceDto(any(Workplace.class))).thenReturn(updatedWorkplaceDto);

        mockMvc.perform(put("/v1/rotatio/workplaces")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(false))
                .andExpect(jsonPath("$.nowUsed").value(true));
    }

    @Test
    void shouldDeleteWorkplace() throws Exception {
        WorkplaceDto afterDelete = new WorkplaceDto(1L, "Test", false, false);
        when(workplaceService.getWorkplaceById(anyLong())).thenReturn(workplace);
        when(workplaceService.deleteWorkplace(any(Workplace.class))).thenReturn(workplace);
        when(workplaceMapper.mapToWorkplaceDto(any(Workplace.class))).thenReturn(afterDelete);

        mockMvc.perform(delete("/v1/rotatio/workplaces/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.active").value(false))
                .andExpect(jsonPath("$.nowUsed").value(false));
    }

    @Test
    void shouldGetAllWorkplaces() throws Exception {

        when(workplaceService.getAllWorkplaces()).thenReturn(workplaces);
        when(workplaceMapper.mapToWorkplaceDtoList(any())).thenReturn(workplaceDtos);

        mockMvc.perform(get("/v1/rotatio/workplaces"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].designation").value("Test"))
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void shouldGetWorkplaceById() throws Exception {

        when(workplaceService.getWorkplaceById(anyLong())).thenReturn(workplace);
        when(workplaceMapper.mapToWorkplaceDto(any(Workplace.class))).thenReturn(workplaceDto);

        mockMvc.perform(get("/v1/rotatio/workplaces/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void shouldGetWorkplacesByActive() throws Exception {

        when(workplaceService.getAllByActive(anyBoolean())).thenReturn(workplaces);
        when(workplaceMapper.mapToWorkplaceDtoList(any())).thenReturn(workplaceDtos);

        mockMvc.perform(get("/v1/rotatio/workplaces/byActive/true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].active").value(true));
    }

    @Test
    void shouldGetWorkplacesByNowUsed() throws Exception {

        when(workplaceService.getAllByNowUsed(anyBoolean())).thenReturn(workplaces);
        when(workplaceMapper.mapToWorkplaceDtoList(any())).thenReturn(workplaceDtos);

        mockMvc.perform(get("/v1/rotatio/workplaces/byNowUsed/true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nowUsed").value(true));
    }

    // Exception handling tests
    @Test
    void shouldReturnNotFoundWhenWorkplaceDoesNotExist() throws Exception {
        when(workplaceService.getWorkplaceById(1L)).thenThrow(new WorkplaceNotFoundException());

        mockMvc.perform(get("/v1/rotatio/workplaces/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Such a workplace not found"));
    }

    @Test
    void shouldReturnBadRequestWhenInvalidInput() throws Exception {
        Workplace invalid = Workplace.builder().build();
        String invalidJson = mapper.writeValueAsString(invalid);
        mockMvc.perform(post("/v1/rotatio/workplaces")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldHandleServiceException() throws Exception {
        when(workplaceService.getWorkplaceById(anyLong())).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/v1/rotatio/workplaces/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Unexpected error"));
    }
}