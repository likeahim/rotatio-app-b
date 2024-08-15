package com.app.rotatio.controller;

import com.app.rotatio.controller.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalHttpErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(TaskAlreadyExistsException.class)
    public ResponseEntity<Object> handleTaskAlreadyExistsException(TaskAlreadyExistsException ex) {
        return new ResponseEntity<>("Such a task already exists", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<Object> handleTaskNotFoundException(TaskNotFoundException ex) {
        return new ResponseEntity<>("Such a task not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WorkingDayNotFoundException.class)
    public ResponseEntity<Object> handleWorkingDayNotFoundException(WorkingDayNotFoundException ex) {
        return new ResponseEntity<>("Such a working day not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WorkplaceNotFoundException.class)
    public ResponseEntity<Object> handleWorkplaceNotFoundException(WorkplaceNotFoundException ex) {
        return new ResponseEntity<>("Such a workplace not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(WorkerNotFoundException.class)
    public ResponseEntity<Object> handleWorkerNotFoundException(WorkerNotFoundException ex) {
        return new ResponseEntity<>("Worker not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex) {
        return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WorkingDayExecuteDateConflictException.class)
    public ResponseEntity<Object> handleWorkingDayExecuteDateConflictException(WorkingDayExecuteDateConflictException ex) {
        return new ResponseEntity<>("Working day for that execute date already exists", HttpStatus.CONFLICT);
    }
}
