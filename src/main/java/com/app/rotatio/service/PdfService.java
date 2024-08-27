package com.app.rotatio.service;

import com.app.rotatio.api.pdfco.client.PdfCoClient;
import com.app.rotatio.controller.exception.PdfConcerterException;
import com.app.rotatio.domain.dto.RequestDto;
import com.app.rotatio.domain.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PdfService {

    private final PdfCoClient pdfCoClient;

    public ResponseDto getPdfResponse(final RequestDto requestDto) throws PdfConcerterException {
        return pdfCoClient.sendHtmlToPdf(requestDto);
    }
}
