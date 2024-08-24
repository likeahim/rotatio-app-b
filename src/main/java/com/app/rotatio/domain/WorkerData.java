package com.app.rotatio.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class WorkerData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String taskName;
    private String workplaceDesignation;

    @ManyToOne
    @JoinColumn(name = "archive_id")
    private Archive archive;
}
