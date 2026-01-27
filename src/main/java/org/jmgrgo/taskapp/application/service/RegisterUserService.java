package org.jmgrgo.taskapp.application.service;

import org.jmgrgo.taskapp.application.dto.RegisterUserCommand;
import org.jmgrgo.taskapp.application.dto.RegisterUserResult;
import org.jmgrgo.taskapp.application.exception.EmailAlreadyExistsException;
import org.jmgrgo.taskapp.application.exception.InvalidRegistrationDataException;
import org.jmgrgo.taskapp.application.port.in.RegisterUserUseCase;
import org.jmgrgo.taskapp.application.port.out.Clock;
import org.jmgrgo.taskapp.application.port.out.PasswordHasher;
import org.jmgrgo.taskapp.application.port.out.UserRepository;
import org.jmgrgo.taskapp.domain.user.User;
import org.jmgrgo.taskapp.domain.user.value.EmailAddress;
import org.jmgrgo.taskapp.domain.user.value.PasswordHash;

import java.time.Instant;
import java.util.Objects;

public class RegisterUserService implements RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final Clock clock;

    public RegisterUserService(UserRepository userRepository, PasswordHasher passwordHasher, Clock clock) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.clock = clock;
    }

    @Override
    public RegisterUserResult registerUser(RegisterUserCommand command) {

        // Validate input data
        validateCommand(command);

        // Verify email is available
        if (userRepository.existsByEmail(command.email())){
            throw new EmailAlreadyExistsException("Email Already In Use");
        }

        // Set current operation time
        Instant now = clock.now();

        // Create user value objects
        EmailAddress email = EmailAddress.fromString(command.email());
        PasswordHash passwordHash = passwordHasher.hash(command.password());

        // Create new user and save it
        User user = User.create(email, passwordHash, null, now);
        userRepository.save(user);

        // Build result dto
        return new RegisterUserResult(
                user.getId().value(),
                user.getEmail().value(),
                user.getCreatedAt()
        );

    }

    private void validateCommand(RegisterUserCommand command) {
        Objects.requireNonNull(command, "Command is required");

        if (command.email() == null || command.email().isBlank()) {
            throw new InvalidRegistrationDataException("Email is required");
        }

        if (command.password() == null || command.password().isBlank()) {
            throw new InvalidRegistrationDataException("Password is required");
        }
    }
}
