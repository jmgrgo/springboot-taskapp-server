package org.jmgrgo.taskapp.application.port.out;

import org.jmgrgo.taskapp.domain.user.User;

public interface UserRepository {
    User save(User user);
    boolean existsByEmail(String email);
    User findByEmail(String email);
}
