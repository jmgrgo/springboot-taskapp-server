package org.jmgrgo.taskapp.domain.user.value;

import org.jmgrgo.taskapp.domain.user.exception.InvalidEmailFormatException;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * A Value Object representing a validated and normalized email address.
 * @param value The standardized email string
 */
public record EmailAddress(String value) {

    // Regex for basic email validation
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,63}$", Pattern.CASE_INSENSITIVE);

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
     * Creates a new EmailAddress
     * @param rawEmail raw email string
     * @return the created EmailAddress value object
     * @throws IllegalArgumentException if the string is not a valid Email Address format.
     * @throws NullPointerException if the value is null.
     */
    public static EmailAddress fromString(String rawEmail) {
        return new EmailAddress(rawEmail);
    }

    /**
     * Performs a null-safe check to determine if a string is a valid email.
     * @param value The raw email string to check
     * @return true if the format is valid after normalization, false otherwise
     */
    public static boolean isValid(String value) {

        // If the value
        if (value == null) return false;

        // Check the normalized version
        return isFormatValid(value.trim().toLowerCase());

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