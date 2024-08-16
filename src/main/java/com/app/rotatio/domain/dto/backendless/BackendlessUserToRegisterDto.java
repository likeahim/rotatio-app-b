package com.app.rotatio.domain.dto.backendless;

public record BackendlessUserToRegisterDto(
        String email,
        String password,
        String firstName,
        String lastName
) {
}
