package com.app.rotatio.domain.dto;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public record WorkingDayDto(
        Long id,
        @NotNull
        LocalDate created,
        LocalDate executeDate,
        boolean planned,
        boolean archived,
        Long userId,
        List<Long> workers
) {
}
