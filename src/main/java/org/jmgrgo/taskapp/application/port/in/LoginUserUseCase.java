package org.jmgrgo.taskapp.application.port.in;

import org.jmgrgo.taskapp.application.dto.LoginUserCommand;
import org.jmgrgo.taskapp.application.dto.LoginUserResult;
public interface LoginUserUseCase {
    LoginUserResult loginUser(LoginUserCommand command);
}
