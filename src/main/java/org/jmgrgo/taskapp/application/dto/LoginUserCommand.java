package org.jmgrgo.taskapp.application.dto;

public record LoginUserCommand(
        String email,
        String password
) {}
