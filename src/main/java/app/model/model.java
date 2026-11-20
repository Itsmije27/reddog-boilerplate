package app.model;

import java.util.Objects;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class model {

    private final Stage stage;
    private final Label messageLabel = new Label();
    private final Label statusLabel = new Label();

    private final TextField usernameField = new TextField();
    private final PasswordField passwordField = new PasswordField();

    private final Button loginButton = new Button("Anmelden");
    private final Button registerButton = new Button("Registrieren");
    private final Button logoutButton = new Button("Abmelden");

    private final VBox authFormBox;

    public model(Stage stage) {
        this.stage = stage;

        messageLabel.getStyleClass().add("greeting-title");
        messageLabel.setWrapText(true);

        statusLabel.setWrapText(true);

        usernameField.setPromptText("Benutzername");
        usernameField.setMaxWidth(260);

        passwordField.setPromptText("Passwort");
        passwordField.setMaxWidth(260);

        loginButton.getStyleClass().add("greeting-button");
        registerButton.getStyleClass().add("greeting-button");
        logoutButton.getStyleClass().add("greeting-button");

        HBox actionButtons = new HBox(12, loginButton, registerButton);
        actionButtons.setAlignment(Pos.CENTER_LEFT);

        authFormBox = new VBox(12, usernameField, passwordField, actionButtons);
        authFormBox.setAlignment(Pos.CENTER_LEFT);

        logoutButton.setVisible(false);
        logoutButton.setManaged(false);

        VBox content = new VBox(18, messageLabel, statusLabel, authFormBox, logoutButton);
        content.setAlignment(Pos.CENTER_LEFT);
        content.getStyleClass().add("greeting-content");

        GridPane layout = new GridPane();
        ColumnConstraints artworkColumn = new ColumnConstraints();
        artworkColumn.setPercentWidth(38);
        ColumnConstraints contentColumn = new ColumnConstraints();
        contentColumn.setPercentWidth(62);
        layout.getColumnConstraints().addAll(artworkColumn, contentColumn);
        layout.add(createArtwork(), 0, 0);
        layout.add(content, 1, 0);
        RowConstraints row = new RowConstraints();
        row.setPercentHeight(100);
        layout.getRowConstraints().add(row);

        Scene scene = new Scene(layout, 760, 440);
        scene.getStylesheets().add(Objects.requireNonNull(
                model.class.getResource("greeting.css")).toExternalForm());

        stage.setTitle("Benutzer-Service – Login & Registrierung");
        stage.setMinWidth(580);
        stage.setMinHeight(340);
        stage.setScene(scene);
    }

    private static Pane createArtwork() {
        Pane artwork = new Pane();
        artwork.getStyleClass().add("greeting-artwork");
        artwork.setMouseTransparent(true);

        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(artwork.widthProperty());
        clip.heightProperty().bind(artwork.heightProperty());
        artwork.setClip(clip);

        Circle outerRing = new Circle(166);
        Circle innerRing = new Circle(125);
        Circle sun = new Circle(84);
        outerRing.getStyleClass().add("artwork-ring");
        innerRing.getStyleClass().add("artwork-ring");
        sun.getStyleClass().add("artwork-sun");
        for (Circle circle : new Circle[] {outerRing, innerRing, sun}) {
            circle.setManaged(false);
            circle.centerXProperty().bind(artwork.widthProperty().multiply(0.38));
            circle.centerYProperty().bind(artwork.heightProperty().multiply(0.57));
        }
        artwork.getChildren().addAll(outerRing, innerRing, sun);
        return artwork;
    }

    public void setMessage(String message) {
        messageLabel.setText(message);
    }

    public void setStatus(String status, boolean isError) {
        statusLabel.setText(status);
        statusLabel.setStyle(isError ? "-fx-text-fill: #d9534f;" : "-fx-text-fill: #2e7d32;");
    }

    public String getUsernameInput() {
        return usernameField.getText();
    }

    public String getPasswordInput() {
        return passwordField.getText();
    }

    public void clearInputs() {
        usernameField.clear();
        passwordField.clear();
    }

    public void setLoggedInState(boolean loggedIn) {
        authFormBox.setVisible(!loggedIn);
        authFormBox.setManaged(!loggedIn);

        logoutButton.setVisible(loggedIn);
        logoutButton.setManaged(loggedIn);
    }

    public void setOnLogin(Runnable action) {
        loginButton.setOnAction(event -> action.run());
    }

    public void setOnRegister(Runnable action) {
        registerButton.setOnAction(event -> action.run());
    }

    public void setOnLogout(Runnable action) {
        logoutButton.setOnAction(event -> action.run());
    }

    public void show() {
        stage.show();
    }
}