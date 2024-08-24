package com.app.rotatio.service;

import com.app.rotatio.api.time.client.TimeApiClient;
import com.app.rotatio.domain.TimeApiCurrent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimeApiServiceTest {

    @Mock
    private TimeApiClient client;

    @InjectMocks
    private TimeApiService timeApiService;

    private TimeApiCurrent timeApiCurrent;

    @BeforeEach
    void setUp() {
        LocalDate now = LocalDate.now();
        timeApiCurrent = TimeApiCurrent.builder()
                .year(now.getYear())
                .month(now.getMonth().getValue())
                .day(now.getDayOfMonth())
                .date(now.toString())
                .build();
    }

    @Test
    void shouldReturnAvailableZones() {
        //Given
        List<String> zones = List.of("Europe/Warsaw", "America/New_York", "Asia/Tokyo");
        when(client.getZones()).thenReturn(zones);
        //When
        List<String> result = timeApiService.getAvailableZones();
        //Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Europe/Warsaw", result.get(0));
        verify(client, times(1)).getZones();
    }

    @Test
    void shouldReturnCurrentTimeForZone() {
        //Given
        String zone = "Europe/Warsaw";
        when(client.getCurrentTimeByZone(zone)).thenReturn(timeApiCurrent);
        //When
        TimeApiCurrent result = timeApiService.getCurrentTime(zone);
        //Then
        String today = LocalDate.now().toString();
        assertNotNull(result);
        assertEquals(today, result.getDate());
        verify(client, times(1)).getCurrentTimeByZone(zone);
    }

    @Test
    void shouldReturnExactDateForZone() {
        //Given
        String zone = "Europe/Warsaw";
        when(client.getCurrentTimeByZone(zone)).thenReturn(timeApiCurrent);
        //When
        String result = timeApiService.getExactDate(zone);
        //Then
        String today = LocalDate.now().toString();
        assertNotNull(result);
        assertEquals(today, result);
        verify(client, times(1)).getCurrentTimeByZone(zone);
    }
}