package com.app.rotatio.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "WORKING_DAYS")
public class WorkingDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "WORKING_DAY_ID")
    private Long id;

    @NotNull
    @Column(name = "CREATED")
    private LocalDate created;

    @Column(name = "EXECUTE_DATE")
    private LocalDate executeDate;

    @Column(name = "PLANNED")
    private boolean planned;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;
}
