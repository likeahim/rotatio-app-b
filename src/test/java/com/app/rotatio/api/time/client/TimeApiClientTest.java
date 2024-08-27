package com.app.rotatio.api.time.client;

import com.app.rotatio.api.time.config.TimeApiConfig;
import com.app.rotatio.controller.exception.CurrentTimeFetchingException;
import com.app.rotatio.domain.TimeApiCurrent;
import com.app.rotatio.service.UriService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TimeApiClientTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private TimeApiConfig apiConfig;

    @Mock
    private UriService uriService;

    @InjectMocks
    private TimeApiClient timeApiClient;

    @Mock
    private TimeApiConfig timeApiConfig;

    private URI zonesUri = URI.create("http://example.pl");
    private URI currentTimeUri = URI.create("http://example.pl");

    @BeforeEach
    void setUp() {
    }

    @Test
    void shouldReturnListOfZones() {
        when(uriService.getUri(apiConfig.getAvailableZonesEndpoint())).thenReturn(zonesUri);
        when(restTemplate.getForObject(any(URI.class), eq(String[].class)))
                .thenReturn(new String[]{"Europe/Warsaw", "America/New_York"});

        List<String> zones = timeApiClient.getZones();

        assertNotNull(zones);
        assertEquals(2, zones.size());
        assertTrue(zones.contains("Europe/Warsaw"));
    }

    @Test
    void shouldThrowExceptionWhenFetchingZonesFails() {
        when(uriService.getUri(apiConfig.getAvailableZonesEndpoint())).thenReturn(zonesUri);
        when(restTemplate.getForObject(any(URI.class), eq(String[].class)))
                .thenThrow(new RestClientException("Failed to fetch zones"));

        assertThrows(RestClientException.class, () -> timeApiClient.getZones());
    }

    @Test
    void shouldReturnCurrentTimeByZone() {
        when(uriService.getUri(anyString())).thenReturn(currentTimeUri);
        TimeApiCurrent expectedTime = new TimeApiCurrent();
        expectedTime.setDate("2024-08-21");

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        when(restTemplate.exchange(eq(currentTimeUri), eq(HttpMethod.GET), eq(entity), eq(TimeApiCurrent.class)))
                .thenReturn(new ResponseEntity<>(expectedTime, HttpStatus.OK));

        TimeApiCurrent currentTime = timeApiClient.getCurrentTimeByZone("Europe/Warsaw");

        assertNotNull(currentTime);
        assertEquals("2024-08-21", currentTime.getDate());
    }

    @Test
    void shouldThrowExceptionWhenFetchingCurrentTimeFails() {
        when(uriService.getUri(anyString())).thenReturn(currentTimeUri);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        when(restTemplate.exchange(eq(currentTimeUri), eq(HttpMethod.GET), eq(entity), eq(TimeApiCurrent.class)))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR));

        assertThrows(CurrentTimeFetchingException.class, () -> timeApiClient.getCurrentTimeByZone("Europe/Warsaw"));
    }
}