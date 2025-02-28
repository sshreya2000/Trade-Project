package com.tp.tradexcelsior.exception;

public class DuplicateChecklistException extends RuntimeException {
    public DuplicateChecklistException(String message) {
        super(message);
    }
}