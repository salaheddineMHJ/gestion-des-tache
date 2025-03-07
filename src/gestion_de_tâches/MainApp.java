package gestion_de_tâches;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Charger le fichier FXML
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));

        // Configurer la scène
        Scene scene = new Scene(root, 800, 600);

        // Configurer la fenêtre principale
        primaryStage.setTitle("Gestion de Tâches");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Méthode principale pour lancer l'application
    public static void main(String[] args) {
        launch(args); // Lancer l'application JavaFX
    }
}