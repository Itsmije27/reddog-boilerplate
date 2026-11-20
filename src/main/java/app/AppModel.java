package app;


public class AppModel {

    private String message = "Willkommen!";
    private final UserService userService;

    public AppModel() {
        // TODO: DummyPasswordHasher durch die fertige Hash-Klasse des Kollegen austauschen
        PasswordHasher passwordHasher = new DummyPasswordHasher();

        // TODO: InMemoryUserRepository ggf. durch die echte Datenschicht austauschen
        UserRepository userRepository = new InMemoryUserRepository();

        this.userService = new UserService(userRepository, passwordHasher);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserService getUserService() {
        return userService;
    }

    /**
     * Registriert einen neuen Benutzer und aktualisiert die Statusmeldung.
     */
    public User registerUser(String username, String password) throws UserAlreadyExistsException {
        User createdUser = userService.register(username, password);
        setMessage("Registrierung erfolgreich! Willkommen, " + createdUser.username() + ".");
        return createdUser;
    }

    /**
     * Meldet einen Benutzer an und aktualisiert die Statusmeldung.
     */
    public User loginUser(String username, String password) throws AuthenticationException {
        User loggedInUser = userService.login(username, password);
        setMessage("Angemeldet als: " + loggedInUser.username());
        return loggedInUser;
    }

    /**
     * Meldet den aktuellen Benutzer ab und setzt die Meldung zurück.
     */
    public void logoutUser() {
        userService.logout();
        setMessage("Erfolgreich abgemeldet. Willkommen!");
    }
}