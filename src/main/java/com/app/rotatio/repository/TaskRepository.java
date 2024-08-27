package com.app.rotatio.repository;

import com.app.rotatio.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByPerformed(boolean performed);
}
