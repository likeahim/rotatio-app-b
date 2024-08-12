package com.app.rotatio.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "WORKING_DAY_ARCHIVES")
public class WorkingDayArchives {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "EXECUTED")
    private LocalDate executed;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WORKING_DAY_ID")
    private WorkingDay workingDay;
}
