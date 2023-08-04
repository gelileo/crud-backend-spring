package com.gelileo.crud.advices;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ResponseError> handleResponseStatusException(ResponseStatusException ex) {
//        return ResponseEntity.status(ex.getStatus()).body(ex.getReason());
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(new ResponseError("ResponseStatusException", ex.getMessage(), ""));
    }

    // Other exception handling methods...
}

