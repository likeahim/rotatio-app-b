package com.app.rotatio.domain;

import com.app.rotatio.prototype.Prototype;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "WORKING_DAYS")
public class WorkingDay extends Prototype<WorkingDay> {

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

    @Column(name = "ARCHIVED")
    private boolean archived;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private List<Worker> workers = new ArrayList<>();

    @Override
    public WorkingDay clone() {
            WorkingDay workingDay = new WorkingDay();
            workingDay.setCreated(this.created);
            workingDay.setExecuteDate(this.executeDate);
            workingDay.setArchived(true);
            workingDay.setUser(this.user);
            List<Worker> clonedWorkers = this.workers.stream()
                            .map((worker -> {
                                Worker clonedWorker = worker.clone();
                                clonedWorker.setWorkingDay(workingDay);
                                return clonedWorker;
                            }))
                                    .toList();
            workingDay.setWorkers(clonedWorkers);
            return workingDay;
    }
}
