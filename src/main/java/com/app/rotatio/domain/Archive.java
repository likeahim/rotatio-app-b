package com.app.rotatio.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "ARCHIVES")
public class Archive {

    @Id
    @GeneratedValue
    @Column(name = "ARCHIVE_ID")
    private Long id;

    @NotNull
    @Column(name = "WORKING_DAY_ID")
    private Long workingDayId;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String workersData;
}
