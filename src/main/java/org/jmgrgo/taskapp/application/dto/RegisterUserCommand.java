package org.jmgrgo.taskapp.application.dto;

public record RegisterUserCommand(
        String email,
        String password
) {}
