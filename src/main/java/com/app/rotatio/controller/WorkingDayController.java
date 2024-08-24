package com.app.rotatio.controller;

import com.app.rotatio.domain.WorkingDay;
import com.app.rotatio.domain.dto.WorkingDayDto;
import com.app.rotatio.mapper.WorkingDayMapper;
import com.app.rotatio.service.WorkingDayService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("v1/rotatio/workingDays")
@RequiredArgsConstructor
@CrossOrigin("*")
public class WorkingDayController {

    private final WorkingDayService workingDayService;
    private final WorkingDayMapper workingDayMapper;

    @SneakyThrows
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<WorkingDayDto> createWorkingDay(@Validated @RequestBody WorkingDayDto workingDayDto) {
        WorkingDay workingDay = workingDayService.createNewWorkingDay(workingDayMapper.mapToWorkingDay(workingDayDto));
        return ResponseEntity.ok(workingDayMapper.mapToWorkingDayDto(workingDay));
    }

    @GetMapping
    public ResponseEntity<List<WorkingDayDto>> getWorkingDays() {
        List<WorkingDay> allWorkingDays = workingDayService.getAllWorkingDays();
        return ResponseEntity.ok(workingDayMapper.mapToWorkingDayDtoList(allWorkingDays));
    }

    @SneakyThrows
    @GetMapping(value = "{workingDayId}")
    public ResponseEntity<WorkingDayDto> getWorkingDayById(@PathVariable("workingDayId") Long workingDayId) {
        WorkingDay workingDayById = workingDayService.getWorkingDayById(workingDayId);
        return ResponseEntity.ok(workingDayMapper.mapToWorkingDayDto(workingDayById));
    }

    @SneakyThrows
    @GetMapping("/byUser/{userId}")
    public ResponseEntity<List<WorkingDayDto>> getWorkingDaysByUserId(@PathVariable("userId") Long userId) {
        List<WorkingDay> allByUser = workingDayService.getAllByUser(userId);
        return ResponseEntity.ok(workingDayMapper.mapToWorkingDayDtoList(allByUser));
    }

    @GetMapping("/planned/{planned}")
    public ResponseEntity<List<WorkingDayDto>> getWorkingDaysByPlanned(@PathVariable("planned") boolean planned) {
        List<WorkingDay> allByPlanned = workingDayService.getAllByPlanned(planned);
        return ResponseEntity.ok(workingDayMapper.mapToWorkingDayDtoList(allByPlanned));
    }

    @SneakyThrows
    @GetMapping("/execute/{executeDate}")
    public ResponseEntity<WorkingDayDto> getWorkingDayByExecuteDate(
            @PathVariable("executeDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate executeDate) {
        WorkingDay byExecuteDate = workingDayService.getByExecuteDate(executeDate);
        return ResponseEntity.ok(workingDayMapper.mapToWorkingDayDto(byExecuteDate));
    }

    @GetMapping(value = "byArchived/{archived}")
    public ResponseEntity<List<WorkingDayDto>> getWorkingDaysByArchived(@PathVariable boolean archived) {
        List<WorkingDay> allByArchived = workingDayService.getAllByArchived(archived);
        return ResponseEntity.ok(workingDayMapper.mapToWorkingDayDtoList(allByArchived));
    }

    @SneakyThrows
    @PutMapping
    public ResponseEntity<WorkingDayDto> updateWorkingDay(@Validated @RequestBody WorkingDayDto workingDayDto) {
        WorkingDay workingDay = workingDayService.saveWorkingDay(workingDayMapper.mapToWorkingDay(workingDayDto));
        return ResponseEntity.ok(workingDayMapper.mapToWorkingDayDto(workingDay));
    }

    @DeleteMapping(value = "{workingDayId}")
    public ResponseEntity<Void> deleteWorkingDay(@PathVariable Long workingDayId) {
        workingDayService.deleteById(workingDayId);
        return ResponseEntity.ok().build();
    }
}
