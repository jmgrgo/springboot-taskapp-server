package org.jmgrgo.taskapp.application.port.out;

import java.time.Instant;

public interface Clock {
    Instant now();
}
