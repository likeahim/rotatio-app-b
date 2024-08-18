package com.app.rotatio.domain.dto.time;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TimeApiCurrentDto(
        @JsonProperty("year")
        int year,
        @JsonProperty("month")
        int month,
        @JsonProperty("day")
        int day,
        @JsonProperty("date")
        String date
) {
}
