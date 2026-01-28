package org.jmgrgo.taskapp.application.port.out;

import org.jmgrgo.taskapp.domain.user.value.PasswordHash;

public interface PasswordHasher {
    PasswordHash hash(String rawPassword);
}
