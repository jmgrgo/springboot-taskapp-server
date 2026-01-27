package org.jmgrgo.taskapp.application.port.out;

import org.jmgrgo.taskapp.domain.user.User;

public interface TokenGenerator {
    String generate(User user);
}