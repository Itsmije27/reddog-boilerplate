package app;

/**
 * Schnittstelle für die Hash-Schicht deines Arbeitskollegen.
 */
public interface PasswordHasher {
    String hash(String plainPassword);
    boolean verify(String plainPassword, String hashedPassword);
}