package org.jmgrgo.taskapp.domain.user;

import org.jmgrgo.taskapp.domain.user.exception.UserIsDeletedException;
import org.jmgrgo.taskapp.domain.user.exception.UserIsLockedException;
import org.jmgrgo.taskapp.domain.user.value.*;

import java.time.Duration;
import java.time.Instant;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a User Account in the system.
 */
public class User {

    private final UserId id;

    private EmailAddress email;
    private boolean emailVerified;
    private Instant emailUpdatedAt;

    private PasswordHash passwordHash;
    private Instant passwordUpdatedAt;

    private UserStatus status;
    private final Set<UserRole> roles;

    private int failedLoginAttempts;
    private Instant accountLockedUntil;

    private final Instant createdAt;
    private Instant updatedAt;
    private Instant lastLoginAt;
    private Instant lastActiveAt;

    private Instant deletedAt;

    private User(Builder builder) {
        this.id = Objects.requireNonNull(builder.id);
        this.email = Objects.requireNonNull(builder.email);
        this.emailVerified = builder.emailVerified;
        this.emailUpdatedAt = builder.emailUpdatedAt;
        this.passwordHash = builder.passwordHash;
        this.passwordUpdatedAt = builder.passwordUpdatedAt;
        this.status = builder.status;
        this.roles = (builder.roles == null || builder.roles.isEmpty())
                ? EnumSet.noneOf(UserRole.class)
                : EnumSet.copyOf(builder.roles);
        this.failedLoginAttempts = builder.failedLoginAttempts;
        this.accountLockedUntil = builder.accountLockedUntil;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
        this.lastLoginAt = builder.lastLoginAt;
        this.lastActiveAt = builder.lastActiveAt;
        this.deletedAt = builder.deletedAt;
    }

    public UserId getId() {
        return id;
    }

    public EmailAddress getEmail() {
        return email;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public Instant getEmailUpdatedAt() {
        return emailUpdatedAt;
    }

    public PasswordHash getPasswordHash() {
        return passwordHash;
    }

    public Instant getPasswordUpdatedAt() {
        return passwordUpdatedAt;
    }

    public UserStatus getStatus() {
        return status;
    }

    public Set<UserRole> getRoles() {
        return Set.copyOf(roles);
    }

    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public Instant getAccountLockedUntil() {
        return accountLockedUntil;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getLastLoginAt() {
        return lastLoginAt;
    }

    public Instant getLastActiveAt() {
        return lastActiveAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public static User create(EmailAddress email, PasswordHash passwordHash, Set<UserRole> roles, Instant now) {

        // Validate inputs
        Objects.requireNonNull(email, "Email is required");
        Objects.requireNonNull(passwordHash, "Password is required");

        // Set "USER" role if no roles are provided
        Set<UserRole> initialRoles = (roles == null || roles.isEmpty())
                ? Set.of(UserRole.USER)
                : roles;

        return new Builder()
                .id(UserId.newId())
                .email(email)
                .passwordHash(passwordHash)
                .roles(initialRoles)
                .status(UserStatus.ACTIVE)
                .createdAt(now)
                .updatedAt(now)
                .emailUpdatedAt(now)
                .passwordUpdatedAt(now)
                .build();
    }

    public void changeEmail(EmailAddress newEmail, Instant now) {
        ensureNotDeleted();
        if (this.email.equals(newEmail)) return;

        this.email = newEmail;
        this.emailVerified = false;
        this.emailUpdatedAt = now;
        touch(now);
    }

    public void markEmailVerified(Instant now) {
        ensureNotDeleted();
        if (emailVerified) return;

        this.emailVerified = true;
        this.emailUpdatedAt = now;
        touch(now);
    }

    public void changePassword(PasswordHash newPasswordHash, Instant now) {
        ensureNotDeleted();
        this.passwordHash = newPasswordHash;
        this.passwordUpdatedAt = now;
        resetFailedLogins();
        touch(now);
    }

    public void addRole(UserRole role, Instant now) {
        ensureNotDeleted();
        if (roles.add(role)) {
            touch(now);
        }
    }

    public void removeRole(UserRole role, Instant now) {
        ensureNotDeleted();
        if (roles.remove(role)) {
            touch(now);
        }
    }

    public boolean hasRole(UserRole role) {
        return roles.contains(role);
    }

    public void suspend(Instant now) {
        ensureNotDeleted();
        this.status = UserStatus.SUSPENDED;
        touch(now);
    }

    public void activate(Instant now) {
        if (isDeleted()) {
            throw new UserIsDeletedException("Cannot activate a deleted user");
        }
        this.status = UserStatus.ACTIVE;
        touch(now);
    }

    public void recordSuccessfulLogin(Instant now) {
        ensureNotDeleted();
        ensureNotLocked(now);

        this.failedLoginAttempts = 0;
        this.accountLockedUntil = null;
        this.lastLoginAt = now;
        this.lastActiveAt = now;
        touch(now);
    }

    public void recordFailedLogin(Instant now, Duration lockPolicy) {
        if (isLocked(now)) return;
        if (accountLockedUntil != null && now.isAfter(accountLockedUntil)) {
            this.failedLoginAttempts = 0;
        }
        this.failedLoginAttempts++;
        if (failedLoginAttempts >= 3) {
            this.accountLockedUntil = now.plus(lockPolicy);
        }
        touch(now);
    }

    public boolean isLocked(Instant now) {
        return accountLockedUntil != null && accountLockedUntil.isAfter(now);
    }

    public void delete(Instant now) {
        if (isDeleted()) return;

        this.status = UserStatus.DELETED;
        this.deletedAt = now;
        this.emailVerified = false;
        touch(now);
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }

    private void touch(Instant now) {
        this.updatedAt = now;
    }

    private void resetFailedLogins() {
        this.failedLoginAttempts = 0;
        this.accountLockedUntil = null;
    }

    private void ensureNotDeleted() {
        if (isDeleted()) {
            throw new UserIsDeletedException("User is deleted");
        }
    }

    private void ensureNotLocked(Instant now) {
        if (isLocked(now)) {
            throw new UserIsLockedException("Account is locked");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public static final class Builder {

        private UserId id;
        private EmailAddress email;
        private boolean emailVerified;
        private Instant emailUpdatedAt;

        private PasswordHash passwordHash;
        private Instant passwordUpdatedAt;

        private UserStatus status;
        private Set<UserRole> roles = EnumSet.noneOf(UserRole.class);

        private int failedLoginAttempts;
        private Instant accountLockedUntil;

        private Instant createdAt;
        private Instant updatedAt;
        private Instant lastLoginAt;
        private Instant lastActiveAt;
        private Instant deletedAt;

        public Builder id(UserId id) {
            this.id = id;
            return this;
        }

        public Builder email(EmailAddress email) {
            this.email = email;
            return this;
        }

        public Builder emailVerified(boolean emailVerified) {
            this.emailVerified = emailVerified;
            return this;
        }

        public Builder emailUpdatedAt(Instant emailUpdatedAt) {
            this.emailUpdatedAt = emailUpdatedAt;
            return this;
        }

        public Builder passwordHash(PasswordHash passwordHash) {
            this.passwordHash = passwordHash;
            return this;
        }

        public Builder passwordUpdatedAt(Instant passwordUpdatedAt) {
            this.passwordUpdatedAt = passwordUpdatedAt;
            return this;
        }

        public Builder status(UserStatus status) {
            this.status = status;
            return this;
        }

        public Builder roles(Set<UserRole> roles) {
            this.roles = EnumSet.copyOf(roles);
            return this;
        }

        public Builder addRole(UserRole role) {
            this.roles.add(role);
            return this;
        }

        public Builder failedLoginAttempts(int failedLoginAttempts) {
            this.failedLoginAttempts = failedLoginAttempts;
            return this;
        }

        public Builder accountLockedUntil(Instant accountLockedUntil) {
            this.accountLockedUntil = accountLockedUntil;
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder lastLoginAt(Instant lastLoginAt) {
            this.lastLoginAt = lastLoginAt;
            return this;
        }

        public Builder lastActiveAt(Instant lastActiveAt) {
            this.lastActiveAt = lastActiveAt;
            return this;
        }

        public Builder deletedAt(Instant deletedAt) {
            this.deletedAt = deletedAt;
            return this;
        }

        public User build() {
            Objects.requireNonNull(id, "User ID is required");
            Objects.requireNonNull(email, "Email is required");
            Objects.requireNonNull(status, "Status is required");
            return new User(this);
        }
    }


}
