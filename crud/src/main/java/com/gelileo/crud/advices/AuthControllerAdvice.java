package com.gelileo.crud.advices;

import com.gelileo.crud.exceptions.TokenRefreshException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthControllerAdvice {
    @ExceptionHandler(value = TokenRefreshException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ResponseError> handleTokenRefreshException(TokenRefreshException ex) {

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ResponseError("TokenRefreshException", ex.getMessage(), ex.getCause().toString()));
    }
}
