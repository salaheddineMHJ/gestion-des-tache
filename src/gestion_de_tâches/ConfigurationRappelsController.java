package gestion_de_tâches;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ConfigurationRappelsController {

    @FXML private TextField joursAvantEcheanceField;

    private int joursAvantEcheance = 2; // Valeur par défaut

    @FXML
    private void sauvegarderConfiguration() {
        try {
            joursAvantEcheance = Integer.parseInt(joursAvantEcheanceField.getText());
            if (joursAvantEcheance < 0) {
                throw new IllegalArgumentException("Le nombre de jours doit être positif.");
            }
            fermerFenetre();
        } catch (NumberFormatException e) {
            // Afficher une erreur si la saisie n'est pas un nombre valide
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez entrer un nombre valide.");
            alert.showAndWait();
        } catch (IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void fermerFenetre() {
        Stage stage = (Stage) joursAvantEcheanceField.getScene().getWindow();
        stage.close();
    }

    public int getJoursAvantEcheance() {
        return joursAvantEcheance;
    }
}