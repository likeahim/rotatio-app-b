package com.app.rotatio.domain;

import com.app.rotatio.prototype.Prototype;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "WORKPLACES")
public class Workplace extends Prototype<Workplace> {

    @Id
    @GeneratedValue
    @Column(name = "WORKPLACE_ID")
    private Long id;

    @NotNull
    @Column(name = "DESIGNATION")
    private String designation;

    @Column(name = "IS_ACTIVE")
    private boolean active;

    @Column(name = "IS_NOW_USED")
    private boolean nowUsed;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Workplace workplace = (Workplace) o;
        return Objects.equals(id, workplace.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id);
    }

    @Override
    public Workplace clone() {
        return Workplace.builder()
                .designation(this.designation)
                .active(this.active)
                .nowUsed(this.nowUsed)
                .build();
    }
}
