package com.tango.gateway_service.exceptions;


import com.tango.gateway_service.utils.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;


@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<?> exceptionHandler(Exception exception){
        var response = ApiResponse.builder()
                .referenceId(UUID.randomUUID().toString())
                .requestTime(LocalDateTime.now())
                .requestType("Outbound")
                .message("An error occurred")
                .status(false)
                .error(exception.getMessage())
                .build();
        return ResponseEntity.badRequest().body(response);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException ex) {

        var response = ApiResponse.builder()
                .referenceId(UUID.randomUUID().toString())
                .requestTime(LocalDateTime.now())
                .requestType("Outbound")
                .message("Operation not successful")
                .status(false)
                .error(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, BAD_REQUEST);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = MissingAuthorizationHeaderException.class)
    public ResponseEntity<?> handleMissingAuthorizationHeaderException(MissingAuthorizationHeaderException ex) {

        var response = ApiResponse.builder()
                .referenceId(UUID.randomUUID().toString())
                .requestTime(LocalDateTime.now())
                .requestType("Outbound")
                .message("Operation not successful")
                .status(false)
                .error(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, UNAUTHORIZED);
    }
}


