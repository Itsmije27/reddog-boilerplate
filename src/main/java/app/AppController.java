package app;


public class AppController {

    private final AppModel model;
    private final AppView view;

    public AppController(AppModel model, AppView view) {
        this.model = model;
        this.view = view;

        view.setMessage(model.getMessage());
        view.setOnRegister(this::handleRegister);
        view.setOnLogin(this::handleLogin);
        view.setOnLogout(this::handleLogout);
    }

    private void handleRegister() {
        String username = view.getUsernameInput();
        String password = view.getPasswordInput();

        try {
            model.registerUser(username, password);
            view.setMessage(model.getMessage());
            view.setStatus("Konto erfolgreich erstellt! Du kannst dich jetzt anmelden.", false);
            view.clearInputs();
        } catch (IllegalArgumentException | UserAlreadyExistsException e) {
            view.setStatus(e.getMessage(), true);
        }
    }

    private void handleLogin() {
        String username = view.getUsernameInput();
        String password = view.getPasswordInput();

        try {
            model.loginUser(username, password);
            view.setMessage(model.getMessage());
            view.setStatus("Erfolgreich eingeloggt.", false);
            view.clearInputs();
            view.setLoggedInState(true);
        } catch (IllegalArgumentException | AuthenticationException e) {
            view.setStatus(e.getMessage(), true);
        }
    }

    private void handleLogout() {
        model.logoutUser();
        view.setMessage(model.getMessage());
        view.setStatus("Du wurdest abgemeldet.", false);
        view.setLoggedInState(false);
    }
}