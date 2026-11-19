package app;

import java.util.Objects;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.transform.Rotate;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AppView {
    private static final String STYLESHEET = Objects.requireNonNull(
            AppView.class.getResource("greeting.css")).toExternalForm();

    private final Stage stage;
    private final StackPane rootContainer = new StackPane();
    private final Label counterBadge = new Label();
    private final Label moodBadge = new Label("Wuff! ★");
    private final Label messageLabel = new Label();
    private final Label subtitleLabel = new Label();
    private final TextField nameField = new TextField();
    private final Button greetingButton = new Button("Begrüßen!");
    private final Button wagButton = new Button("Wedeln!");
    private final Button resetButton = new Button("Zurücksetzen");
    private final Button themeButton = new Button("Nacht");
    private final Button aboutButton = new Button("?");

    private final Group chibiCharacter = new Group();
    private final Group tailGroup = new Group();
    private final Rotate tailRotate = new Rotate(0, 154, 193);
    private final Group normalEyesGroup = new Group();
    private final Group happyEyesGroup = new Group();
    private final Group heartBubbleGroup = new Group();

    private Timeline tailWagAnimation;
    private boolean darkMode = false;
    private boolean excited = false;

    public AppView(Stage stage) {
        this.stage = stage;

        rootContainer.getStyleClass().add("app-shell");

        GridPane layout = new GridPane();
        layout.getStyleClass().add("main-grid");
        layout.setHgap(24);

        ColumnConstraints artworkColumn = new ColumnConstraints();
        artworkColumn.setPercentWidth(42);
        ColumnConstraints contentColumn = new ColumnConstraints();
        contentColumn.setPercentWidth(58);
        layout.getColumnConstraints().addAll(artworkColumn, contentColumn);

        RowConstraints row = new RowConstraints();
        row.setPercentHeight(100);
        layout.getRowConstraints().add(row);

        layout.add(createArtworkCard(), 0, 0);
        layout.add(createContentColumn(), 1, 0);

        rootContainer.getChildren().add(layout);

        Scene scene = new Scene(rootContainer, 880, 540);
        scene.getStylesheets().add(STYLESHEET);

        stage.setTitle("RedDog Chibi — Begrüßung");
        stage.setMinWidth(700);
        stage.setMinHeight(460);
        stage.setScene(scene);
    }

    private VBox createArtworkCard() {
        VBox card = new VBox(14);
        card.setAlignment(Pos.CENTER);
        card.getStyleClass().add("artwork-card");

        StackPane stagePane = new StackPane();
        stagePane.getStyleClass().add("artwork-stage");
        VBox.setVgrow(stagePane, Priority.ALWAYS);

        Group backdrop = createPastelBackdrop();
        buildChibiPuppy();

        stagePane.getChildren().addAll(backdrop, chibiCharacter);

        moodBadge.getStyleClass().add("mood-pill");
        card.getChildren().addAll(stagePane, moodBadge);
        return card;
    }

    private Group createPastelBackdrop() {
        Group bg = new Group();

        Circle bubbleTopLeft = new Circle(42, 46, 30);
        bubbleTopLeft.getStyleClass().add("artwork-bubble-pink");
        Circle bubbleBottomRight = new Circle(204, 212, 34);
        bubbleBottomRight.getStyleClass().add("artwork-bubble-mint");
        Circle bubbleTopRight = new Circle(206, 56, 18);
        bubbleTopRight.getStyleClass().add("artwork-bubble-yellow");
        Circle bubbleBottomLeft = new Circle(34, 198, 16);
        bubbleBottomLeft.getStyleClass().add("artwork-bubble-lavender");

        SVGPath sparkle1 = createSparkle(36, 98, 1.0, "sparkle-yellow");
        SVGPath sparkle2 = createSparkle(212, 128, 0.85, "sparkle-pink");
        SVGPath sparkle3 = createSparkle(54, 232, 0.75, "sparkle-mint");
        SVGPath sparkle4 = createSparkle(184, 30, 0.7, "sparkle-lavender");

        bg.getChildren().addAll(
                bubbleTopLeft, bubbleBottomRight, bubbleTopRight, bubbleBottomLeft,
                sparkle1, sparkle2, sparkle3, sparkle4
        );
        return bg;
    }

    private void buildChibiPuppy() {
        // Soft ground shadow
        Ellipse shadow = new Ellipse(120, 224, 62, 13);
        shadow.getStyleClass().add("chibi-shadow");

        // Fluffy wagging tail (pivots from base at puppy body)
        Ellipse tail = new Ellipse(170, 182, 24, 14);
        tail.setRotate(-32);
        tail.getStyleClass().addAll("chibi-fur", "chibi-outline");
        Circle tailTip = new Circle(184, 173, 8);
        tailTip.getStyleClass().add("chibi-cream");
        tailGroup.getChildren().setAll(tail, tailTip);
        tailGroup.getTransforms().setAll(tailRotate);

        // Chubby chibi body
        Ellipse body = new Ellipse(120, 190, 42, 33);
        body.getStyleClass().addAll("chibi-fur", "chibi-outline");
        Ellipse tummy = new Ellipse(120, 195, 25, 21);
        tummy.getStyleClass().add("chibi-cream");

        // Cute pastel collar & golden bell
        Rectangle collar = new Rectangle(87, 159, 66, 14);
        collar.setArcWidth(14);
        collar.setArcHeight(14);
        collar.getStyleClass().addAll("chibi-collar", "chibi-outline");

        Circle bell = new Circle(120, 174, 9.5);
        bell.getStyleClass().addAll("chibi-bell", "chibi-outline");
        Circle bellShine = new Circle(117, 171, 2.5);
        bellShine.getStyleClass().add("chibi-sparkle-dot");

        // Tiny round chibi paws
        Ellipse leftPaw = new Ellipse(94, 215, 14.5, 10.5);
        leftPaw.getStyleClass().addAll("chibi-cream", "chibi-outline");
        Ellipse rightPaw = new Ellipse(146, 215, 14.5, 10.5);
        rightPaw.getStyleClass().addAll("chibi-cream", "chibi-outline");

        // Soft oversized chibi puppy ears
        Ellipse leftEarOuter = new Ellipse(68, 66, 22, 34);
        leftEarOuter.setRotate(-26);
        leftEarOuter.getStyleClass().addAll("chibi-fur", "chibi-outline");
        Ellipse leftEarInner = new Ellipse(70, 68, 11, 20);
        leftEarInner.setRotate(-26);
        leftEarInner.getStyleClass().add("chibi-inner-ear");

        Ellipse rightEarOuter = new Ellipse(172, 66, 22, 34);
        rightEarOuter.setRotate(26);
        rightEarOuter.getStyleClass().addAll("chibi-fur", "chibi-outline");
        Ellipse rightEarInner = new Ellipse(170, 68, 11, 20);
        rightEarInner.setRotate(26);
        rightEarInner.getStyleClass().add("chibi-inner-ear");

        // Big round chibi head + cream Shiba cheek mask
        Ellipse headFill = new Ellipse(120, 114, 68, 56);
        headFill.getStyleClass().add("chibi-fur");

        Ellipse leftCheek = new Ellipse(94, 134, 34, 24);
        leftCheek.getStyleClass().add("chibi-cream");
        Ellipse rightCheek = new Ellipse(146, 134, 34, 24);
        rightCheek.getStyleClass().add("chibi-cream");
        Ellipse muzzleBridge = new Ellipse(120, 129, 25, 20);
        muzzleBridge.getStyleClass().add("chibi-cream");

        // Clip cheek mask neatly to the head silhouette
        Ellipse headClip = new Ellipse(120, 114, 67, 55);
        Group cheekMask = new Group(leftCheek, rightCheek, muzzleBridge);
        cheekMask.setClip(headClip);

        // Crisp comic outline around the head
        Ellipse headOutline = new Ellipse(120, 114, 68, 56);
        headOutline.setFill(Color.TRANSPARENT);
        headOutline.getStyleClass().add("chibi-outline");

        // Classic cute Shiba eyebrow dots
        Ellipse leftBrow = new Ellipse(94, 91, 7.5, 4.5);
        leftBrow.setRotate(-12);
        leftBrow.getStyleClass().add("chibi-cream");
        Ellipse rightBrow = new Ellipse(146, 91, 7.5, 4.5);
        rightBrow.setRotate(12);
        rightBrow.getStyleClass().add("chibi-cream");

        // Rosy blush ovals
        Ellipse leftBlush = new Ellipse(77, 128, 12.5, 7);
        leftBlush.getStyleClass().add("chibi-blush");
        Ellipse rightBlush = new Ellipse(163, 128, 12.5, 7);
        rightBlush.getStyleClass().add("chibi-blush");

        // Normal sparkly anime eyes
        Ellipse leftEye = new Ellipse(93, 112, 10, 12);
        leftEye.getStyleClass().add("chibi-eye");
        Circle leftEyeShineBig = new Circle(89, 107, 4.2);
        leftEyeShineBig.getStyleClass().add("chibi-sparkle-dot");
        Circle leftEyeShineSmall = new Circle(96, 116, 2.0);
        leftEyeShineSmall.getStyleClass().add("chibi-sparkle-dot");

        Ellipse rightEye = new Ellipse(147, 112, 10, 12);
        rightEye.getStyleClass().add("chibi-eye");
        Circle rightEyeShineBig = new Circle(143, 107, 4.2);
        rightEyeShineBig.getStyleClass().add("chibi-sparkle-dot");
        Circle rightEyeShineSmall = new Circle(150, 116, 2.0);
        rightEyeShineSmall.getStyleClass().add("chibi-sparkle-dot");

        normalEyesGroup.getChildren().addAll(
                leftEye, leftEyeShineBig, leftEyeShineSmall,
                rightEye, rightEyeShineBig, rightEyeShineSmall
        );

        // Excited happy "^ ^" eyes
        Arc leftHappyEye = createComicArc(93, 116, 11, 10, 18, 144, "chibi-happy-eye");
        Arc rightHappyEye = createComicArc(147, 116, 11, 10, 18, 144, "chibi-happy-eye");
        happyEyesGroup.getChildren().addAll(leftHappyEye, rightHappyEye);
        happyEyesGroup.setVisible(false);

        // Button nose & ":3" smile
        Ellipse nose = new Ellipse(120, 124, 6.8, 4.6);
        nose.getStyleClass().add("chibi-eye");
        Circle noseShine = new Circle(118, 122.4, 1.5);
        noseShine.getStyleClass().add("chibi-sparkle-dot");

        Arc leftMouth = createComicArc(113, 130, 7, 6, 195, 160, "chibi-mouth");
        Arc rightMouth = createComicArc(127, 130, 7, 6, 185, 160, "chibi-mouth");

        // Floating comic heart bubble when greeted
        SVGPath heart = new SVGPath();
        heart.setContent("M 0 6 C 0 -2, -14 -2, -14 8 C -14 16, 0 24, 0 26 C 0 24, 14 16, 14 8 C 14 -2, 0 -2, 0 6 Z");
        heart.setTranslateX(188);
        heart.setTranslateY(30);
        heart.setRotate(14);
        heart.getStyleClass().addAll("chibi-heart", "chibi-outline-thin");
        heartBubbleGroup.getChildren().add(heart);
        heartBubbleGroup.setVisible(false);

        chibiCharacter.getChildren().addAll(
                shadow,
                tailGroup,
                body, tummy,
                collar, bell, bellShine,
                leftPaw, rightPaw,
                leftEarOuter, leftEarInner,
                rightEarOuter, rightEarInner,
                headFill, cheekMask, headOutline,
                leftBrow, rightBrow,
                leftBlush, rightBlush,
                normalEyesGroup, happyEyesGroup,
                nose, noseShine,
                leftMouth, rightMouth,
                heartBubbleGroup
        );
    }

    private VBox createContentColumn() {
        VBox column = new VBox(18);
        column.setAlignment(Pos.CENTER_LEFT);
        column.getStyleClass().add("content-column");

        // Top utility bar with round pills & circular buttons
        counterBadge.getStyleClass().add("counter-pill");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        themeButton.getStyleClass().add("pill-utility-button");
        themeButton.setGraphic(createThemeDotGraphic(false));
        themeButton.setGraphicTextGap(8);

        aboutButton.getStyleClass().add("circle-icon-button");
        aboutButton.setTooltip(new Tooltip("Über diese App"));

        HBox topBar = new HBox(10, counterBadge, spacer, themeButton, aboutButton);
        topBar.setAlignment(Pos.CENTER_LEFT);

        // Comic speech-bubble message card
        messageLabel.getStyleClass().add("greeting-title");
        messageLabel.setWrapText(true);

        subtitleLabel.getStyleClass().add("greeting-subtitle");
        subtitleLabel.setWrapText(true);

        VBox speechCard = new VBox(10, messageLabel, subtitleLabel);
        speechCard.getStyleClass().add("speech-card");

        // Rounded pill name input
        Label inputCaption = new Label("Dein Name");
        inputCaption.getStyleClass().add("input-caption");

        nameField.setPromptText("z. B. Mia, Yuki oder Leo ...");
        nameField.getStyleClass().add("kawaii-input");

        VBox inputBox = new VBox(6, inputCaption, nameField);

        // Chunky bubbly pill action buttons
        greetingButton.getStyleClass().add("greeting-button");
        greetingButton.setGraphic(createRoundBadgeGraphic("♥", "button-badge-dot-primary"));
        greetingButton.setGraphicTextGap(10);

        wagButton.getStyleClass().add("wag-button");
        wagButton.setGraphic(createRoundBadgeGraphic("♪", "button-badge-dot-blue"));
        wagButton.setGraphicTextGap(8);

        resetButton.getStyleClass().add("reset-button");
        resetButton.setGraphic(createRoundBadgeGraphic("↺", "button-badge-dot-secondary"));
        resetButton.setGraphicTextGap(8);

        FlowPane buttonRow = new FlowPane(12, 12, greetingButton, wagButton, resetButton);
        buttonRow.setAlignment(Pos.CENTER_LEFT);

        column.getChildren().addAll(topBar, speechCard, inputBox, buttonRow);
        return column;
    }

    private static SVGPath createSparkle(double x, double y, double scale, String styleClass) {
        SVGPath sparkle = new SVGPath();
        sparkle.setContent("M 0 -12 Q 0 0 12 0 Q 0 0 0 12 Q 0 0 -12 0 Q 0 0 0 -12 Z");
        sparkle.setTranslateX(x);
        sparkle.setTranslateY(y);
        sparkle.setScaleX(scale);
        sparkle.setScaleY(scale);
        sparkle.getStyleClass().addAll(styleClass, "chibi-outline-thin");
        return sparkle;
    }

    private static Arc createComicArc(double cx, double cy, double rx, double ry,
            double startAngle, double length, String styleClass) {
        Arc arc = new Arc(cx, cy, rx, ry, startAngle, length);
        arc.setType(ArcType.OPEN);
        arc.setFill(Color.TRANSPARENT);
        arc.setStrokeLineCap(StrokeLineCap.ROUND);
        arc.setStrokeLineJoin(StrokeLineJoin.ROUND);
        arc.getStyleClass().add(styleClass);
        return arc;
    }

    private static Node createRoundBadgeGraphic(String symbol, String dotClass) {
        StackPane badge = new StackPane();
        badge.getStyleClass().add(dotClass);
        Label icon = new Label(symbol);
        icon.getStyleClass().add("button-badge-symbol");
        badge.getChildren().add(icon);
        return badge;
    }

    private static Node createThemeDotGraphic(boolean isDark) {
        StackPane dot = new StackPane();
        dot.getStyleClass().add("theme-dot");
        Label icon = new Label(isDark ? "☀" : "☾");
        icon.getStyleClass().add("theme-dot-symbol");
        dot.getChildren().add(icon);
        return dot;
    }

    public String getNameInput() {
        return nameField.getText();
    }

    public void clearNameInput() {
        nameField.clear();
    }

    public void setMessage(String message) {
        messageLabel.setText(message);
    }

    public void setSubtitle(String subtitle) {
        subtitleLabel.setText(subtitle);
    }

    public void setGreetingCount(int count) {
        counterBadge.setText(count == 1 ? "★ 1× begrüßt" : "★ " + count + "× begrüßt");
    }

    public void setChibiExcited(boolean excited) {
        this.excited = excited;
        normalEyesGroup.setVisible(!excited);
        happyEyesGroup.setVisible(excited);
        heartBubbleGroup.setVisible(excited);
        updateMoodBadge();
    }

    public void setDarkMode(boolean darkMode) {
        this.darkMode = darkMode;
        if (darkMode) {
            if (!rootContainer.getStyleClass().contains("dark-theme")) {
                rootContainer.getStyleClass().add("dark-theme");
            }
            themeButton.setText("Tag");
        } else {
            rootContainer.getStyleClass().remove("dark-theme");
            themeButton.setText("Nacht");
        }
        themeButton.setGraphic(createThemeDotGraphic(darkMode));
        updateMoodBadge();
    }

    private void updateMoodBadge() {
        if (excited) {
            moodBadge.setText(darkMode ? "Sternen-Wuff! ♥" : "Juhu! Hallo! ♥");
        } else {
            moodBadge.setText(darkMode ? "Sternennacht ☾" : "Wuff! ★");
        }
    }

    public void playChibiBounce() {
        TranslateTransition hop = new TranslateTransition(Duration.millis(150), chibiCharacter);
        hop.setFromY(0);
        hop.setToY(-14);
        hop.setAutoReverse(true);
        hop.setCycleCount(2);
        hop.setInterpolator(Interpolator.EASE_OUT);

        ScaleTransition squish = new ScaleTransition(Duration.millis(150), chibiCharacter);
        squish.setFromX(1.0);
        squish.setFromY(1.0);
        squish.setToX(1.06);
        squish.setToY(0.95);
        squish.setAutoReverse(true);
        squish.setCycleCount(2);

        new ParallelTransition(hop, squish).play();
    }

    public void playTailWag() {
        if (tailWagAnimation != null) {
            tailWagAnimation.stop();
        }
        tailRotate.setAngle(0);
        tailWagAnimation = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(tailRotate.angleProperty(), 0)),
                new KeyFrame(Duration.millis(90), new KeyValue(tailRotate.angleProperty(), -26, Interpolator.EASE_BOTH)),
                new KeyFrame(Duration.millis(190), new KeyValue(tailRotate.angleProperty(), 24, Interpolator.EASE_BOTH)),
                new KeyFrame(Duration.millis(290), new KeyValue(tailRotate.angleProperty(), -24, Interpolator.EASE_BOTH)),
                new KeyFrame(Duration.millis(390), new KeyValue(tailRotate.angleProperty(), 22, Interpolator.EASE_BOTH)),
                new KeyFrame(Duration.millis(490), new KeyValue(tailRotate.angleProperty(), -18, Interpolator.EASE_BOTH)),
                new KeyFrame(Duration.millis(590), new KeyValue(tailRotate.angleProperty(), 14, Interpolator.EASE_BOTH)),
                new KeyFrame(Duration.millis(700), new KeyValue(tailRotate.angleProperty(), 0, Interpolator.EASE_OUT))
        );
        tailWagAnimation.play();
    }

    public void showAboutDialog() {
        Stage dialog = new Stage();
        dialog.initOwner(stage);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Über RedDog Chibi");
        dialog.setResizable(false);

        Label badge = new Label("♥ Über diese App");
        badge.getStyleClass().add("counter-pill");

        Label title = new Label("RedDog Chibi");
        title.getStyleClass().add("about-title");

        Label body = new Label(
                "Eine niedliche JavaFX-Begrüßungs-App im Kawaii-Comic-Stil.\n\n"
                        + "• Persönliche Tageszeit-Begrüßung\n"
                        + "• Erdbeermilch- & Sternennacht-Design\n"
                        + "• Läuft komplett lokal — ohne Cookies & ohne Tracking");
        body.setWrapText(true);
        body.getStyleClass().add("about-body");

        Button closeButton = new Button("Schließen");
        closeButton.getStyleClass().add("greeting-button");
        closeButton.setGraphic(createRoundBadgeGraphic("★", "button-badge-dot-primary"));
        closeButton.setGraphicTextGap(8);
        closeButton.setOnAction(event -> dialog.close());

        VBox card = new VBox(16, badge, title, body, closeButton);
        card.setAlignment(Pos.CENTER_LEFT);
        card.getStyleClass().add("about-card");

        StackPane shell = new StackPane(card);
        shell.setPadding(new Insets(20));
        shell.getStyleClass().add("app-shell");
        if (darkMode) {
            shell.getStyleClass().add("dark-theme");
        }

        Scene dialogScene = new Scene(shell, 430, 315);
        dialogScene.getStylesheets().add(STYLESHEET);

        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }

    public void setOnGreeting(Runnable action) {
        greetingButton.setOnAction(event -> action.run());
        nameField.setOnAction(event -> action.run());
    }

    public void setOnWagTail(Runnable action) {
        wagButton.setOnAction(event -> action.run());
    }

    public void setOnReset(Runnable action) {
        resetButton.setOnAction(event -> action.run());
    }

    public void setOnToggleTheme(Runnable action) {
        themeButton.setOnAction(event -> action.run());
    }

    public void setOnAbout(Runnable action) {
        aboutButton.setOnAction(event -> action.run());
    }

    public void show() {
        stage.show();
    }
}
