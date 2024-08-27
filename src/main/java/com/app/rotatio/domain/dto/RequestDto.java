package com.app.rotatio.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestDto {
    private LocalDate date;
    @JsonProperty("html")
    private String html;
    @JsonProperty("name")
    private String name;
    @JsonProperty("margins")
    private String margins;
    @JsonProperty("paperSize")
    private String paperSize;
    @JsonProperty("orientation")
    private String orientation;
    @JsonProperty("printBackground")
    private boolean printBackground;
    @JsonProperty("async")
    private boolean async;
    @JsonProperty("footer")
    private String footer;
    @JsonProperty("header")
    private String header;
}
