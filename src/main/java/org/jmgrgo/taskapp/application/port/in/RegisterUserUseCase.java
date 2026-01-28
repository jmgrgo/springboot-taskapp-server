package org.jmgrgo.taskapp.application.port.in;

import org.jmgrgo.taskapp.application.dto.RegisterUserCommand;
import org.jmgrgo.taskapp.application.dto.RegisterUserResult;

public interface RegisterUserUseCase {
    RegisterUserResult registerUser(RegisterUserCommand command);
}
