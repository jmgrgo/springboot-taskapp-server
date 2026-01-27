package org.jmgrgo.taskapp.application.exception;

public class InvalidRegistrationDataException extends RuntimeException {
    public InvalidRegistrationDataException(String message) {
        super(message);
    }
}
