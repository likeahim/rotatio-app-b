package com.app.rotatio.service;

import com.app.rotatio.controller.exception.UserNotFoundException;
import com.app.rotatio.controller.exception.WorkingDayExecuteDateConflictException;
import com.app.rotatio.controller.exception.WorkingDayNotFoundException;
import com.app.rotatio.domain.*;
import com.app.rotatio.mapper.WorkingDayMapper;
import com.app.rotatio.repository.WorkingDayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkingDayService {

    @Autowired
    private final WorkingDayRepository workingDayRepository;
    @Autowired
    private final UserService userService;

    public WorkingDay saveWorkingDay(final WorkingDay workingDay) {
        return workingDayRepository.save(workingDay);
    }

    public WorkingDay createNewWorkingDay(final WorkingDay workingDay) throws WorkingDayExecuteDateConflictException {
        if (workingDayRepository.existsByExecuteDate(workingDay.getExecuteDate())) {
            throw new WorkingDayExecuteDateConflictException();
        } else {
            return saveWorkingDay(workingDay);
        }
    }

    public void delete(final WorkingDay workingDay) {
        workingDayRepository.delete(workingDay);
    }

    public WorkingDay getWorkingDayById(final Long id) throws WorkingDayNotFoundException {
        return workingDayRepository.findById(id).orElseThrow(WorkingDayNotFoundException::new);
    }

    public WorkingDay getByExecuteDate(final LocalDate executeDate) throws WorkingDayNotFoundException {
        return workingDayRepository.findByExecuteDate(executeDate);
    }

    public List<WorkingDay> getAllWorkingDays() {
        return workingDayRepository.findAll();
    }

    public List<WorkingDay> getAllByUser(final Long userId) throws UserNotFoundException {
        User user = userService.getUserById(userId);
        return workingDayRepository.findByUser(user);
    }

    public List<WorkingDay> getAllByPlanned(final boolean planned) {
        return workingDayRepository.findByPlanned(planned);
    }

    public List<WorkingDay> getAllByExecuteDateBefore(final LocalDate date) {
        return workingDayRepository.findByExecuteDateBefore(date);
    }

    public void deleteAll() {
        workingDayRepository.deleteAll();
    }

    public List<WorkingDay> longToWorkingDaysList(List<Long> days) {
        return days.stream()
                .map(id -> {
                    try {
                        return getWorkingDayById(id);
                    } catch (WorkingDayNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }).toList();
    }

    public List<Long> workingDaysToLongList(List<WorkingDay> plannedDays) {
        if(plannedDays.isEmpty()) {
            return Collections.emptyList();
        }
        return plannedDays.stream()
                .map(WorkingDay::getId)
                .toList();
    }
}
