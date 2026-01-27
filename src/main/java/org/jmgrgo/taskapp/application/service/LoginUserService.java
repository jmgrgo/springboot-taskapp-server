package org.jmgrgo.taskapp.application.service;

import org.jmgrgo.taskapp.application.dto.LoginUserCommand;
import org.jmgrgo.taskapp.application.dto.LoginUserResult;
import org.jmgrgo.taskapp.application.exception.InvalidCredentialsException;
import org.jmgrgo.taskapp.application.port.in.LoginUserUseCase;
import org.jmgrgo.taskapp.application.port.out.Clock;
import org.jmgrgo.taskapp.application.port.out.PasswordMatchVerifier;
import org.jmgrgo.taskapp.application.port.out.TokenGenerator;
import org.jmgrgo.taskapp.application.port.out.UserRepository;
import org.jmgrgo.taskapp.domain.user.User;
import org.jmgrgo.taskapp.domain.user.value.EmailAddress;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public class LoginUserService implements LoginUserUseCase {

    private static final Duration LOCK_DURATION = Duration.ofMinutes(15);

    private final UserRepository userRepository;
    private final PasswordMatchVerifier passwordVerifier;
    private final TokenGenerator tokenGenerator;
    private final Clock clock;

    public LoginUserService(UserRepository userRepository, PasswordMatchVerifier passwordVerifier, TokenGenerator tokenGenerator, Clock clock) {
        this.userRepository = userRepository;
        this.passwordVerifier = passwordVerifier;
        this.tokenGenerator = tokenGenerator;
        this.clock = clock;
    }

    @Override
    public LoginUserResult loginUser(LoginUserCommand command) {

        // Validate input data
        validate(command);

        // Set current operation time
        Instant now = clock.now();

        // Find user
        User user = userRepository
                .findByEmail(EmailAddress.fromString(command.email()).value());

        // Check if command password matches user password
        if (!passwordVerifier.matches(command.password(), user.getPasswordHash())) {
            user.recordFailedLogin(now, LOCK_DURATION);
            userRepository.save(user);
            throw new InvalidCredentialsException("Invalid credentials");
        }

        // Record login and save
        user.recordSuccessfulLogin(now);
        userRepository.save(user);

        // Generate token
        String token = tokenGenerator.generate(user);

        // Build result dto
        return new LoginUserResult(token);
    }

    private void validate(LoginUserCommand command) {
        Objects.requireNonNull(command, "Command is required");

        if (command.email() == null || command.email().isBlank()) {
            throw new InvalidCredentialsException("Email is required");
        }

        if (command.password() == null || command.password().isBlank()) {
            throw new InvalidCredentialsException("Password is required");
        }
    }

}
