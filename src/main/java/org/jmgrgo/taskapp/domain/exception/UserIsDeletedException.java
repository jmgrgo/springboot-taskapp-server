package org.jmgrgo.taskapp.domain.exception;

public class UserIsDeletedException extends RuntimeException {
    public UserIsDeletedException(String message) {
        super(message);
    }
}
