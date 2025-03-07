package gestion_de_tâches;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import java.util.List;

public class TachesTermineesController {
    private MainController mainController;

    @FXML
    private FlowPane tachesFlowPane;

    @FXML
    public void initialize() {
        loadTachesTerminees();
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    private void loadTachesTerminees() {
        tachesFlowPane.getChildren().clear();
        List<Tache> taches = TacheDAO.getTachesTerminees();
        for (Tache tache : taches) {
            VBox card = createTaskCard(tache);
            tachesFlowPane.getChildren().add(card);
        }
    }

   private VBox createTaskCard(Tache tache) {
    VBox card = new VBox(10);
    card.getStyleClass().add("task-card-terminée"); // Appliquer le style CSS

    // Titre de la tâche
    Label titleLabel = new Label("Titre: " + tache.getTitre());
    titleLabel.getStyleClass().add("task-card-title");

    // Description de la tâche
    Label descriptionLabel = new Label("Description: " + tache.getDescription());
    descriptionLabel.getStyleClass().add("task-description");
    descriptionLabel.setWrapText(true); // Retour à la ligne automatique

    // Date d'échéance
    Label dueDateLabel = new Label("Date d'échéance: " + tache.getDateEcheance());
    dueDateLabel.getStyleClass().add("task-date");

    // Priorité
    Label priorityLabel = new Label("Priorité: " + tache.getPriorite());
    switch (tache.getPriorite()) {
        case "Haute":
            priorityLabel.getStyleClass().add("priority-high");
            break;
        case "Moyenne":
            priorityLabel.getStyleClass().add("priority-medium");
            break;
        default:
            priorityLabel.getStyleClass().add("priority-low");
            break;
    }

    // Statut "Terminée"
    Label statusLabel = new Label("Statut: Terminée");
    statusLabel.getStyleClass().add("status-label");

    // Ajouter tous les éléments à la carte
    card.getChildren().addAll(titleLabel, descriptionLabel, dueDateLabel, priorityLabel, statusLabel);
    return card;
}
    @FXML
    private void retourAMain() {
        if (mainController != null) {
        	mainController.showDashboard();
        } else {
            System.err.println("mainController is null");
        }
    }
}