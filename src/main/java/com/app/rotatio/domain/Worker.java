package com.app.rotatio.domain;

import com.app.rotatio.prototype.Prototype;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "WORKERS")
public class Worker extends Prototype<Worker> {

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

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "TASK_ID")
    private Task task;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "WORKPLACE_ID")
    private Workplace workplace;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Worker worker = (Worker) o;
        return Objects.equals(id, worker.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }

    @Override
    public Worker clone() {
        return Worker.builder()
                .workerNumber(this.workerNumber)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .status(this.status)
                .presenceFrom(this.presenceFrom)
                .absenceFrom(this.absenceFrom)
                .task(this.task.clone())
                .workplace(this.workplace.clone())
                .build();
    }
}
