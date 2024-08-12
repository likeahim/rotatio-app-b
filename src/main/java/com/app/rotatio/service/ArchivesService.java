package com.app.rotatio.service;

import com.app.rotatio.controller.exception.NoArchivesForThisWorkingDayException;
import com.app.rotatio.controller.exception.SuchArchivesNotFoundException;
import com.app.rotatio.controller.exception.WorkingDayNotFoundException;
import com.app.rotatio.domain.WorkingDay;
import com.app.rotatio.domain.WorkingDayArchives;
import com.app.rotatio.repository.WorkingDayArchivesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArchivesService {

    @Autowired
    private final WorkingDayArchivesRepository repository;
    @Autowired
    private final WorkingDayService workingDayService;

    public WorkingDayArchives saveArchives(final WorkingDayArchives workingDayArchives) throws Exception {
        List<WorkingDayArchives> list = getAllArchives().stream()
                .filter(a -> !workingDayArchives.getWorkingDay().equals(a.getWorkingDay()))
                .toList();
        if(list.isEmpty()) {
            return repository.save(workingDayArchives);
        } else {
            throw new Exception();
        }
    }

    public WorkingDayArchives getArchivesById(final Long id) throws SuchArchivesNotFoundException {
        return repository.findById(id).orElseThrow(SuchArchivesNotFoundException::new);
    }

    public List<WorkingDayArchives> getAllArchives() {
        return repository.findAll();
    }

    public WorkingDayArchives getArchivesByWorkingDay(final Long id)
            throws WorkingDayNotFoundException, NoArchivesForThisWorkingDayException {
        WorkingDay workingDayById = workingDayService.getWorkingDayById(id);
        return repository.findByWorkingDay(workingDayById).orElseThrow(NoArchivesForThisWorkingDayException::new);
    }

    public List<WorkingDayArchives> getAllArchivesByExecuted(final LocalDate executed) {
        return repository.findByExecuted(executed);
    }
}
