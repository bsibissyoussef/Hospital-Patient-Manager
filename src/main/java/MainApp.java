import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import managers.DataStore;
import managers.JsonManager;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Initialiser JsonManager
        JsonManager.init();

        // Charger les données (fichier ou défaut)
        DataStore.getInstance().init();

        // Charger la page LOGIN
        FXMLLoader loader = new FXMLLoader(
                MainApp.class.getResource("/resources/login.fxml"));
        Scene scene = new Scene(loader.load(), 700, 500);

        // Appliquer le CSS
        try {
            scene.getStylesheets().add(
                    MainApp.class.getResource("/resources/style.css")
                            .toExternalForm());
        } catch (Exception e) {
            System.out.println("CSS non trouvé.");
        }

        primaryStage.setTitle("🏥 Hospital Patient Manager — Login");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}