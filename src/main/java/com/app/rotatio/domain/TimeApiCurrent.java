package com.app.rotatio.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeApiCurrent {
    private int year;
    private int month;
    private int day;
    private String date;
}
