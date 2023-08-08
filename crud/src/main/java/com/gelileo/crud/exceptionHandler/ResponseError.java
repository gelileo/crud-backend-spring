package com.gelileo.crud.exceptionHandler;

public record ResponseError(String name, String message, String reason) {
}
