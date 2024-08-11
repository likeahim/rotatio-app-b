package com.app.rotatio.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "TASKS")
public class Task extends ListItem {

    @Id
    @GeneratedValue
    @Column(name = "TASK_ID")
    private Long id;

    @NotNull
    @Column(name = "NAME", unique = true)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "IS_PERFORMED")
    private boolean isPerformed;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "WORKING_DAY_ID")
    private WorkingDay workingDay;

    @Builder.Default
    @OneToMany(mappedBy = "task", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<Workplace> workplaces = new ArrayList<>();
}
