package controllers;

import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;

// Gestionnaire du thème dark/light
public class ThemeManager {

    // État actuel du thème
    private static boolean isDark = false;

    public static boolean isDark() { return isDark; }

    // Basculer entre dark et light
    public static void toggle(Scene scene) {
        isDark = !isDark;
        applyTheme(scene);
    }

    // Appliquer le thème à la scène
    public static void applyTheme(Scene scene) {
        if (scene == null) return;

        javafx.scene.Node root = scene.getRoot();

        if (isDark) {
            applyDark(root);
        } else {
            applyLight(root);
        }
    }

    // Appliquer le thème sombre
    private static void applyDark(javafx.scene.Node node) {
        if (node instanceof BorderPane) {
            node.setStyle("-fx-background-color: #121212;");
        }
        if (node instanceof VBox) {
            String styleClass = node.getStyleClass().toString();
            if (styleClass.contains("sidebar")) {
                node.setStyle("-fx-background-color: #1F1F1F;");
            } else if (styleClass.contains("sidebar-header")) {
                node.setStyle("-fx-background-color: #141414;");
            } else if (styleClass.contains("main-content")) {
                node.setStyle("-fx-background-color: #121212;");
            } else if (styleClass.contains("form-card")) {
                node.setStyle("-fx-background-color: #1E1E1E; " +
                        "-fx-background-radius: 12; " +
                        "-fx-padding: 20; " +
                        "-fx-effect: dropshadow(gaussian,rgba(0,0,0,0.4),8,0,0,2);");
            } else if (styleClass.contains("table-card")) {
                node.setStyle("-fx-background-color: #1E1E1E; " +
                        "-fx-background-radius: 12; " +
                        "-fx-padding: 20; " +
                        "-fx-effect: dropshadow(gaussian,rgba(0,0,0,0.4),8,0,0,2);");
            } else if (styleClass.contains("stat-card")) {
                node.setStyle("-fx-background-color: #1E1E1E; " +
                        "-fx-background-radius: 12; " +
                        "-fx-padding: 16; " +
                        "-fx-effect: dropshadow(gaussian,rgba(0,0,0,0.4),8,0,0,2);");
            }
        }
        if (node instanceof HBox) {
            String styleClass = node.getStyleClass().toString();
            if (styleClass.contains("top-header")) {
                node.setStyle("-fx-background-color: #1E1E1E; " +
                        "-fx-padding: 16 20 16 20; " +
                        "-fx-background-radius: 12; " +
                        "-fx-effect: dropshadow(gaussian,rgba(0,0,0,0.3),8,0,0,2);");
            }
        }
        if (node instanceof Label) {
            String styleClass = node.getStyleClass().toString();
            if (styleClass.contains("page-title")) {
                node.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; " +
                        "-fx-text-fill: #90CAF9;");
            } else if (styleClass.contains("field-label")) {
                node.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; " +
                        "-fx-text-fill: #B0BEC5;");
            } else if (styleClass.contains("card-title")) {
                node.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; " +
                        "-fx-text-fill: #90CAF9; -fx-padding: 0 0 8 0;");
            } else if (styleClass.contains("stat-label")) {
                node.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; " +
                        "-fx-text-fill: #B0BEC5;");
            } else if (styleClass.contains("menu-item")) {
                node.setStyle("-fx-text-fill: #90CAF9; -fx-font-size: 14px; " +
                        "-fx-padding: 10 16 10 16; -fx-cursor: hand; " +
                        "-fx-background-radius: 8;");
            } else if (styleClass.contains("sidebar-title")) {
                node.setStyle("-fx-text-fill: white; -fx-font-size: 20px; " +
                        "-fx-font-weight: bold;");
            } else if (styleClass.contains("footer-text")) {
                node.setStyle("-fx-text-fill: #444444; -fx-font-size: 11px;");
            }
        }
        if (node instanceof TextField) {
            node.setStyle("-fx-background-color: #2C2C2C; " +
                    "-fx-border-color: #444444; " +
                    "-fx-border-radius: 8; " +
                    "-fx-background-radius: 8; " +
                    "-fx-padding: 8 12 8 12; " +
                    "-fx-font-size: 13px; " +
                    "-fx-text-fill: white; " +
                    "-fx-prompt-text-fill: #666666;");
        }
        if (node instanceof ComboBox) {
            node.setStyle("-fx-background-color: #2C2C2C; " +
                    "-fx-border-color: #444444; " +
                    "-fx-border-radius: 8; " +
                    "-fx-background-radius: 8;");
        }
        if (node instanceof TableView) {
            node.setStyle("-fx-background-color: #1E1E1E; " +
                    "-fx-border-color: transparent;");
        }

        // Appliquer récursivement aux enfants
        if (node instanceof javafx.scene.Parent) {
            for (javafx.scene.Node child :
                    ((javafx.scene.Parent) node).getChildrenUnmodifiable()) {
                applyDark(child);
            }
        }
    }

    // Réinitialiser au thème clair
    private static void applyLight(javafx.scene.Node node) {
        // Réinitialiser tous les styles inline
        resetStyles(node);

        if (node instanceof javafx.scene.Parent) {
            for (javafx.scene.Node child :
                    ((javafx.scene.Parent) node).getChildrenUnmodifiable()) {
                applyLight(child);
            }
        }
    }

    private static void resetStyles(javafx.scene.Node node) {
        node.setStyle("");
    }
}