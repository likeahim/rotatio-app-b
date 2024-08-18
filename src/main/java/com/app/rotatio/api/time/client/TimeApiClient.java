package com.app.rotatio.api.time.client;

import com.app.rotatio.api.time.config.TimeApiConfig;
import com.app.rotatio.controller.exception.CurrentTimeFetchingException;
import com.app.rotatio.domain.TimeApiCurrent;
import com.app.rotatio.service.UriService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TimeApiClient {

    private final RestTemplate restTemplate;
    private final TimeApiConfig apiConfig;
    private final UriService uriService;

    public List<String> getZones() {
        URI uri = uriService.getUri(apiConfig.getAvailableZonesEndpoint());
        String[] zones = restTemplate.getForObject(uri, String[].class);
        return Arrays.asList(zones);
    }

    public TimeApiCurrent getCurrentTimeByZone(String zone) {
        URI uri = uriService.getUri(apiConfig.getCurrentTimeByZoneEndpoint() + zone);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<TimeApiCurrent> response = restTemplate.exchange(uri, HttpMethod.GET, entity, TimeApiCurrent.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new CurrentTimeFetchingException();
        }
    }
}
