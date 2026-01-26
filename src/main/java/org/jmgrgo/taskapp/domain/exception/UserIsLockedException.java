package org.jmgrgo.taskapp.domain.exception;

public class UserIsLockedException extends RuntimeException {
    public UserIsLockedException(String message) {
        super(message);
    }
}
