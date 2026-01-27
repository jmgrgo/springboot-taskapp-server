package org.jmgrgo.taskapp.domain.user.value;

import java.util.Objects;
import java.util.UUID;

/**
 * A Value Object representing a unique identifier for a User.
 * @param value The UUID value.
 */
public record UserId(UUID value) {

    public UserId {

        // Enforce NonNull input
        Objects.requireNonNull(value, "UserId must not be null");
    }

    /**
     * Generates a new random UserId.
     * @return the generated UserId
     */
    public static UserId newId() {
        return new UserId(UUID.randomUUID());
    }

    /**
     * Creates a UserId from a raw UUID string.
     * @throws IllegalArgumentException if the string is not a valid UUID format.
     * @throws NullPointerException if the value is null.
     */
    public static UserId fromString(String value) {
        return new UserId(UUID.fromString(value));
    }

    /**
     * Checks if a string is a valid UUID before attempting to create a UserId.
     * @param value the UUID raw string
     * @return true if the string is a valid UUID, false otherwise
     */
    public static boolean isValid(String value) {

        // If the uuid is null
        if (value == null) return false;

        try {
            // Parse raw string to UUID
            UUID.fromString(value);

            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }
}