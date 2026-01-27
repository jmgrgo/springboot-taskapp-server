package org.jmgrgo.taskapp.domain.user.exception;

public class UserIsLockedException extends RuntimeException {
    public UserIsLockedException(String message) {
        super(message);
    }
}
