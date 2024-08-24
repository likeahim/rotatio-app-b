package com.app.rotatio.controller;

import com.app.rotatio.domain.Archive;
import com.app.rotatio.domain.dto.ArchiveDto;
import com.app.rotatio.mapper.ArchiveMapper;
import com.app.rotatio.service.ArchiveService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/rotatio/archives")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ArchiveController {

    private final ArchiveService archiveService;
    private final ArchiveMapper archiveMapper;

    @SneakyThrows
    @PostMapping(value = "{workingDayId}")
    public ResponseEntity<ArchiveDto> createArchive(@Validated @PathVariable Long workingDayId) {
        Archive archiveById = archiveService.archive(workingDayId);
        return ResponseEntity.ok(archiveMapper.mapToArchiveDto(archiveById));
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Void> deleteArchive(@PathVariable Long id) {
        archiveService.deleteArchiveById(id);
        return ResponseEntity.ok().build();
    }

    @SneakyThrows
    @GetMapping(value = "{id}")
    public ResponseEntity<ArchiveDto> getArchiveById(@PathVariable Long id) {
        Archive archiveById = archiveService.getArchiveById(id);
        return ResponseEntity.ok(archiveMapper.mapToArchiveDto(archiveById));
    }

    @GetMapping(value = "/byWorkingDay/{workingDayId}")
    public ResponseEntity<ArchiveDto> getArchiveByWorkingDay(@PathVariable Long workingDayId) {
        Archive archiveByWorkingDay = archiveService.getArchiveByWorkingDay(workingDayId);
        return ResponseEntity.ok(archiveMapper.mapToArchiveDto(archiveByWorkingDay));
    }

    @GetMapping
    public ResponseEntity<List<ArchiveDto>> getAllArchives() {
        List<Archive> archives = archiveService.getAllArchives();
        return ResponseEntity.ok(archiveMapper.mapToArchiveDtoList(archives));
    }
}
