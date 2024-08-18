package com.app.rotatio.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeApiCurrent {
    private int year;
    private int month;
    private int day;
    private String date;
}
