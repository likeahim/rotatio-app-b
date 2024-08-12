package com.app.rotatio.service;

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
    private WorkingDayRepository workingDayRepository;
    @Autowired
    private StrategyService strategyService;

    public WorkingDay save(WorkingDay workingDay) {
        return workingDayRepository.save(workingDay);
    }

    public void delete(WorkingDay workingDay) {
        workingDayRepository.delete(workingDay);
    }

    public List<WorkingDay> getAllWorkingDays() {
        return workingDayRepository.findAll();
    }

    public List<WorkingDay> fetchByUser(User user) {
        return workingDayRepository.findByUser(user);
    }

    public List<WorkingDay> fetchByPlanned(boolean planned) {
        return workingDayRepository.findByPlanned(planned);
    }

    public List<WorkingDay> fetchByArchived(boolean archived) {
        return workingDayRepository.findByArchived(archived);
    }

    public List<WorkingDay> fetchByExecuteDateBefore(LocalDate date) {
        return workingDayRepository.findByExecuteDateBefore(date);
    }

    public<T extends ListItem> void addItemToList(WorkingDay workingDay, T item) {
        strategyService.addItemToWorkingDay(workingDay, item);
        save(workingDay);
    }

    public <T extends ListItem> void removeItemFromList(WorkingDay workingDay, T item) {
        strategyService.removeItemFromWorkingDay(workingDay, item);
        save(workingDay);
    }

    public void deleteAll() {
        workingDayRepository.deleteAll();
    }
}
