package com.app.rotatio.mapper;

import com.app.rotatio.domain.TimeApiCurrent;
import com.app.rotatio.domain.TimeApiZones;
import com.app.rotatio.domain.dto.time.TimeApiCurrentDto;
import com.app.rotatio.domain.dto.time.TimeApiZonesDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TimeApiMapperTest {

    @Autowired
    private TimeApiMapper timeApiMapper;

    private TimeApiZonesDto timeApiZonesDto;
    private TimeApiZones timeApiZones;
    private TimeApiCurrentDto timeApiCurrentDto;
    private TimeApiCurrent timeApiCurrent;

    @BeforeEach
    public void setUp() {
        timeApiZonesDto = new TimeApiZonesDto("Europe/Warsaw");
        timeApiZones = TimeApiZones.builder()
                .zone("Europe/Warsaw")
                .build();

        timeApiCurrentDto = new TimeApiCurrentDto(2023, 10, 15, "2023-10-15T10:00:00Z");
        timeApiCurrent = TimeApiCurrent.builder()
                .year(2023)
                .month(10)
                .day(15)
                .date("2023-10-15T10:00:00Z")
                .build();
    }

    @Test
    void shouldMapToZones() {
        // When
        TimeApiZones result = timeApiMapper.mapToZones(timeApiZonesDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getZone()).isEqualTo(timeApiZonesDto.zone());
    }

    @Test
    void shouldMapToZoneDto() {
        // When
        TimeApiZonesDto result = timeApiMapper.mapToZoneDto(timeApiZones);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.zone()).isEqualTo(timeApiZones.getZone());
    }

    @Test
    void shouldMapToCurrent() {
        // When
        TimeApiCurrent result = timeApiMapper.mapToCurrent(timeApiCurrentDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getYear()).isEqualTo(timeApiCurrentDto.year());
        assertThat(result.getMonth()).isEqualTo(timeApiCurrentDto.month());
        assertThat(result.getDay()).isEqualTo(timeApiCurrentDto.day());
        assertThat(result.getDate()).isEqualTo(timeApiCurrentDto.date());
    }

    @Test
    void shouldMapToCurrentDto() {
        // When
        TimeApiCurrentDto result = timeApiMapper.mapToCurrentDto(timeApiCurrent);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.year()).isEqualTo(timeApiCurrent.getYear());
        assertThat(result.month()).isEqualTo(timeApiCurrent.getMonth());
        assertThat(result.day()).isEqualTo(timeApiCurrent.getDay());
        assertThat(result.date()).isEqualTo(timeApiCurrent.getDate());
    }

    @Test
    void shouldMapToZonesListDto() {
        // Given
        List<TimeApiZones> zonesList = Arrays.asList(timeApiZones);

        // When
        List<TimeApiZonesDto> result = timeApiMapper.mapToZonesListDto(zonesList);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
        TimeApiZonesDto resultItem = result.get(0);
        assertThat(resultItem.zone()).isEqualTo(timeApiZones.getZone());
    }
}