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
import java.util.List;

public class TachesAVenirController {
    private MainController mainController;
    @FXML
    private FlowPane tachesFlowPane;

    @FXML
    public void initialize() {
        loadTachesAVenir();
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
    public void resetFilter() {
    	tachesFlowPane.getChildren().clear();
        List<Tache> taches = TacheDAO.getTachesAVenir(); 
        for (Tache tache : taches) {
            VBox card = createTaskCard(tache);
            tachesFlowPane.getChildren().add(card);
        }
    }
  
    private void loadTachesAVenir() {
        tachesFlowPane.getChildren().clear();
        List<Tache> taches = TacheDAO.getTachesAVenir();
        for (Tache tache : taches) {
            VBox card = createTaskCard(tache);
            tachesFlowPane.getChildren().add(card);
        }
    }

    private VBox createTaskCard(Tache tache) {
        VBox card = new VBox(10);
        card.getStyleClass().add("task-card");
        card.setAlignment(Pos.CENTER); // Centrer le contenu de la carte

        Label titleLabel = new Label(tache.getTitre());
        titleLabel.getStyleClass().add("task-card-title");

        Label dueDateLabel = new Label("Date: " + tache.getDateEcheance());
        dueDateLabel.getStyleClass().add("task-date");

        Label priorityLabel = new Label("Priorité: " + tache.getPriorite());
        switch(tache.getPriorite()) {
            case "Haute": priorityLabel.getStyleClass().add("priority-high"); break;
            case "Moyenne": priorityLabel.getStyleClass().add("priority-medium"); break;
            default: priorityLabel.getStyleClass().add("priority-low"); break;
        }

        card.getChildren().addAll(titleLabel, dueDateLabel, priorityLabel);
        return card;
    }
    public void applyFilter(String keyword, String filter) {
        tachesFlowPane.getChildren().clear();
        List<Tache> taches = TacheDAO.rechercherTachesAVenir(keyword, filter);
        for (Tache tache : taches) {
            VBox card = createTaskCard(tache);
            tachesFlowPane.getChildren().add(card);
        }
    }
    @FXML
    private void retourAMain() {
      
        	  if (mainController != null) {
                  mainController.showDashboard(); 
                  mainController.resetSearchFilter();
              }
       
    }
    
    
}