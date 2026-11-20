package app;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main {
    public static void main(String[] args) {
        Application.launch(JavaFxApplication.class, args);
    }

    public static class JavaFxApplication extends Application {
        @Override
        public void start(Stage stage) {
            AppModel model = new AppModel();
            AppView view = new AppView(stage);
            new AppController(model, view);
            view.show();
        }
    }
}