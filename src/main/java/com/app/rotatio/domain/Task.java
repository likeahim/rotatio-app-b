package com.app.rotatio.domain;

import com.app.rotatio.prototype.Prototype;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "TASKS")
public class Task extends Prototype<Task> {

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
    private boolean performed;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }

    @Override
    public Task clone() {
        return Task.builder()
                .name(this.name + " clone")
                .description(this.description)
                .performed(this.performed)
                .build();
    }
}
