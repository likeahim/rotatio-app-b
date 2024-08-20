package com.app.rotatio.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
public class Mail {
    @NonNull
    private String from;
    @NonNull
    private String to;
    @NonNull
    private String subject;
    @NonNull
    private String body;
    private String toCc;
}
