package com.app.rotatio.repository;

import com.app.rotatio.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface WorkerRepository extends JpaRepository<Worker, Long> {

    List<Worker> findAllByStatus(WorkerStatus status);
    List<Worker> findAllByWorkingDay(WorkingDay workingDay);
}
