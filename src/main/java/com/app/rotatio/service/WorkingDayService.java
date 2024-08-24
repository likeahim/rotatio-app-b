package com.app.rotatio.service;

import com.app.rotatio.controller.exception.*;
import com.app.rotatio.domain.*;
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

//    public WorkingDay cloneAndSaveWorkingDay(final Long id) throws WorkingDayNotFoundException, WorkingDayAlreadyArchivedException {
//        WorkingDay toClone = getWorkingDayById(id);
//        if (!toClone.isArchived()) {
//            toClone.setArchived(true);
//            WorkingDay cloned = toClone.clone();
//
//            return saveWorkingDay(cloned);
//        } else {
//            throw new WorkingDayAlreadyArchivedException();
//        }
//    }

    public void deleteById(final Long id) {
        workingDayRepository.deleteById(id);
    }

    public WorkingDay getWorkingDayById(final Long id) throws WorkingDayNotFoundException {
        return workingDayRepository.findById(id).orElseThrow(WorkingDayNotFoundException::new);
    }

    public WorkingDay getByExecuteDate(final LocalDate executeDate) throws WorkingDayNotFoundException {
        return workingDayRepository.findByExecuteDate(executeDate).orElseThrow(WorkingDayNotFoundException::new);
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

    public List<WorkingDay> getAllByArchived(final boolean archived) {
        return workingDayRepository.findByArchived(archived);
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
                        throw new RuntimeException(e.getMessage());
                    }
                }).toList();
    }

    public List<Long> workingDaysToLongList(List<WorkingDay> plannedDays) {
        if (plannedDays.isEmpty()) {
            return Collections.emptyList();
        }
        return plannedDays.stream()
                .map(WorkingDay::getId)
                .toList();
    }
}
