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
@Table(name = "WORKPLACES")
public class Workplace extends ListItem {

    @Id
    @GeneratedValue
    @Column(name = "WORKPLACE_ID")
    private Long id;

    @NotNull
    @Column(name = "DESIGNATION")
    private String designation;

    @Column(name = "IS_ACTIVE")
    private boolean isActive;

    @Column(name = "IS_NOW_USED")
    private boolean isNowUsed;

    @ManyToOne
    @JoinColumn(name = "TASK_ID")
    private Task task;

    @ManyToOne
    @JoinColumn(name = "WORKING_DAY_ID")
    private WorkingDay workingDay;

    @Builder.Default
    @OneToMany(mappedBy = "workplace", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<Worker> workers = new ArrayList<>();

}
