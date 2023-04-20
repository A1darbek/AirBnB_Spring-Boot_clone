package com.ayderbek.springbootexample.exceptions;

public class ConflictException extends RuntimeException{
    public ConflictException(String message) {
        super(message);
    }
}
