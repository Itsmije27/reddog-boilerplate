package app;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AppView {
    private final Stage stage;
    private final Label messageLabel = new Label();
    private final Button greetingButton = new Button("Klick mich");

    public AppView(Stage stage) {
        this.stage = stage;

        VBox content = new VBox(12, messageLabel, greetingButton);
        content.setAlignment(Pos.CENTER);

        stage.setTitle("Java Boilerplate");
        stage.setScene(new Scene(content));
        stage.setWidth(600);
        stage.setHeight(400);
    }

    public void setMessage(String message) {
        messageLabel.setText(message);
    }

    public void setOnGreeting(Runnable action) {
        greetingButton.setOnAction(event -> action.run());
    }

    public void show() {
        stage.show();
    }
}
