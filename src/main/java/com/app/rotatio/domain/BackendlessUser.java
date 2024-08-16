package com.app.rotatio.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BackendlessUser {
    private String email;
    private String firstName;
    private String lastName;
    private String objectId;
    private String userToken;
    private String userStatus;
}
