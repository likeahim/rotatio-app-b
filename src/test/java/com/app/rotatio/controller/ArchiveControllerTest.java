package com.app.rotatio.controller;

import com.app.rotatio.controller.exception.ArchiveNotFoundException;
import com.app.rotatio.domain.Archive;
import com.app.rotatio.domain.dto.ArchiveDto;
import com.app.rotatio.mapper.ArchiveMapper;
import com.app.rotatio.service.ArchiveService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitWebConfig
@WebMvcTest(ArchiveController.class)
public class ArchiveControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArchiveService archiveService;

    @MockBean
    private ArchiveMapper archiveMapper;

    private ArchiveDto archiveDto;
    private Archive archive;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        archiveDto = new ArchiveDto(1L, 1L, "workers");
        archive = Archive.builder()
                .id(1L)
                .workingDayId(1L)
                .workersData("workers")
                .build();
    }

    @Test
    void shouldCreateArchive() throws Exception {
        //Given
        when(archiveService.archive(anyLong())).thenReturn(archive);
        when(archiveMapper.mapToArchiveDto(any(Archive.class))).thenReturn(archiveDto);
        //When
        //Then
        mockMvc.perform(post("/v1/rotatio/archives/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

    }

    @Test
    void shouldDeleteArchive() throws Exception {
        //Given
        //When
        //Then
        mockMvc.perform(delete("/v1/rotatio/archives/delete/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    void shouldGetArchiveById() throws Exception {
        //Given
        when(archiveService.getArchiveById(anyLong())).thenReturn(archive);
        when(archiveMapper.mapToArchiveDto(any(Archive.class))).thenReturn(archiveDto);
        //When
        //Then
        mockMvc.perform(get("/v1/rotatio/archives/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.workingDayId").value(1));
    }

    // Exception handling tests
    @Test
    void shouldReturnNotFoundWhenArchiveDoesNotExist() throws Exception {
        //Given
        when(archiveService.getArchiveById(1L)).thenThrow(new ArchiveNotFoundException());
        //When
        //Then
        mockMvc.perform(get("/v1/rotatio/archives/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Such an Archive not found"));
    }

    @Test
    void shouldReturnBadRequestWhenInvalidInput() throws Exception {
        //Given
        //When
        //Then
        mockMvc.perform(post("/v1/rotatio/archives/invalid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldHandleServiceException() throws Exception {
        //Given
        when(archiveService.archive(anyLong())).thenThrow(new RuntimeException("Unexpected error"));
        //When
        //Then
        mockMvc.perform(post("/v1/rotatio/archives/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Unexpected error"));
    }
}