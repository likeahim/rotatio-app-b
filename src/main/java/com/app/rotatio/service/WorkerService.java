package com.app.rotatio.service;

import com.app.rotatio.controller.exception.TaskNotFoundException;
import com.app.rotatio.controller.exception.WorkerNotFoundException;
import com.app.rotatio.controller.exception.WorkingDayNotFoundException;
import com.app.rotatio.controller.exception.WorkplaceNotFoundException;
import com.app.rotatio.domain.*;
import com.app.rotatio.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkerService {

    private final WorkerRepository workerRepository;
    private final TaskService taskService;
    private final WorkplaceService workplaceService;
    private final WorkingDayService workingDayService;

    public Worker saveWorker(final Worker worker) {
        return workerRepository.save(worker);
    }

    public Worker deleteWorker(final Worker worker) {
        worker.setAbsenceFrom(LocalDate.now());
        worker.setStatus(WorkerStatus.UNEMPLOYED);
        return workerRepository.save(worker);
    }

    public Worker updateStatus(final Long id, final int statusValue) throws WorkerNotFoundException {
        WorkerStatus status = WorkerStatus.fromValue(statusValue);
        Worker workerById = getWorkerById(id);
        workerById.setStatus(status);
        return workerRepository.save(workerById);
    }

    public Worker updatePresenceFrom(Long id, LocalDate date) throws WorkerNotFoundException {
        Worker workerById = getWorkerById(id);
        workerById.setPresenceFrom(date);
        return workerRepository.save(workerById);
    }

    public Worker updateAbsenceFrom(Long id, LocalDate date) throws WorkerNotFoundException {
        Worker workerById = getWorkerById(id);
        workerById.setAbsenceFrom(date);
        return workerRepository.save(workerById);
    }

    public Worker getWorkerById(final Long id) throws WorkerNotFoundException {
        return workerRepository.findById(id).orElseThrow(WorkerNotFoundException::new);
    }

    public List<Worker> getAllWorkers() {
        return workerRepository.findAll();
    }

    public List<Worker> getWorkersByStatus(final int statusValue) {
        WorkerStatus status = WorkerStatus.fromValue(statusValue);
        return workerRepository.findAllByStatus(status);
    }

    public List<Worker> getWorkersByPresenceFromBefore(final LocalDate from) {
        return workerRepository.findAllByPresenceFromBefore(from);
    }

    public List<Worker> getWorkersByPresenceTo(final LocalDate to) {
        return workerRepository.findAllByAbsenceFrom(to);
    }

    public List<Worker> getWorkersByTask(final Long taskId) throws TaskNotFoundException {
        Task task = taskService.getTaskById(taskId);
        return workerRepository.findAllByTask(task);
    }

    public List<Worker> getWorkersByWorkplace(final Long workplaceId) throws WorkplaceNotFoundException {
        Workplace workplace = workplaceService.getWorkplaceById(workplaceId);
        return workerRepository.findAllByWorkplace(workplace);
    }

    public List<Worker> getWorkersByWorkingDay(final Long workingDayId) throws WorkingDayNotFoundException {
        WorkingDay workingDay = workingDayService.getWorkingDayById(workingDayId);
        return workerRepository.findAllByWorkingDay(workingDay);
    }
}
