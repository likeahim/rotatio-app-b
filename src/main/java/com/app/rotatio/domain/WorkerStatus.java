package com.app.rotatio.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum WorkerStatus {
    PRESENT(1),
    ABSENT(2),
    UNEMPLOYED(3);

    private final int value;
}
