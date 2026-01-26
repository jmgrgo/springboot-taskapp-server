package org.jmgrgo.taskapp.domain.model.user;

import org.jmgrgo.taskapp.domain.exception.InvalidEmailFormatException;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * A Value Object representing a validated and normalized email address.
 * @param value The standardized email string
 */
public record EmailAddress(String value) {

    // Regex far basic email validation
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public EmailAddress {

        // Enforce NonNull input
        Objects.requireNonNull(value, "Email cannot be null");

        // Normalize input
        value = value.trim().toLowerCase();

        // Use the static check to enforce the invariant
        if (!isFormatValid(value)) {
            throw new InvalidEmailFormatException("Invalid email format");
        }
    }

    /**
     * Performs a null-safe check to determine if a string is a valid email.
     * @param email The raw email string to check
     * @return true if the format is valid after normalization, false otherwise
     */
    public static boolean isValid(String email) {

        // If there's no email
        if (email == null) return false;

        // Check the normalized version
        return isFormatValid(email.trim().toLowerCase());

    }

    /**
     * Checks if an email follows a valid format.
     * @param normalizedEmail the normalized email string to validate
     * @return true if the email format is valid, false otherwise
     */
    private static boolean isFormatValid(String normalizedEmail) {
        return !normalizedEmail.isBlank() && EMAIL_PATTERN.matcher(normalizedEmail).matches();
    }

    @Override
    public String toString() {
        return value;
    }
}