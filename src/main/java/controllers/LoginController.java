package controllers;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

// Contrôleur de la page de connexion
public class LoginController {

    @FXML private VBox loginBox;
    @FXML private TextField fieldUsername;
    @FXML private PasswordField fieldPassword;
    @FXML private Label labelError;
    @FXML private Button btnLogin;

    // Identifiants administrateur
    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASS = "admin123";

    @FXML
    public void initialize() {
        // Animation fade-in au démarrage
        FadeTransition fade = new FadeTransition(Duration.millis(1000), loginBox);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        labelError.setVisible(false);

        // Login avec touche Enter
        fieldPassword.setOnAction(e -> handleLogin());
        fieldUsername.setOnAction(e -> fieldPassword.requestFocus());
    }

    @FXML
    public void handleLogin() {
        String username = fieldUsername.getText();
        String password = fieldPassword.getText();

        // Debug — shof ash katkteb
        System.out.println("Username: '" + username + "'");
        System.out.println("Password: '" + password + "'");
        System.out.println("Expected: '" + ADMIN_USER + "' / '" + ADMIN_PASS + "'");

        if (username.isEmpty() || password.isEmpty()) {
            showError("⚠️ Veuillez remplir tous les champs.");
            return;
        }

        if (ADMIN_USER.equals(username) && ADMIN_PASS.equals(password)) {
            openMainApp();
        } else {
            showError("❌ Nom d'utilisateur ou mot de passe incorrect.");
            fieldPassword.clear();
            shakeEffect();
        }
    }
    @FXML
    public void handleClear() {
        fieldUsername.clear();
        fieldPassword.clear();
        labelError.setVisible(false);
        fieldUsername.requestFocus();
    }

    private void openMainApp() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/resources/main.fxml"));
            Scene scene = new Scene(loader.load(), 1050, 650);

            try {
                scene.getStylesheets().add(
                        getClass().getResource("/resources/style.css")
                                .toExternalForm());
            } catch (Exception e) {
                System.out.println("CSS non trouvé.");
            }

            Stage stage = (Stage) btnLogin.getScene().getWindow();
            stage.setTitle("🏥 Hospital Patient Manager");
            stage.setScene(scene);
            stage.setResizable(true);
            stage.setMinWidth(900);
            stage.setMinHeight(580);
            stage.centerOnScreen();

        } catch (Exception e) {
            showError("❌ Erreur ouverture application: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showError(String message) {
        labelError.setText(message);
        labelError.setVisible(true);

        // Animation fade pour l'erreur
        FadeTransition fade = new FadeTransition(Duration.millis(300), labelError);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }

    private void shakeEffect() {
        // Animation shake sur le formulaire
        javafx.animation.TranslateTransition shake =
                new javafx.animation.TranslateTransition(Duration.millis(80), loginBox);
        shake.setByX(10);
        shake.setCycleCount(6);
        shake.setAutoReverse(true);
        shake.play();
    }
}