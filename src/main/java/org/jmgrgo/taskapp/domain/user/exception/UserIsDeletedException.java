package org.jmgrgo.taskapp.domain.user.exception;

public class UserIsDeletedException extends RuntimeException {
    public UserIsDeletedException(String message) {
        super(message);
    }
}
