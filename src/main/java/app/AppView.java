package app;

import java.util.Objects;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;

public class AppView {
    private final Stage stage;
    private final Label messageLabel = new Label();
    private final Button greetingButton = new Button("Begrüßung anzeigen");

    public AppView(Stage stage) {
        this.stage = stage;

        messageLabel.getStyleClass().add("greeting-title");
        messageLabel.setWrapText(true);
        greetingButton.getStyleClass().add("greeting-button");

        VBox content = new VBox(26, messageLabel, greetingButton);
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

        Button darkModeButton = new Button("☾");
        darkModeButton.setAccessibleText("Darkmode einschalten");

        HBox kopfleiste = new HBox(darkModeButton);
        kopfleiste.setAlignment(Pos.CENTER_RIGHT);
        kopfleiste.setPadding(new Insets(6, 12, 6, 12));

        BorderPane fenster = new BorderPane();
        fenster.setTop(kopfleiste);
        fenster.setCenter(layout);

        darkModeButton.setOnAction(event -> {
            if (fenster.getStyleClass().contains("dark-mode")) {
                fenster.getStyleClass().remove("dark-mode");
                darkModeButton.setText("☾");
                darkModeButton.setAccessibleText("Darkmode einschalten");
            } else {
                fenster.getStyleClass().add("dark-mode");
                darkModeButton.setText("☀");
                darkModeButton.setAccessibleText("Darkmode ausschalten");
            }
        });
        
        Scene scene = new Scene(fenster, 760, 440);
        
        scene.getStylesheets().add(Objects.requireNonNull(
                AppView.class.getResource("greeting.css")).toExternalForm());

        stage.setTitle("Begrüßung");
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

    public void setOnGreeting(Runnable action) {
        greetingButton.setOnAction(event -> action.run());
    }

    public void show() {
        stage.show();
    }
}
