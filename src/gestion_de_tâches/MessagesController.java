package gestion_de_tâches;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class MessagesController {
	private MainController mainController;
    @FXML
    private FlowPane messagesFlowPane;

    @FXML
    public void initialize() {
        // Charger les tâches non terminées depuis la base de données
        List<Tache> taches = TacheDAO.getTachesNonTerminees();

        // Afficher chaque tâche sous forme de carte de message
        for (Tache tache : taches) {
            VBox card = createMessageCard(tache);
            messagesFlowPane.getChildren().add(card);
        }
    }
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
   
    public void resetFilter() {
        messagesFlowPane.getChildren().clear();
        List<Tache> taches = TacheDAO.getTachesNonTerminees(); // Recharge toutes les tâches non terminées
        for (Tache tache : taches) {
            VBox card = createMessageCard(tache);
            messagesFlowPane.getChildren().add(card);
        }
    }
    
    private VBox createMessageCard(Tache tache) {
        VBox card = new VBox(10);
        card.getStyleClass().add("message-card");
        card.setAlignment(Pos.CENTER); // Centrer le contenu de la carte

        Label titleLabel = new Label(tache.getTitre());
        titleLabel.getStyleClass().add("message-card-title");

        Label descriptionLabel = new Label(tache.getDescription());
        descriptionLabel.setWrapText(true);

        int joursRestants = tache.getJoursRappel();
        Label dueDateLabel = new Label();

        if (joursRestants > 0) {
            dueDateLabel.setText("Jours restants : " + joursRestants);
            dueDateLabel.getStyleClass().add("message-status-remaining");
        } else if (joursRestants == 0) {
            dueDateLabel.setText("Aujourd'hui !");
            dueDateLabel.getStyleClass().add("message-status-today");
        } else {
            dueDateLabel.setText("En retard de " + Math.abs(joursRestants) + " jours");
            dueDateLabel.getStyleClass().add("message-status-overdue");
        }

        card.getChildren().addAll(titleLabel, descriptionLabel, dueDateLabel);
        return card;
    }
    
    public void applyFilter(String keyword, String filter) {
        messagesFlowPane.getChildren().clear();
        List<Tache> taches;
        if (keyword.isEmpty() && (filter == null || filter.isEmpty())) {
            taches = TacheDAO.getTachesNonTerminees(); // Afficher tout si aucun filtre
        } else {
            taches = TacheDAO.rechercherMessages(keyword, filter);
        }
        for (Tache tache : taches) {
            VBox card = createMessageCard(tache);
            messagesFlowPane.getChildren().add(card);
        }
    }
    @FXML
    private void retourAMain() {
        if (mainController != null) {
            
            mainController.resetSearchFilter();
            mainController.showDashboard();
        }
    }
   
}