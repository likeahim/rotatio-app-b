package com.app.rotatio.controller;

import com.app.rotatio.domain.Worker;
import com.app.rotatio.domain.dto.WorkerDto;
import com.app.rotatio.mapper.WorkerMapper;
import com.app.rotatio.service.WorkerService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("v1/rotatio/workers")
@RequiredArgsConstructor
@CrossOrigin("*")
public class WorkerController {

    private final WorkerService workerService;
    private final WorkerMapper workerMapper;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WorkerDto> createWorker(@RequestBody WorkerDto workerDto) {
        Worker worker = workerService.saveWorker(workerMapper.mapToWorker(workerDto));
        return ResponseEntity.ok(workerMapper.mapToWorkerDto(worker));
    }

    @PutMapping
    public ResponseEntity<WorkerDto> updateWorker(@RequestBody WorkerDto workerDto) {
        Worker worker = workerService.saveWorker(workerMapper.mapToWorker(workerDto));
        return ResponseEntity.ok(workerMapper.mapToWorkerDto(worker));
    }

    @SneakyThrows
    @PatchMapping(value = "/updateStatus/{workerId}/{status}")
    public ResponseEntity<WorkerDto> updateWorkerStatus(@PathVariable Long workerId, @PathVariable int status) {
        Worker worker = workerService.updateStatus(workerId, status);
        return ResponseEntity.ok(workerMapper.mapToWorkerDto(worker));
    }

    @SneakyThrows
    @PatchMapping(value = "/updatePresence/{workerId}/{date}")
    public ResponseEntity<WorkerDto> updatePresence(@PathVariable Long workerId, @PathVariable LocalDate date) {
        Worker worker = workerService.updatePresenceFrom(workerId, date);
        return ResponseEntity.ok(workerMapper.mapToWorkerDto(worker));
    }

    @SneakyThrows
    @PatchMapping(value = "/updateAbsence/{workerId}/{date}")
    public ResponseEntity<WorkerDto> updateAbsence(@PathVariable Long workerId, @PathVariable LocalDate date) {
        Worker worker = workerService.updateAbsenceFrom(workerId, date);
        return ResponseEntity.ok(workerMapper.mapToWorkerDto(worker));
    }

    @SneakyThrows
    @DeleteMapping(value = "{workerId}")
    public ResponseEntity<WorkerDto> deleteWorker(@PathVariable Long workerId) {
        Worker worker = workerService.deleteWorker(workerId);
        return ResponseEntity.ok(workerMapper.mapToWorkerDto(worker));
    }

    @GetMapping
    public ResponseEntity<List<WorkerDto>> getAllWorkers() {
        List<Worker> allWorkers = workerService.getAllWorkers();
        return ResponseEntity.ok(workerMapper.mapToWorkerDtoList(allWorkers));
    }

    @SneakyThrows
    @GetMapping(value = "{workerId}")
    public ResponseEntity<WorkerDto> getWorkerById(@PathVariable Long workerId) {
        Worker workerById = workerService.getWorkerById(workerId);
        return ResponseEntity.ok(workerMapper.mapToWorkerDto(workerById));
    }

    @SneakyThrows
    @GetMapping(value = "/byStatus/{statusValue}")
    public ResponseEntity<List<WorkerDto>> getWorkersByStatus(@PathVariable int statusValue) {
        List<Worker> workersByStatus = workerService.getWorkersByStatus(statusValue);
        return ResponseEntity.ok(workerMapper.mapToWorkerDtoList(workersByStatus));
    }

    @SneakyThrows
    @GetMapping(value = "/byPresenceBefore/{date}")
    public ResponseEntity<List<WorkerDto>> getWorkersByPresenceFrom(@PathVariable LocalDate date) {
        List<Worker> workersByPresenceFrom = workerService.getWorkersByPresenceFromBefore(date);
        return ResponseEntity.ok(workerMapper.mapToWorkerDtoList(workersByPresenceFrom));
    }

    @GetMapping(value = "/byAbsenceFrom/{date}")
    public ResponseEntity<List<WorkerDto>> getWorkersByAbsenceFrom(@PathVariable LocalDate date) {
        List<Worker> workersByPresenceTo = workerService.getWorkersByPresenceTo(date);
        return ResponseEntity.ok(workerMapper.mapToWorkerDtoList(workersByPresenceTo));
    }

    @SneakyThrows
    @GetMapping(value = "/byTask/{taskId}")
    public ResponseEntity<List<WorkerDto>> getWorkersByTask(@PathVariable Long taskId) {
        List<Worker> workersByTask = workerService.getWorkersByTask(taskId);
        return ResponseEntity.ok(workerMapper.mapToWorkerDtoList(workersByTask));
    }

    @SneakyThrows
    @GetMapping(value = "/byWorkplace/{workplaceId}")
    public ResponseEntity<List<WorkerDto>> getWorkersByWorkplace(@PathVariable Long workplaceId) {
        List<Worker> workers = workerService.getWorkersByWorkplace(workplaceId);
        return ResponseEntity.ok(workerMapper.mapToWorkerDtoList(workers));
    }

    @SneakyThrows
    @GetMapping(value = "/byWorkingDay/{workingDayId}")
    public ResponseEntity<List<WorkerDto>> getWorkersByWorkingDay(@PathVariable Long workingDayId) {
        List<Worker> workers = workerService.getWorkersByWorkingDay(workingDayId);
        return ResponseEntity.ok(workerMapper.mapToWorkerDtoList(workers));
    }

}
