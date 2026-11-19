package app;

public class AppController {
    private final AppModel model;
    private final AppView view;

    public AppController(AppModel model, AppView view) {
        this.model = model;
        this.view = view;

        view.setOnGreeting(this::greet);
        view.setOnWagTail(this::wagTail);
        view.setOnReset(this::reset);
        view.setOnToggleTheme(this::toggleTheme);
        view.setOnAbout(view::showAboutDialog);

        syncView();
    }

    private void greet() {
        model.setName(view.getNameInput());
        model.greet();
        syncView();
        view.playChibiBounce();
    }

    private void wagTail() {
        model.wagTail();
        syncView();
        view.playTailWag();
    }

    private void reset() {
        model.reset();
        view.clearNameInput();
        syncView();
    }

    private void toggleTheme() {
        model.toggleDarkMode();
        syncView();
    }

    private void syncView() {
        view.setMessage(model.getMessage());
        view.setSubtitle(model.getSubtitle());
        view.setGreetingCount(model.getGreetingCount());
        view.setChibiExcited(model.isExcited());
        view.setDarkMode(model.isDarkMode());
    }
}
