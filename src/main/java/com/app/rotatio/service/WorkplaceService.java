package com.app.rotatio.service;

import com.app.rotatio.controller.exception.WorkplaceNotFoundException;import com.app.rotatio.domain.Workplace;
import com.app.rotatio.repository.WorkplaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkplaceService {

    @Autowired
    private final WorkplaceRepository workplaceRepository;

    public Workplace saveWorkplace(final Workplace workplace) {
        return workplaceRepository.save(workplace);
    }

    public Workplace deleteWorkplace(final Workplace workplace) {
        workplace.setActive(false);
        workplace.setNowUsed(false);
        return workplaceRepository.save(workplace);
    }

    public Workplace getWorkplaceById(final Long id) throws WorkplaceNotFoundException {
        return workplaceRepository.findById(id).orElseThrow(WorkplaceNotFoundException::new);
    }

    public List<Workplace> getAllWorkplaces() {
        return workplaceRepository.findAll();
    }

    public List<Workplace> getAllByActive(final boolean active) {
        return workplaceRepository.findByActive(active);
    }

    public List<Workplace> getAllByNowUsed(final boolean nowUsed) {
        return workplaceRepository.findByNowUsed(nowUsed);
    }
}
