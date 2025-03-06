package com.tp.tradexcelsior.exception;

public class ChecklistNotFoundException extends RuntimeException {
    public ChecklistNotFoundException(String message) {
        super(message);
    }
}