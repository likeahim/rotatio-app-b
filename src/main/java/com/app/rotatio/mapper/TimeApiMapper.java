package com.app.rotatio.mapper;

import com.app.rotatio.domain.TimeApiCurrent;
import com.app.rotatio.domain.TimeApiZones;
import com.app.rotatio.domain.dto.time.TimeApiCurrentDto;
import com.app.rotatio.domain.dto.time.TimeApiZonesDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TimeApiMapper {

    public TimeApiZones mapToZones(final TimeApiZonesDto zonesDto) {
        return TimeApiZones.builder()
                .zone(zonesDto.zone())
                .build();
    }

    public TimeApiZonesDto mapToZoneDto(final TimeApiZones zones) {
        return new TimeApiZonesDto(
                zones.getZone()
        );
    }

    public TimeApiCurrent mapToCurrent(final TimeApiCurrentDto currentDto) {
        return TimeApiCurrent.builder()
                .year(currentDto.year())
                .month(currentDto.month())
                .day(currentDto.day())
                .date(currentDto.date())
                .build();
    }

    public TimeApiCurrentDto mapToCurrentDto(final TimeApiCurrent current) {
        return new TimeApiCurrentDto(
                current.getYear(),
                current.getMonth(),
                current.getDay(),
                current.getDate()
        );
    }

    public List<TimeApiZonesDto> mapToZonesListDto(List<TimeApiZones> availableZones) {
        return availableZones.stream()
                .map(this::mapToZoneDto)
                .toList();
    }
}
