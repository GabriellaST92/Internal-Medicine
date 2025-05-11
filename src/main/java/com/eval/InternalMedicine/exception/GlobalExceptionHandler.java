package com.eval.InternalMedicine.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TimeConflictException.class)
    public ResponseEntity<ErrorResponse>handleDoctorAvailabilityException(TimeConflictException ex){
        return new ResponseEntity<>(
                new ErrorResponse("TimeConflict", ex.getMessage()),
                HttpStatus.CONFLICT
        );
    }
    @ExceptionHandler(InvalidAppointmentException.class)
    public ResponseEntity<ErrorResponse> handleInvalidAppointment(InvalidAppointmentException ex) {
        return new ResponseEntity<>(
                new ErrorResponse("Invalid appointment", ex.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        return new ResponseEntity<>(
                new ErrorResponse("Server error", "Something went wrong. Please try again later."),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

}
