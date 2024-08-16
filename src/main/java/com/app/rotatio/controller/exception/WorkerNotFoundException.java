package com.app.rotatio.controller.exception;

public class WorkerNotFoundException extends Exception {
    public WorkerNotFoundException() {
        super("Such a worker not found");
    }
}
