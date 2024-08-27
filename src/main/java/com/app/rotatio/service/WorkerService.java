package com.app.rotatio.service;

import com.app.rotatio.controller.exception.WorkerNotFoundException;
import com.app.rotatio.controller.exception.WorkingDayNotFoundException;
import com.app.rotatio.domain.*;
import com.app.rotatio.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkerService {

    @Autowired
    private final WorkerRepository workerRepository;
    @Autowired
    private final WorkingDayService workingDayService;

    public Worker saveWorker(final Worker worker) {
        return workerRepository.save(worker);
    }

    public Worker deleteWorker(final Long id) throws WorkerNotFoundException {
        Worker worker = getWorkerById(id);
        worker.setStatus(WorkerStatus.UNEMPLOYED);
        return workerRepository.save(worker);
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

    public List<Worker> getWorkersByWorkingDay(final Long workingDayId) throws WorkingDayNotFoundException {
        WorkingDay workingDay = workingDayService.getWorkingDayById(workingDayId);
        return workerRepository.findAllByWorkingDay(workingDay);
    }

    public List<Worker> longToWorkersList(List<Long> workers) {
        if (workers.isEmpty()) {
            return Collections.emptyList();
        }
        return workers.stream()
                .map(id -> {
                    try {
                        return getWorkerById(id);
                    } catch (WorkerNotFoundException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                })
                .toList();
    }

    public List<Long> workersToLongList(List<Worker> workers) {
        if (workers.isEmpty()) {
            return Collections.<Long>emptyList();
        }
        return workers.stream()
                .map(Worker::getId)
                .toList();
    }
}
