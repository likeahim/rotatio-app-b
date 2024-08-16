package com.app.rotatio.domain.dto.backendless;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BackendlessUserDto(
        @JsonProperty("email")
        String email,
        @JsonProperty("firstName")
        String firstName,
        @JsonProperty("lastName")
        String lastName,
        @JsonProperty("objectId")
        String objectId,
        @JsonProperty("user-token")
        String userToken,
        @JsonProperty("userStatus")
        String userStatus
) {
}
