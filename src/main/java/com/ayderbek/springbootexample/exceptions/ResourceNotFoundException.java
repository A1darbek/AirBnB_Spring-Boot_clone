package com.ayderbek.springbootexample.exceptions;

import jakarta.validation.constraints.NotNull;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message, String id, @NotNull Integer userId) {
        super(message);
    }
}
