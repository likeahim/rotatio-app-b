package com.app.rotatio.controller;

import com.app.rotatio.domain.TimeApiCurrent;
import com.app.rotatio.domain.dto.time.TimeApiCurrentDto;
import com.app.rotatio.domain.dto.time.TimeApiZonesDto;
import com.app.rotatio.mapper.TimeApiMapper;
import com.app.rotatio.service.TimeApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/rotatio/time")
@RequiredArgsConstructor
@CrossOrigin("*")
public class TimeApiController {

    private final TimeApiService service;
    private final TimeApiMapper mapper;

    @GetMapping(value = "/zones")
    public ResponseEntity<List<String>> getZones() {
        return ResponseEntity.ok(service.getAvailableZones());
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TimeApiCurrentDto> getCurrentTime(@RequestParam("timeZone") String zone) {
        return ResponseEntity.ok(mapper.mapToCurrentDto(service.getCurrentTime(zone)));
    }

    @GetMapping(value = "/value")
    public ResponseEntity<String> getExactDate(@RequestParam("timeZone") String zone) {
        return ResponseEntity.ok(service.getExactDate(zone));
    }
}
