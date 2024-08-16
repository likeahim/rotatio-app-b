package com.app.rotatio.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue
    @Column(name = "USER_ID")
    private Long id;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LASTNAME")
    private String lastname;

    @NotNull
    @Column(name = "EMAIL", unique = true)
    private String email;

    @Column(name = "USER_STATUS")
    private String userStatus;

    @Column(name = "OBJECT_ID")
    private String objectId;

    @Column(name = "USER_TOKEN")
    private String userToken;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<WorkingDay> plannedDays = new ArrayList<>();
}
