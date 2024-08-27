package com.app.rotatio.api.pdfco.client;

import com.app.rotatio.api.pdfco.config.PdfCoConfig;
import com.app.rotatio.controller.exception.PdfConcerterException;
import com.app.rotatio.domain.dto.RequestDto;
import com.app.rotatio.domain.dto.ResponseDto;
import com.app.rotatio.service.UriService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class PdfCoClient {

    private final PdfCoConfig config;
    private final RestTemplate restTemplate;
    private final UriService uriService;

    public ResponseDto sendHtmlToPdf(final RequestDto requestDto) throws PdfConcerterException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", config.getApiKey());
        HttpEntity<RequestDto> request = new HttpEntity<>(requestDto, headers);
        URI uri = uriService.getUri(config.getEndpoint());

        try {
            return restTemplate.postForObject(uri, request, ResponseDto.class);
        } catch (Exception e) {
            throw new PdfConcerterException(e.getMessage());
        }
    }
}
