package com.app.rotatio.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "WORKERS")
public class Worker {

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

    @ManyToOne //a może manytomany?
    @JoinColumn(name = "WORKING_DAY_ID")
    private WorkingDay workingDay;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "TASK_ID")
    private Task task;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "WORKPLACE_ID")
    private Workplace workplace;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private Long workerNumber;
        private String firstName;
        private String lastName;
        private WorkerStatus status;
        private LocalDate presenceFrom;
        private LocalDate absenceFrom;
        private WorkingDay workingDay;
        private Task task;
        private Workplace workplace;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder workerNumber(Long workerNumber) {
            this.workerNumber = workerNumber;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder status(WorkerStatus status) {
            this.status = status;
            return this;
        }

        public Builder presenceFrom(LocalDate presenceFrom) {
            this.presenceFrom = presenceFrom;
            return this;
        }

        public Builder absenceFrom(LocalDate absenceFrom) {
            this.absenceFrom = absenceFrom;
            return this;
        }

        public Builder workingDay(WorkingDay workingDay) {
            this.workingDay = workingDay;
            return this;
        }

        public Builder task(Task task) {
            this.task = task;
            return this;
        }

        public Builder workplace(Workplace workplace) {
            this.workplace = workplace;
            return this;
        }

        public Worker build() {
            Worker worker = new Worker();
            worker.id = this.id;
            worker.workerNumber = this.workerNumber;
            worker.firstName = this.firstName;
            worker.lastName = this.lastName;
            worker.status = this.status;
            worker.presenceFrom = this.presenceFrom;
            worker.absenceFrom = this.absenceFrom;
            worker.workingDay = this.workingDay;
            worker.task = this.task;
            worker.workplace = this.workplace;
            return worker;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Worker worker = (Worker) o;
        return Objects.equals(workerNumber, worker.workerNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(workerNumber);
    }
}
