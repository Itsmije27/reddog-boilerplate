package app;

public class AppController {
    private final AppModel model;
    private final AppView view;

    public AppController(AppModel model, AppView view) {
        this.model = model;
        this.view = view;

        view.setMessage(model.getMessage());
        view.setOnGreeting(this::greet);
    }

    private void greet() {
        model.setMessage("Hallo!");
        view.setMessage(model.getMessage());
    }
}
