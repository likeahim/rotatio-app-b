package com.app.rotatio.service;

import com.app.rotatio.controller.exception.WorkplaceNotFoundException;
import com.app.rotatio.domain.Task;
import com.app.rotatio.domain.WorkingDay;
import com.app.rotatio.domain.Workplace;
import com.app.rotatio.repository.WorkplaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<Workplace> getAllByDesignation(final String designation) {
        return workplaceRepository.findByDesignation(designation);
    }

    public List<Workplace> getAllByTask(final Task task) {
        return workplaceRepository.findByTask(task);
    }

    public List<Workplace> getAllByWorkingDay(final WorkingDay workingDay) {
        return workplaceRepository.findByWorkingDay(workingDay);
    }


    public List<Workplace> longToWorkplacesList(List<Long> workplaces) {
        return workplaces.stream()
                .map(id -> {
                    try {
                        return getWorkplaceById(id);
                    } catch (WorkplaceNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
    }
}
