package com.app.rotatio.controller;

import com.app.rotatio.domain.TimeApiCurrent;
import com.app.rotatio.domain.dto.time.TimeApiCurrentDto;
import com.app.rotatio.mapper.TimeApiMapper;
import com.app.rotatio.service.TimeApiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitWebConfig
@WebMvcTest(TimeApiController.class)
public class TimeApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TimeApiService service;

    @MockBean
    private TimeApiMapper mapper;

    @Test
    void shouldGetZones() throws Exception {
        //Given
        List<String> zones = Arrays.asList("Europe/Warsaw", "America/New_York");
        when(service.getAvailableZones()).thenReturn(zones);
        //When
        //Then
        mockMvc.perform(get("/v1/rotatio/time/zones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Europe/Warsaw"))
                .andExpect(jsonPath("$[1]").value("America/New_York"));
    }

    @Test
    void shouldGetCurrentTime() throws Exception {
        //Given
        TimeApiCurrentDto dto = new TimeApiCurrentDto(2024, 8, 20, "2024/08/20");
        TimeApiCurrent currentTime = TimeApiCurrent.builder()
                .year(2024)
                .month(8)
                .day(20)
                .date("2024/08/20")
                .build();
        when(service.getCurrentTime("Europe/Warsaw")).thenReturn(currentTime);
        when(mapper.mapToCurrentDto(currentTime)).thenReturn(dto);
        //When
        //Then
        mockMvc.perform(get("/v1/rotatio/time")
                        .param("timeZone", "Europe/Warsaw")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.year").value(2024))
                .andExpect(jsonPath("$.date").value("2024/08/20"));
    }

    @Test
    void shouldGetExactDate() throws Exception {
        //Given
        when(service.getExactDate("Europe/Warsaw")).thenReturn("2024/08/20");
        //When
        //Then
        mockMvc.perform(get("/v1/rotatio/time/value")
                        .param("timeZone", "Europe/Warsaw"))
                .andExpect(status().isOk())
                .andExpect(content().string("2024/08/20"));
    }

    @Test
    void shouldReturnBadRequestWhenTimeZoneIsMissing() throws Exception {
        //Given
        //When
        //Then
        mockMvc.perform(get("/v1/rotatio/time/value"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnServiceExceptionWhenInvalidTimeZoneIsProvided() throws Exception {
        //Given
        when(service.getExactDate("Invalid/Zone")).thenThrow(new IllegalArgumentException("Invalid time zone"));
        //When
        //Then
        mockMvc.perform(get("/v1/rotatio/time/value")
                        .param("timeZone", "Invalid/Zone"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Invalid time zone"));
    }

    @Test
    void shouldHandleServiceException() throws Exception {
        //Given
        when(service.getExactDate(anyString())).thenThrow(new RuntimeException("Unexpected error"));
        //When
        //Then
        mockMvc.perform(get("/v1/rotatio/time/value")
                        .param("timeZone", "Europe/Warsaw"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Unexpected error"));
    }
}