package com.runnertracker.exceptionHandler;

public class FitException extends Exception {
    public FitException(String message, Exception e) {
        super(message);
    }
}
