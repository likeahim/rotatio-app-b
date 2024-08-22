package com.app.rotatio.domain;

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
    @NotNull
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String objectId;
    private String userToken;
    private String userStatus;
}
