package com.app.rotatio.domain.dto;

import javax.validation.constraints.NotNull;

public record WorkplaceDto(
        Long id,
        @NotNull
        String designation,
        boolean active,
        boolean nowUsed
) {
}
