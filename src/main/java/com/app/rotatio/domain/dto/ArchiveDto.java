package com.app.rotatio.domain.dto;

import javax.validation.constraints.NotNull;

public record ArchiveDto(
        Long id,
        @NotNull
        Long workingDayId,
        String workersData
) {
}
