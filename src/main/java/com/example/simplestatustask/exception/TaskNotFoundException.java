package com.example.simplestatustask.exception;

public class TaskNotFoundException extends RuntimeException {

    /**
     * Constructor with error message
     *
     * @param message Error message describing the exception
     */
    public TaskNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructor with error message and cause
     *
     * @param message Error message describing the exception
     * @param cause The cause of the exception
     */
    public TaskNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
