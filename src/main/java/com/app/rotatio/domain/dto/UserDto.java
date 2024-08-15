package com.app.rotatio.domain.dto;

import java.util.List;

public record UserDto(
        Long id,
        String firstName,
        String lastname,
        String email,
        String password,
        boolean isEnabled,
        List<Long> plannedDays
) {
}
