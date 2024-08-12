package com.app.rotatio.service;

import com.app.rotatio.domain.*;
import com.app.rotatio.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkerService {

    @Autowired
    private final WorkerRepository workerRepository;

    public Worker saveWorker(final Worker worker) {
        return workerRepository.save(worker);
    }

    public Worker deleteWorker(final Worker worker) {
        worker.setAbsenceFrom(LocalDate.now());
        worker.setStatus(WorkerStatus.UNEMPLOYED);
        return workerRepository.save(worker);
    }

    public Optional<Worker> getWorkerById(final Long id) {
        return workerRepository.findById(id);
    }

    public List<Worker> getAllWorkers() {
        return workerRepository.findAll();
    }

    public List<Worker> getWorkersByStatus(final WorkerStatus status) {
        return workerRepository.findAllByStatus(status);
    }

    public List<Worker> getWorkersByPresenceFrom(final LocalDate from) {
        return workerRepository.findAllByPresenceFrom(from);
    }

    public List<Worker> getWorkersByPresenceTo(final LocalDate to) {
        return workerRepository.findAllByAbsenceFrom(to);
    }

    public List<Worker> getWorkersByTask(final Task task) {
        return workerRepository.findAllByTask(task);
    }

    public List<Worker> getWorkersByWorkplace(final Workplace workplace) {
        return workerRepository.findAllByWorkplace(workplace);
    }

    public List<Worker> getWorkersByWorkingDay(final WorkingDay workingDay) {
        return workerRepository.findAllByWorkingDay(workingDay);
    }
}
