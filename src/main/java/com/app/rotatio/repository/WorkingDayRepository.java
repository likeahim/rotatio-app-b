package com.app.rotatio.repository;

import com.app.rotatio.domain.User;
import com.app.rotatio.domain.WorkingDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface WorkingDayRepository extends JpaRepository<WorkingDay, Long> {

    List<WorkingDay> findByUser(User user);
    List<WorkingDay> findByPlanned(boolean planned);
    List<WorkingDay> findByArchived(boolean archived);
    List<WorkingDay> findByExecuteDateBefore(LocalDate date);
    Optional<WorkingDay> findByExecuteDate(LocalDate date);
    boolean existsByExecuteDate(LocalDate date);
}