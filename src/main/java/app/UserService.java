package app;

import java.util.Objects;
import java.util.Optional;

public class UserService {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private User currentUser;

    public UserService(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = Objects.requireNonNull(userRepository);
        this.passwordHasher = Objects.requireNonNull(passwordHasher);
    }

    public User register(String username, String plainPassword) throws UserAlreadyExistsException {
        validateInput(username, plainPassword);
        String trimmedUsername = username.trim();

        if (userRepository.existsByUsername(trimmedUsername)) {
            throw new UserAlreadyExistsException("Benutzername '" + trimmedUsername + "' ist bereits vergeben.");
        }

        String hashedPassword = passwordHasher.hash(plainPassword);
        User newUser = User.createNew(trimmedUsername, hashedPassword);
        userRepository.save(newUser);
        return newUser;
    }

    public User login(String username, String plainPassword) throws AuthenticationException {
        validateInput(username, plainPassword);
        String trimmedUsername = username.trim();

        User user = userRepository.findByUsername(trimmedUsername)
                .orElseThrow(() -> new AuthenticationException("Benutzername oder Passwort falsch."));

        if (!passwordHasher.verify(plainPassword, user.passwordHash())) {
            throw new AuthenticationException("Benutzername oder Passwort falsch.");
        }

        this.currentUser = user;
        return user;
    }

    public void logout() {
        this.currentUser = null;
    }

    public Optional<User> getCurrentUser() {
        return Optional.ofNullable(currentUser);
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    private void validateInput(String username, String plainPassword) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Bitte einen Benutzernamen eingeben.");
        }
        if (plainPassword == null || plainPassword.isBlank()) {
            throw new IllegalArgumentException("Bitte ein Passwort eingeben.");
        }
    }
}