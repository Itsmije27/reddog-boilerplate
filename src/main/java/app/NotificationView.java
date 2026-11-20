package app;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class NotificationView extends BorderPane {

    public static class NotificationItem {
        private final String titel;
        private final String nachricht;
        private final String zeitstempel;
        private boolean gelesen;

        public NotificationItem(String titel, String nachricht, String zeitstempel, boolean gelesen) {
            this.titel = titel;
            this.nachricht = nachricht;
            this.zeitstempel = zeitstempel;
            this.gelesen = gelesen;
        }

        public String getTitel() { return titel; }
        public String getNachricht() { return nachricht; }
        public String getZeitstempel() { return zeitstempel; }
        public boolean isGelesen() { return gelesen; }
        public void setGelesen(boolean gelesen) { this.gelesen = gelesen; }
    }

    private final ObservableList<NotificationItem> notifications = FXCollections.observableArrayList();
    private final VBox cardContainer;
    private final Label unreadBadge;
    private boolean showOnlyUnread = false;

    public NotificationView() {
        this.getStyleClass().add("notification-root");
        this.setPadding(new Insets(20));

        var cssUrl = getClass().getResource("/app/notifications.css");
        if (cssUrl != null) {
            this.getStylesheets().add(cssUrl.toExternalForm());
        }

        // Beispieldaten
        notifications.addAll(
            new NotificationItem("Neuer Termin", "Projekt-Meeting heute um 14:30 Uhr.", "10:15", false),
            new NotificationItem("System-Update", "Alle Module wurden erfolgreich synchronisiert.", "09:40", false),
            new NotificationItem("Willkommen", "Benachrichtigungs-Center erfolgreich gestartet.", "Gestern", true)
        );

        // --- TOP: Header & Filter ---
        VBox topBox = new VBox(12);
        topBox.setPadding(new Insets(0, 0, 15, 0));

        HBox titleRow = new HBox(10);
        titleRow.setAlignment(Pos.CENTER_LEFT);
        Label titleLabel = new Label("Benachrichtigungen");
        titleLabel.getStyleClass().add("notification-header-title");

        unreadBadge = new Label("0");
        unreadBadge.getStyleClass().add("notification-badge");
        titleRow.getChildren().addAll(titleLabel, unreadBadge);

        HBox actionRow = new HBox(10);
        actionRow.setAlignment(Pos.CENTER_LEFT);

        Button filterBtn = new Button("Nur ungelesene");
        filterBtn.getStyleClass().add("notification-action-btn");
        filterBtn.setOnAction(e -> {
            showOnlyUnread = !showOnlyUnread;
            filterBtn.setText(showOnlyUnread ? "Alle anzeigen" : "Nur ungelesene");
            refreshList();
        });

        Button markAllReadBtn = new Button("Alle gelesen");
        markAllReadBtn.getStyleClass().add("notification-action-btn");
        markAllReadBtn.setOnAction(e -> {
            notifications.forEach(n -> n.setGelesen(true));
            refreshList();
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button testAddBtn = new Button("+ Test-Meldung");
        testAddBtn.getStyleClass().add("notification-primary-btn");
        testAddBtn.setOnAction(e -> addNotification(
            "Neue Meldung",
            "Dies ist eine automatisch generierte Test-Benachrichtigung."
        ));

        actionRow.getChildren().addAll(filterBtn, markAllReadBtn, spacer, testAddBtn);
        topBox.getChildren().addAll(titleRow, actionRow);
        this.setTop(topBox);

        // --- CENTER: Liste ---
        cardContainer = new VBox(10);
        cardContainer.setPadding(new Insets(5));

        ScrollPane scrollPane = new ScrollPane(cardContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("notification-scroll");
        this.setCenter(scrollPane);

        refreshList();
    }

    public void addNotification(String titel, String nachricht) {
        String now = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        notifications.add(0, new NotificationItem(titel, nachricht, now, false));
        refreshList();
    }

    private void refreshList() {
        cardContainer.getChildren().clear();

        long unreadCount = notifications.stream().filter(n -> !n.isGelesen()).count();
        unreadBadge.setText(unreadCount + " neu");

        for (NotificationItem item : notifications) {
            if (showOnlyUnread && item.isGelesen()) {
                continue;
            }
            cardContainer.getChildren().add(createNotificationCard(item));
        }

        if (cardContainer.getChildren().isEmpty()) {
            Label emptyLabel = new Label("Keine Benachrichtigungen vorhanden.");
            emptyLabel.getStyleClass().add("notification-card-msg");
            cardContainer.getChildren().add(emptyLabel);
        }
    }

    private VBox createNotificationCard(NotificationItem item) {
        VBox card = new VBox(6);
        card.getStyleClass().add(item.isGelesen() ? "notification-card" : "notification-card-unread");

        HBox topRow = new HBox(10);
        topRow.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label((item.isGelesen() ? "" : "● ") + item.getTitel());
        titleLabel.getStyleClass().add("notification-card-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label timeLabel = new Label(item.getZeitstempel());
        timeLabel.getStyleClass().add("notification-card-time");

        topRow.getChildren().addAll(titleLabel, spacer, timeLabel);

        Label msgLabel = new Label(item.getNachricht());
        msgLabel.setWrapText(true);
        msgLabel.getStyleClass().add("notification-card-msg");

        HBox bottomRow = new HBox(8);
        bottomRow.setAlignment(Pos.CENTER_RIGHT);

        if (!item.isGelesen()) {
            Button readBtn = new Button("✓ Als gelesen markieren");
            readBtn.getStyleClass().add("notification-icon-btn");
            readBtn.setOnAction(e -> {
                item.setGelesen(true);
                refreshList();
            });
            bottomRow.getChildren().add(readBtn);
        }

        Button deleteBtn = new Button("✕ Entfernen");
        deleteBtn.getStyleClass().add("notification-icon-btn");
        deleteBtn.setOnAction(e -> {
            notifications.remove(item);
            refreshList();
        });
        bottomRow.getChildren().add(deleteBtn);

        card.getChildren().addAll(topRow, msgLabel, bottomRow);
        return card;
    }

    // Eigenständiger Launcher zum schnellen Testen der UI
    public static class PreviewApp extends Application {
        @Override
        public void start(Stage primaryStage) {
            Scene scene = new Scene(new NotificationView(), 480, 620);
            primaryStage.setTitle("Benachrichtigungen - Team 3");
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }

    public static void main(String[] args) {
        Application.launch(PreviewApp.class, args);
    }
}
