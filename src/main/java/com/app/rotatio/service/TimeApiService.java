package com.app.rotatio.service;

import com.app.rotatio.api.time.client.TimeApiClient;
import com.app.rotatio.domain.TimeApiCurrent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeApiService {

    private final TimeApiClient client;

    public List<String> getAvailableZones() {
        return client.getZones();
    }

    public TimeApiCurrent getCurrentTime(String zone) {
        return client.getCurrentTimeByZone(zone);
    }

    public String getExactDate(String zone) {
        TimeApiCurrent currentTimeByZone = client.getCurrentTimeByZone(zone);
        return currentTimeByZone.getDate();
    }
}
