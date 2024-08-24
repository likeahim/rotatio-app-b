package com.app.rotatio.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BackendlessUser {
    private Long id;
    @NotNull
    private String email;
    private String password;
    private String firstName;
    private String lastname;
    private String objectId;
    @JsonProperty("user-token")
    private String userToken;
    private String userStatus;
}
