package com.app.rotatio.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.List;

public record UserDto(
        Long id,
        @JsonProperty("firstName")
        String firstName,
        @JsonProperty("lastname")
        String lastname,
        @NotNull
        @JsonProperty("email")
        String email,
        @JsonProperty("password")
        String password,
        String userStatus,
        String objectId,
        @JsonProperty("user-token")
        String userToken,
        List<Long> plannedDays
) {
}
