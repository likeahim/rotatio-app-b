package com.app.rotatio.controller;

import com.app.rotatio.controller.exception.PdfConcerterException;
import com.app.rotatio.domain.dto.RequestDto;
import com.app.rotatio.domain.dto.ResponseDto;
import com.app.rotatio.service.PdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/rotatio/pdf")
@RequiredArgsConstructor
@CrossOrigin("*")
public class PdfController {

    private final PdfService pdfService;

    @PostMapping
    public ResponseEntity<ResponseDto> generatePdf(@RequestBody RequestDto requestDto) throws PdfConcerterException {
        return ResponseEntity.ok(pdfService.getPdfResponse(requestDto));
    }
}
