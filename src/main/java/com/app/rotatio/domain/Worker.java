package com.app.rotatio.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "WORKERS")
public class Worker extends ListItem {

    @Id
    @GeneratedValue
    @Column(name = "WORKER_ID")
    private Long id;

    @NotNull
    @Column(name = "WORKER_NUMBER", unique = true)
    private Long workerNumber;

    @NotNull
    @Column(name = "FIRST_NAME")
    private String firstName;

    @NotNull
    @Column(name = "LASTNAME")
    private String lastName;

    @NotNull
    @Column(name = "STATUS")
    private WorkerStatus status;

    @Column(name = "PRESENCE_FROM")
    private LocalDate presenceFrom;

    @Column(name = "ABSENCE_FROM")
    private LocalDate absenceFrom;

    @ManyToOne
    @JoinColumn(name = "WORKING_DAY_ID")
    private WorkingDay workingDay;

    @ManyToOne
    @JoinColumn(name = "TASK_ID")
    private Task task;

    @ManyToOne
    @JoinColumn(name = "WORKPLACE_ID")
    private Workplace workplace;
}
