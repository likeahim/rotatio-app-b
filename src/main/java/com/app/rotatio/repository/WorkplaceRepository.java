package com.app.rotatio.repository;

import com.app.rotatio.domain.Task;
import com.app.rotatio.domain.WorkingDay;
import com.app.rotatio.domain.Workplace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface WorkplaceRepository extends JpaRepository<Workplace, Long> {

    List<Workplace> findByDesignation(String designation);
    List<Workplace> findByTask(Task task);
    List<Workplace> findByWorkingDay(WorkingDay workingDay);
    List<Workplace> findByActive(Boolean isActive);
    List<Workplace> findByNowUsed(Boolean isNowUsed);
}
