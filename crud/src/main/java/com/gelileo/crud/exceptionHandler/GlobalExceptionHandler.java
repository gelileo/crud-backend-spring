package com.gelileo.crud.exceptionHandler;

import com.gelileo.crud.validation.ValidationMessage;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
                .body(new ResponseError("ResponseStatusException", ex.getMessage(), ex.getBody().getDetail() == null ? "" : ex.getBody().getDetail()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseError> handleAccessDeniedException(AccessDeniedException ex) {

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ResponseError("Access Denied: ", ex.getMessage(),  ""));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseError> handleValidationException(MethodArgumentNotValidException ex) {

        ValidationMessage validationMessage = new ValidationMessage(ex.getMessage());
        String generalMsg = validationMessage.getValidationError().getGeneralMessage();
        String errorMsg = validationMessage.getValidationError().getDefaultMessage();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ResponseError("Validation error", generalMsg,  errorMsg));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseError> handleConstraintViolationException(ConstraintViolationException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ResponseError("Validation error: ", ex.getMessage(),  ""));
    }
    // Other exception handling methods...
}

