package app;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppController {
    private final AppModel model;
    private final AppView view;
    private final WeatherService weatherService = new WeatherService();
    private Stage notificationStage;
    private NotificationView notificationView;

    public AppController(AppModel model, AppView view) {
        this.model = model;
        this.view = view;

        view.setMessage(model.getMessage());
        view.setOnGreeting(this::greet);
        view.setOnOpenNotifications(this::openNotifications);

        loadWeather();
    }

    private void loadWeather() {
        // Netzwerkabruf im Hintergrund, damit die Oberfläche nicht einfriert
        Thread thread = new Thread(() -> {
            String weather = weatherService.getWeather();
            Platform.runLater(() -> view.setWeather(weather));
        }, "weather-loader");
        thread.setDaemon(true);
        thread.start();
    }

    private void greet() {
        model.setMessage("Hallo!");
        view.setMessage(model.getMessage());
    }

    private void openNotifications() {
        if (notificationStage == null) {
            notificationView = new NotificationView();
            notificationStage = new Stage();
            notificationStage.setTitle("Benachrichtigungen - Team 3");
            notificationStage.initOwner(view.getStage());
            notificationStage.setScene(new Scene(notificationView, 480, 620));
            notificationStage.setMinWidth(400);
            notificationStage.setMinHeight(450);
        }

        if (!notificationStage.isShowing()) {
            notificationStage.show();
        } else {
            notificationStage.toFront();
        }
    }
}