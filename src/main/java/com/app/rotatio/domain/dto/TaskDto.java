package com.app.rotatio.domain.dto;

import java.util.List;

public record TaskDto(
        Long id,
        String name,
        String description,
        boolean isPerformed
) {
}
