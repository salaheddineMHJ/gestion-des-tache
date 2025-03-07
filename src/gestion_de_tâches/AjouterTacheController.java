package gestion_de_tâches;

import java.io.IOException;
import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AjouterTacheController {
	 private MainController mainController;
    @FXML private TextField titreField;
    @FXML private TextField descriptionField;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> prioriteComboBox;
    @FXML private ComboBox<String> categorieComboBox;
    @FXML private TextField joursRappelField;

    @FXML
    public void initialize() {
        // Initialiser les ComboBox
        prioriteComboBox.getItems().addAll("Faible", "Moyenne", "Haute");
        categorieComboBox.getItems().addAll("Travail", "Personnel", "Urgent");
    }
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
    @FXML
    private void sauvegarderTache() {
        try {
            // Récupérer les valeurs des champs
            String titre = titreField.getText();
            String description = descriptionField.getText();
            LocalDate dateEcheance = datePicker.getValue();
            String priorite = prioriteComboBox.getValue();
            String categorie = categorieComboBox.getValue();

            // Gérer le champ "joursRappel" comme optionnel
            int joursRappel = 0; // Valeur par défaut si le champ est vide
            if (!joursRappelField.getText().isEmpty()) {
                joursRappel = Integer.parseInt(joursRappelField.getText());
            }

            // Validation du titre (champ obligatoire)
            if (titre == null || titre.isEmpty()) {
                showAlert("Erreur", "Le titre de la tâche est obligatoire.");
                return;
            }

            // Validation de la date d'échéance (ne doit pas être antérieure à la date actuelle)
            if (dateEcheance != null && dateEcheance.isBefore(LocalDate.now())) {
                showAlert("Erreur", "La date d'échéance ne peut pas être antérieure à la date actuelle.");
                return;
            }

            // Appliquer des valeurs par défaut si nécessaire
            if (description == null || description.isEmpty()) {
                description = ""; // Description vide par défaut
            }
           
            if (mainController != null) {
                mainController.showDashboard();
            }
            if (priorite == null || priorite.isEmpty()) {
                priorite = "Moyenne";
            }
            if (categorie == null || categorie.isEmpty()) {
                categorie = "Personnel"; // Set default category
            }

            // Créer la nouvelle tâche
            Tache nouvelleTache = new Tache(titre, description, dateEcheance, priorite, categorie, joursRappel);

            // Ajouter la tâche à la base de données
            TacheDAO.ajouterTache(nouvelleTache);

            // Fermer la fenêtre après avoir sauvegardé la tâche
            retourAMain();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer un nombre valide pour les jours de rappel.");
        } catch (RuntimeException e) {
            showAlert("Erreur", e.getMessage());
        }
    }
    @FXML
    private void annuler() {
    	retourAMain();
    }

    private void retourAMain() {
        try {
            // Charger la vue principale (main.fxml)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
            Parent root = loader.load();

            // Obtenir la scène actuelle
            Scene scene = titreField.getScene();

            // Remplacer la scène actuelle par la vue principale
            scene.setRoot(root);

            // Rafraîchir les données dans le MainController
            MainController mainController = loader.getController();
            mainController.refreshData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
}