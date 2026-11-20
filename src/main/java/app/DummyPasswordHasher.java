package app;

/**
 * Platzhalter-Klasse für die Hash-Schicht, bis dein Kollege fertig ist.
 */
// TODO: Später durch die echte Hash-Klasse des Kollegen ersetzen.
public class DummyPasswordHasher implements PasswordHasher {

    private static final String DUMMY_PREFIX = "HASH_";

    @Override
    public String hash(String plainPassword) {
        if (plainPassword == null) {
            throw new IllegalArgumentException("Passwort darf nicht null sein.");
        }
        return DUMMY_PREFIX + plainPassword;
    }

    @Override
    public boolean verify(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        return hashedPassword.equals(hash(plainPassword));
    }
}