package com.app.rotatio.repository;

import com.app.rotatio.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Transactional
@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long> {

    List<Worker> findAllByStatus(WorkerStatus status);
    List<Worker> findAllByPresenceFromBefore(LocalDate presenceFrom);
    List<Worker> findAllByAbsenceFrom(LocalDate absenceFrom);
    List<Worker> findAllByTask(Task task);
    List<Worker> findAllByWorkplace(Workplace workplace);
    List<Worker> findAllByWorkingDay(WorkingDay workingDay);
}
