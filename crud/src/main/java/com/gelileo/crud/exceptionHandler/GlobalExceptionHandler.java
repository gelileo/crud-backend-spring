package com.gelileo.crud.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseError> handleAccessDeniedException(AccessDeniedException ex) {

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ResponseError("Access Denied: ", ex.getMessage(),  ""));
    }

    // Other exception handling methods...
}

