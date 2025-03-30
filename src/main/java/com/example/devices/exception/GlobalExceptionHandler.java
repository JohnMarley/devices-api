package com.example.devices.exception;

import com.example.devices.dto.error.GeneralErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

import static com.example.devices.exception.ErrorMessages.GENERAL_INTERNAL_SERVER_ERROR_MESSAGE;

@Slf4j
@ControllerAdvice(basePackages = "com.example.devices.controller")
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {DeviceNotFoundException.class})
    public ResponseEntity<GeneralErrorResponse> resourceNotFoundException(DeviceNotFoundException e) {
        var errorResponse = GeneralErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .statusMessage(HttpStatus.NOT_FOUND.getReasonPhrase())
                .errors(List.of(e.getMessage()))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<GeneralErrorResponse> requestDataValidationException(MethodArgumentNotValidException e) {
        var errors = e.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();
        var errorResponse = GeneralErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .statusMessage(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .errors(errors)
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = IllegalDeviceStateException.class)
    public ResponseEntity<GeneralErrorResponse> illegalDeviceStateException(IllegalDeviceStateException e) {
        var errorResponse = GeneralErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .statusMessage(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .errors(List.of(e.getMessage()))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<GeneralErrorResponse> generalException(Exception e) {
        log.error(e.getMessage());
        var errorResponse = GeneralErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .statusMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .errors(List.of(GENERAL_INTERNAL_SERVER_ERROR_MESSAGE))
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
