package com.gelileo.crud.exceptions;

import com.gelileo.crud.interfaces.GenericError;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum UserError implements GenericError {
    NotFound(HttpStatus.NOT_FOUND, "user is not found"),
    AlreadyExist(HttpStatus.BAD_REQUEST, "user already exists"),
    PasswordNotMatch(HttpStatus.BAD_REQUEST, "password does not match the user"),
    RoleMissing(HttpStatus.NOT_FOUND, "a user role is missing");


    private final HttpStatus status;
    private final String desc;
    UserError(HttpStatus status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    @Override
    public String toString() {
        return name() + ": " + desc;
    }
}
