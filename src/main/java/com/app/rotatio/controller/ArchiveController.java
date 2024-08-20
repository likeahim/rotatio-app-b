package com.app.rotatio.controller;

import com.app.rotatio.domain.Archive;
import com.app.rotatio.domain.dto.ArchiveDto;
import com.app.rotatio.mapper.ArchiveMapper;
import com.app.rotatio.service.ArchiveService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/rotatio/archives")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ArchiveController {

    private final ArchiveService archiveService;
    private final ArchiveMapper archiveMapper;

    @SneakyThrows
    @PostMapping(value = "{workingDayId}")
    public ResponseEntity<ArchiveDto> createArchive(@PathVariable Long workingDayId) {
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
}
