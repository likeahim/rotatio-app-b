package com.app.rotatio.controller;

import com.app.rotatio.domain.WorkingDayArchives;
import com.app.rotatio.domain.dto.WorkingDayArchivesDto;
import com.app.rotatio.mapper.ArchivesMapper;
import com.app.rotatio.service.ArchivesService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("v1/rotatio/archives")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ArchivesController {

    private final ArchivesService archivesService;
    private final ArchivesMapper archivesMapper;

    @SneakyThrows
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WorkingDayArchivesDto> createArchives(@RequestBody WorkingDayArchivesDto archivesDto) {
        WorkingDayArchives saved = archivesService.saveArchives(archivesMapper.mapToArchives(archivesDto));
        return ResponseEntity.ok(archivesMapper.mapToArchivesDto(saved));
    }

    @GetMapping
    public ResponseEntity<List<WorkingDayArchivesDto>> getAllArchives() {
        List<WorkingDayArchives> archives = archivesService.getAllArchives();
        return ResponseEntity.ok(archivesMapper.mapToArchivesDtoList(archives));
    }

    @SneakyThrows
    @GetMapping(value = "{archivesId}")
    public ResponseEntity<WorkingDayArchivesDto> getArchives(@PathVariable Long archivesId) {
        WorkingDayArchives archivesById = archivesService.getArchivesById(archivesId);
        return ResponseEntity.ok(archivesMapper.mapToArchivesDto(archivesById));
    }

    @SneakyThrows
    @GetMapping(value = "/byWorkingDay/{workingDayId}")
    public ResponseEntity<WorkingDayArchivesDto> getArchiveByWorkingDay(@PathVariable Long workingDayId) {
        WorkingDayArchives archivesByWorkingDay = archivesService.getArchivesByWorkingDay(workingDayId);
        return ResponseEntity.ok(archivesMapper.mapToArchivesDto(archivesByWorkingDay));
    }

    @GetMapping(value = "/byExecuted")
    public ResponseEntity<List<WorkingDayArchivesDto>> getArchivesByExecuted(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<WorkingDayArchives> allArchivesByExecuted = archivesService.getAllArchivesByExecuted(date);
        return ResponseEntity.ok(archivesMapper.mapToArchivesDtoList(allArchivesByExecuted));
    }

    }
