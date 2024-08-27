package com.app.rotatio.api.pdfco.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class PdfCoConfig {

    @Value("${pdf.co.api-key}")
    private String apiKey;
    @Value("${pdf.co.endpoint}")
    private String endpoint;
}
