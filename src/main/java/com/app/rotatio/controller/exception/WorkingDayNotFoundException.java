package com.app.rotatio.controller.exception;

public class WorkingDayNotFoundException extends Exception {

    public WorkingDayNotFoundException() {
        super("Such a working day does not exist");
    }
}
