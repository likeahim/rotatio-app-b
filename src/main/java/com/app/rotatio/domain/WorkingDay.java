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
    private Boolean planned;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Builder.Default
    @OneToMany(mappedBy = "workingDay", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    List<Workplace> workplaces = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "workingDay", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    List<Task> tasks = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "workingDay", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    List<Worker> workers = new ArrayList<>();
}
