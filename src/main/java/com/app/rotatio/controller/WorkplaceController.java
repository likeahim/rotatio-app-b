package com.app.rotatio.controller;

import com.app.rotatio.domain.Workplace;
import com.app.rotatio.domain.dto.WorkplaceDto;
import com.app.rotatio.mapper.WorkplaceMapper;
import com.app.rotatio.service.WorkplaceService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/rotatio/workplaces")
@RequiredArgsConstructor
@CrossOrigin("*")
public class WorkplaceController {

    private final WorkplaceService workplaceService;
    private final WorkplaceMapper workplaceMapper;

    @SneakyThrows
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WorkplaceDto> createWorkplace(@Validated @RequestBody WorkplaceDto workplaceDto) {
        Workplace workplace = workplaceService.saveWorkplace(workplaceMapper.mapToWorkplace(workplaceDto));
        return ResponseEntity.ok(workplaceMapper.mapToWorkplaceDto(workplace));
    }

    @SneakyThrows
    @PutMapping
    public ResponseEntity<WorkplaceDto> updateWorkplace(@Validated @RequestBody WorkplaceDto workplaceDto) {
        Workplace workplace = workplaceService.saveWorkplace(workplaceMapper.mapToWorkplace(workplaceDto));
        return ResponseEntity.ok(workplaceMapper.mapToWorkplaceDto(workplace));
    }

    @SneakyThrows
    @DeleteMapping("{workplaceId}")
    public ResponseEntity<WorkplaceDto> deleteWorkplace(@PathVariable Long workplaceId) {
        Workplace workplaceById = workplaceService.getWorkplaceById(workplaceId);
        Workplace workplace = workplaceService.deleteWorkplace(workplaceById);
        return ResponseEntity.ok(workplaceMapper.mapToWorkplaceDto(workplace));
    }

    @GetMapping
    public ResponseEntity<List<WorkplaceDto>> getAllWorkplaces() {
        List<Workplace> workplaces = workplaceService.getAllWorkplaces();
        return ResponseEntity.ok(workplaceMapper.mapToWorkplaceDtoList(workplaces));
    }

    @SneakyThrows
    @GetMapping(value = "{workplaceId}")
    public ResponseEntity<WorkplaceDto> getWorkplaceById(@PathVariable Long workplaceId) {
        Workplace workplaceById = workplaceService.getWorkplaceById(workplaceId);
        return ResponseEntity.ok(workplaceMapper.mapToWorkplaceDto(workplaceById));
    }

    @GetMapping("/byActive/{active}")
    public ResponseEntity<List<WorkplaceDto>> getWorkplacesByActive(@PathVariable boolean active) {
        List<Workplace> allByActive = workplaceService.getAllByActive(active);
        return ResponseEntity.ok(workplaceMapper.mapToWorkplaceDtoList(allByActive));
    }

    @GetMapping("/byNowUsed/{nowUsed}")
    public ResponseEntity<List<WorkplaceDto>> getWorkplacesByNowUsed(@PathVariable boolean nowUsed) {
        List<Workplace> allByNowUsed = workplaceService.getAllByNowUsed(nowUsed);
        return ResponseEntity.ok(workplaceMapper.mapToWorkplaceDtoList(allByNowUsed));
    }
}
