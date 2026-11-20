/**
 * 
 */
package app.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * 
 */
public class PasswordHasher {

	private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
	private static final int DEFAULT_ITERATIONS = 600_000;
	private static final int SALT_LENGTH_BYTES = 16;
	private static final int KEY_LENGTH_BITS = 256;
	private static final String DELIMITER = ":";
	
	private final SecureRandom secureRandom;
	private final int iterations;
	
	//Standardkonstruktor mit Default-wert
	public PasswordHasher() {
		this(DEFAULT_ITERATIONS);
	}
	
	//Konfigurierbarer Konstruktor
	public PasswordHasher(int iterations) {
		if (iterations <= 0) {
			throw new IllegalArgumentException("Iterationen müssen positiv sein!");
		}
		this.secureRandom = new SecureRandom();
		this.iterations = iterations;
	}

	/**
	 * Erzeugt einen PBKDF2-Passwort-Hash im Format {@code <iterations>:<base64Salt>:<base64DerivedKey>}.
	 */
	public String hash(String password) {
		if (password == null || password.isEmpty()) {
			throw new IllegalArgumentException("Passwort darf nicht leer sein!");
		}
		char[] chars = password.toCharArray();
		try {
			return hash(chars);
		} finally {
			// Kopie im char-Array sofort überschreiben, damit sie nicht im Heap verbleibt
			Arrays.fill(chars, '\0');
		}
	}

	/**
	 * Erzeugt einen PBKDF2-Passwort-Hash im Format {@code <iterations>:<base64Salt>:<base64DerivedKey>}.
	 */
	public String hash(char[] password) {
		if (password == null || password.length == 0) {
			throw new IllegalArgumentException("Passwort darf nicht leer sein!");
		}
		byte[] salt = generateSalt();
		byte[] derivedKey = deriveKey(password, salt, this.iterations, KEY_LENGTH_BITS);
		String encodedSalt = Base64.getEncoder().encodeToString(salt);
		String encodedKey = Base64.getEncoder().encodeToString(derivedKey);
		return String.join(DELIMITER, String.valueOf(this.iterations), encodedSalt, encodedKey);
	}

	/**
	 * Prüft ein eingegebenes Passwort gegen einen gespeicherten Hash-String.
	 */
	public boolean verify(String password, String storedHash) {
		if (password == null || password.isEmpty() || storedHash == null || storedHash.isEmpty()) {
			return false;
		}
		char[] chars = password.toCharArray();
		try {
			return verify(chars, storedHash);
		} finally {
			Arrays.fill(chars, '\0');
		}
	}

	/**
	 * Prüft ein eingegebenes Passwort gegen einen gespeicherten Hash-String.
	 */
	public boolean verify(char[] password, String storedHash) {
		if (password == null || password.length == 0 || storedHash == null || storedHash.isEmpty()) {
			return false;
		}
		// Limit -1 behält auch leere Endteile (z. B. "1000:abc:") zur sauberen Ablehnung bei
		String[] parts = storedHash.split(DELIMITER, -1);
		if (parts.length != 3) {
			return false;
		}

		final int storedIterations;
		final byte[] salt;
		final byte[] expectedKey;
		try {
			storedIterations = Integer.parseInt(parts[0]);
			salt = Base64.getDecoder().decode(parts[1]);
			expectedKey = Base64.getDecoder().decode(parts[2]);
		} catch (IllegalArgumentException e) {
			// Deckt sowohl NumberFormatException als auch ungültiges Base64 ab
			return false;
		}

		if (storedIterations <= 0
				|| salt.length == 0
				|| expectedKey.length == 0
				|| expectedKey.length > Integer.MAX_VALUE / 8) {
			return false;
		}

		byte[] actualKey = deriveKey(password, salt, storedIterations, expectedKey.length * 8);
		return MessageDigest.isEqual(expectedKey, actualKey);
	}

	private byte[] generateSalt() {
		byte[] salt = new byte[SALT_LENGTH_BYTES];
		secureRandom.nextBytes(salt);
		return salt;
	}

	private byte[] deriveKey(char[] password, byte[] salt, int iterations, int keyLengthBits) {
		PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLengthBits);
		try {
			SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
			return factory.generateSecret(spec).getEncoded();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new IllegalStateException("PBKDF2-Hashing fehlgeschlagen", e);
		} finally {
			spec.clearPassword();
		}
	}
}
