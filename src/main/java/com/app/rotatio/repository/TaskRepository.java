package com.app.rotatio.repository;

import com.app.rotatio.domain.Task;
import com.app.rotatio.domain.WorkingDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findByName(String name);
    List<Task> findAllByWorkingDay(WorkingDay workingDay);
}
