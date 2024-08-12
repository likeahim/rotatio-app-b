package com.app.rotatio.domain.dto;

import java.time.LocalDate;

public record WorkingDayArchivesDto(
        Long id,
        LocalDate executed,
        Long workingDayId
) {
}
