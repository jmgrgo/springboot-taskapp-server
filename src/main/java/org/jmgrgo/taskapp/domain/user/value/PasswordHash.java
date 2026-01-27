package org.jmgrgo.taskapp.domain.user.value;

import org.jmgrgo.taskapp.domain.user.exception.InvalidPasswordFormatException;

import java.util.Objects;

/**
 * A Value Object representing a securely hashed password.
 */
public record PasswordHash(String value) {

    public PasswordHash {

        // Enforce NonNull input
        Objects.requireNonNull(value, "Password hash cannot be null");

        // Check if it's a valid Bcrypt hash
        if (value.length() < 60) {
            throw new InvalidPasswordFormatException("Invalid password hash format");
        }
    }

    /**
     * Factory method to wrap a pre-computed hash.
     */
    public static PasswordHash fromString(String hash) {
        return new PasswordHash(hash);
    }

    @Override
    public String toString() {
        return "PasswordHash[PROTECTED]";
    }
}