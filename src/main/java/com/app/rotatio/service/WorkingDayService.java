package com.app.rotatio.service;

import com.app.rotatio.controller.exception.WorkingDayNotFoundException;
import com.app.rotatio.domain.*;
import com.app.rotatio.repository.WorkingDayRepository;
import com.app.rotatio.service.strategy.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class WorkingDayService {

    @Autowired
    private final WorkingDayRepository workingDayRepository;
    @Autowired
    private final StrategyService strategyService;

    public WorkingDay saveWorkingDay(final WorkingDay workingDay) {
        return workingDayRepository.save(workingDay);
    }

    public void delete(final WorkingDay workingDay) {
        workingDayRepository.delete(workingDay);
    }

    public WorkingDay getWorkingDayById(final Long id) throws WorkingDayNotFoundException {
        return workingDayRepository.findById(id).orElseThrow(WorkingDayNotFoundException::new);
    }

    public List<WorkingDay> getAllWorkingDays() {
        return workingDayRepository.findAll();
    }

    public List<WorkingDay> getAllByUser(final User user) {
        return workingDayRepository.findByUser(user);
    }

    public List<WorkingDay> getAllByPlanned(final boolean planned) {
        return workingDayRepository.findByPlanned(planned);
    }

    public List<WorkingDay> getAllByArchived(final boolean archived) {
        return workingDayRepository.findByArchived(archived);
    }

    public List<WorkingDay> getAllByExecuteDateBefore(final LocalDate date) {
        return workingDayRepository.findByExecuteDateBefore(date);
    }

    public<T extends ListItem> void addItemToList(final WorkingDay workingDay, final T item) {
        strategyService.addItemToWorkingDay(workingDay, item);
        saveWorkingDay(workingDay);
    }

    public <T extends ListItem> void removeItemFromList(final WorkingDay workingDay, final T item) {
        strategyService.removeItemFromWorkingDay(workingDay, item);
        saveWorkingDay(workingDay);
    }

    public void deleteAll() {
        workingDayRepository.deleteAll();
    }
}
