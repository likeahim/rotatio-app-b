package com.app.rotatio.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum WorkerStatus {
    PRESENT(1),
    ABSENT(2),
    UNEMPLOYED(3);

    private final int value;

    public static WorkerStatus fromValue(int value) {
        return switch (value) {
            case 1 -> PRESENT;
            case 2 -> ABSENT;
            case 3 -> UNEMPLOYED;
            default -> null;
        };
    }
}
