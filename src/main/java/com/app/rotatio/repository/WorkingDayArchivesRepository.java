package com.app.rotatio.repository;

import com.app.rotatio.domain.WorkingDay;
import com.app.rotatio.domain.WorkingDayArchives;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WorkingDayArchivesRepository extends JpaRepository<WorkingDayArchives, Long> {
    Optional<WorkingDayArchives> findByWorkingDay(WorkingDay workingDay);
    List<WorkingDayArchives> findByExecuted(LocalDate executed);
}