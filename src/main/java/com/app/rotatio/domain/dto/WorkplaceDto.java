package com.app.rotatio.domain.dto;

import java.util.List;

public record WorkplaceDto(
        Long id,
        String designation,
        boolean active,
        boolean nowUsed
) {
}
