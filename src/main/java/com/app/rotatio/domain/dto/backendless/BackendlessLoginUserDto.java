package com.app.rotatio.domain.dto.backendless;

import javax.validation.constraints.NotNull;

public record BackendlessLoginUserDto(
        @NotNull
        String login,
        String password
) {
}
