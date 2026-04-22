package controllers;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Window;
import javafx.util.Duration;

// Gestionnaire des animations de l'application
public class AnimationManager {

    // ===== FADE-IN — page apparait en douceur =====
    public static void fadeIn(Node node) {
        node.setOpacity(0);
        FadeTransition fade = new FadeTransition(Duration.millis(400), node);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }

    // ===== SLIDE-IN depuis la gauche =====
    public static void slideIn(Node node) {
        node.setOpacity(0);
        node.setTranslateX(-30);

        FadeTransition fade = new FadeTransition(Duration.millis(350), node);
        fade.setFromValue(0);
        fade.setToValue(1);

        TranslateTransition slide = new TranslateTransition(Duration.millis(350), node);
        slide.setFromX(-30);
        slide.setToX(0);
        slide.setInterpolator(Interpolator.EASE_OUT);

        ParallelTransition pt = new ParallelTransition(fade, slide);
        pt.play();
    }

    // ===== HOVER SCALE — bouton kattkbr mla hover =====
    public static void addHoverEffect(Node node) {
        node.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), node);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();
        });
        node.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(150), node);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });
    }

    // ===== SHAKE — error animation =====
    public static void shake(Node node) {
        TranslateTransition shake = new TranslateTransition(Duration.millis(60), node);
        shake.setByX(8);
        shake.setCycleCount(6);
        shake.setAutoReverse(true);
        shake.setOnFinished(e -> node.setTranslateX(0));
        shake.play();
    }

    // ===== NOTIFICATION POPUP =====
    public static void showNotification(Window owner, String message, boolean isSuccess) {
        // Créer le popup
        Popup popup = new Popup();

        // Créer le contenu
        Label label = new Label(message);
        label.setStyle(
                "-fx-background-color: " + (isSuccess ? "#2E7D32" : "#C62828") + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 14 24 14 24;" +
                        "-fx-background-radius: 10;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 3);"
        );

        VBox container = new VBox(label);
        container.setStyle("-fx-background-color: transparent;");
        popup.getContent().add(container);
        popup.setAutoHide(true);

        // Position — bas droite de la fenêtre
        double x = owner.getX() + owner.getWidth() - 320;
        double y = owner.getY() + owner.getHeight() - 100;
        popup.show(owner, x, y);

        // Animation entrée
        label.setOpacity(0);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), label);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        // Auto-hide après 3 secondes
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(e -> {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(400), label);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(ev -> popup.hide());
            fadeOut.play();
        });
        pause.play();
    }

    // ===== PULSE — animation pour stat cards =====
    public static void pulse(Node node) {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), node);
        st.setFromX(1.0);
        st.setFromY(1.0);
        st.setToX(1.08);
        st.setToY(1.08);
        st.setCycleCount(2);
        st.setAutoReverse(true);
        st.play();
    }
}