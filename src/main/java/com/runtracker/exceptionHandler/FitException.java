package com.runtracker.exceptionHandler;

public class FitException extends Exception {
    public FitException(String message, Exception e) {
        super(message, e);
    }
}
