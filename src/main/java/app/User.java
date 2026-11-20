package app;

import java.util.Objects;
import java.util.UUID;

public record User(UUID id, String username, String passwordHash) {

    public User {
        Objects.requireNonNull(id, "User-ID darf nicht null sein.");
        Objects.requireNonNull(username, "Benutzername darf nicht null sein.");
        Objects.requireNonNull(passwordHash, "Passwort-Hash darf nicht null sein.");
    }

    public static User createNew(String username, String passwordHash) {
        return new User(UUID.randomUUID(), username, passwordHash);
    }
}