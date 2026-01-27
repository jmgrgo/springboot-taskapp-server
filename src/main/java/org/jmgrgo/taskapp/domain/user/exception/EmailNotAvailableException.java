package org.jmgrgo.taskapp.domain.user.exception;

public class EmailNotAvailableException extends RuntimeException {
    public EmailNotAvailableException(String message) {
        super(message);
    }
}
