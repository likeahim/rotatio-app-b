package com.app.rotatio.domain.dto;

import java.time.LocalDate;

public record WorkingDayDto(
        Long id,
        LocalDate created,
        LocalDate executeDate,
        boolean planned,
        Long userId
) {
}
