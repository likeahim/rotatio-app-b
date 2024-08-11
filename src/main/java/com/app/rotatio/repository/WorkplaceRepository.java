package com.app.rotatio.repository;

import com.app.rotatio.domain.Workplace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface WorkplaceRepository extends JpaRepository<Workplace, Long> {
}
