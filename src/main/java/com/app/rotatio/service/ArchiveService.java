package com.app.rotatio.service;

import com.app.rotatio.controller.exception.*;
import com.app.rotatio.domain.Archive;
import com.app.rotatio.domain.Worker;
import com.app.rotatio.domain.WorkingDay;
import com.app.rotatio.repository.ArchiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArchiveService {

    private final ArchiveRepository archiveRepository;
    private final WorkingDayService workingDayService;

    public Archive saveArchive(final Archive archive) {
        return archiveRepository.save(archive);
    }

    public Archive getArchiveById(final Long id) throws ArchiveNotFoundException {
        return archiveRepository.findById(id).orElseThrow(ArchiveNotFoundException::new);
    }

    public void deleteArchiveById(final Long id) {
        archiveRepository.deleteById(id);
    }

    public Archive archive(final Long workingDayId) throws WorkingDayNotFoundException, ArchiveProcessException {
        StringBuilder builder = new StringBuilder();

        try {
            WorkingDay workingDay = workingDayService.getWorkingDayById(workingDayId);
            for (Worker worker : workingDay.getWorkers()) {
                String workerData = worker.getFirstName() + " " + worker.getLastName() +
                                    ", " + worker.getTask().getName() +
                                    ", " + worker.getWorkplace().getDesignation();
                builder.append(workerData).append(" | ");
            }
            String workersData = builder.toString();

            if (workersData.endsWith(" | ")) {
                workersData = workersData.substring(0, workersData.length() - 3);
            }

            Archive archive = Archive.builder()
                    .workingDayId(workingDayId)
                    .workersData(workersData)
                    .build();

            return saveArchive(archive);
        } catch (Exception e) {
            throw new ArchiveProcessException("Failed to archive WorkingDay ID: " + workingDayId);
        }
    }
}
