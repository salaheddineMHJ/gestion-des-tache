package gestion_de_tâches;

import javafx.event.ActionEvent;

import java.io.IOException;
import java.lang.classfile.components.ClassPrinter.Node;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.stream.Collectors;
public class MainController {
	 @FXML private BorderPane mainBorderPane;
	 private Parent currentView;
    // Références aux éléments du formulaire d'ajout
    @FXML private VBox formulaireAjoutBox;
    @FXML private TextField titreField;
    @FXML private TextField descriptionField;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> prioriteComboBox;
    @FXML private ComboBox<String> categorieComboBox;
    @FXML private TextField joursRappelField;

    // Références aux éléments du formulaire de modification
    @FXML private VBox formulaireModificationBox;
    @FXML private TextField titreModifField;
    @FXML private TextField descriptionModifField;
    @FXML private DatePicker dateModifPicker;
    @FXML private ComboBox<String> prioriteModifComboBox;
    @FXML private ComboBox<String> categorieModifComboBox;
    @FXML private TextField joursRappelModifField;

    // Références aux éléments de la TableView et des filtres
    @FXML private TableView<Tache> tacheTable;
    @FXML private TableColumn<Tache, String> titreCol;
    @FXML private TableColumn<Tache, String> descriptionCol;
    @FXML private TableColumn<Tache, LocalDate> dateCol;
    @FXML private TableColumn<Tache, String> prioriteCol;
    @FXML private TableColumn<Tache, String> categorieCol;
    @FXML private TableColumn<Tache, String> statutCol;
    @FXML private ComboBox<String> filterComboBox;
    @FXML private TextField searchField;
    @FXML private Label tachesEnCoursLabel;
    @FXML private Label tachesUrgentesLabel;
    @FXML private FlowPane tachesAVenirFlowPane;
    private ObservableList<Tache> taches = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialisation des ComboBox
        prioriteComboBox.setItems(FXCollections.observableArrayList("Faible", "Moyenne", "Haute"));
        categorieComboBox.setItems(FXCollections.observableArrayList("Travail", "Personnel", "Urgent"));
        prioriteModifComboBox.setItems(FXCollections.observableArrayList("Faible", "Moyenne", "Haute"));
        categorieModifComboBox.setItems(FXCollections.observableArrayList("Travail", "Personnel", "Urgent"));
        filterComboBox.setItems(FXCollections.observableArrayList("Afficher tout", "Faible", "Moyenne", "Haute", "Travail", "Personnel", "Urgent"));
        
        // Liaison des colonnes de la TableView avec les propriétés de Tache
        titreCol.setCellValueFactory(cellData -> cellData.getValue().titreProperty());
        descriptionCol.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        dateCol.setCellValueFactory(cellData -> cellData.getValue().dateEcheanceProperty());
        prioriteCol.setCellValueFactory(cellData -> cellData.getValue().prioriteProperty());
        categorieCol.setCellValueFactory(cellData -> cellData.getValue().categorieProperty());
        statutCol.setCellValueFactory(cellData -> cellData.getValue().statutProperty());
        currentView = (Parent) mainBorderPane.getCenter();
        // Charger les tâches depuis la base de données
        try {
            taches.addAll(TacheDAO.getTaches());
            tacheTable.setItems(taches);
        } catch (Exception e) {
            showAlert("Erreur", "Impossible de charger les tâches depuis la base de données.");
            e.printStackTrace();
        }
       
        updateDashboard();
    }
    public void loadView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent view = loader.load();
            view.getProperties().put("controller", loader.getController());
            Object controller = loader.getController();
            
            if (controller instanceof MessagesController) {
                ((MessagesController) controller).setMainController(this);
            } else if (controller instanceof TachesAVenirController) {
                ((TachesAVenirController) controller).setMainController(this);
            }else if (controller instanceof TachesTermineesController) {
                ((TachesTermineesController) controller).setMainController(this);
            }
            // Définir userData pour identifier la vue
            switch (fxmlFile) {
                case "messages.fxml":
                    view.setUserData("messages");
                    break;
                case "taches_a_venir.fxml":
                    view.setUserData("taches-a-venir");
                    break;
                case "taches_terminees.fxml":
                    view.setUserData("taches-terminees");
                    break;
                default:
                    view.setUserData("main");
            }

            mainBorderPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showDashboard() {
        mainBorderPane.setCenter(currentView);
        refreshData(); 
    }

    // Rafraîchir les données de la TableView
    public void refreshData() {
        taches.clear();
        taches.addAll(TacheDAO.getTaches());
        tacheTable.refresh();
        updateDashboard();
    }

//Avant : Ouvrait une nouvelle fenêtre
@FXML
private void afficherFormulaireAjout() {
 loadView("ajouter_tache.fxml"); // Maintenant chargé dans le même BorderPane
}

//Avant : Ouvrait une nouvelle fenêtre
@FXML
private void afficherMessages() {
 loadView("messages.fxml"); // Maintenant chargé dans le même BorderPane
}
private VBox createTaskCard(Tache tache) {
    VBox card = new VBox(10);
    card.getStyleClass().add("task-card");
    
    Label titleLabel = new Label(tache.getTitre());
    titleLabel.getStyleClass().add("task-card-title");
    
    Label priorityLabel = new Label("Priorité: " + tache.getPriorite());
    
    // Appliquer le style seulement sur le label de priorité
    switch(tache.getPriorite()) {
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

    card.getChildren().addAll(
        titleLabel, 
        new Label(tache.getDescription()), 
        new Label("Date: " + tache.getDateEcheance()), 
        priorityLabel
    );
    
    return card;
}
	
    @FXML
    private Label tachesAVenirLabel;
    @FXML
    private ProgressBar progressBar; // Ajoutez cette ligne

    @FXML
    private void afficherTachesAVenir() {
        loadView("taches_a_venir.fxml");
    }

    @FXML
    private void cacherFormulaireAjout() {
        formulaireAjoutBox.setVisible(false); // Cacher le formulaire d'ajout
        clearFields(); // Vider les champs du formulaire d'ajout
    }

    @FXML
    private void cacherFormulaireModification() {
        formulaireModificationBox.setVisible(false); // Cacher le formulaire de modification
        clearFields(); // Vider les champs du formulaire de modification
    }

    @FXML
    private void sauvegarderTache() {
        try {
            String titre = titreField.getText();
            String description = descriptionField.getText();
            LocalDate dateEcheance = datePicker.getValue();
            String priorite = prioriteComboBox.getValue();
            String categorie = categorieComboBox.getValue();
            int joursRappel = Integer.parseInt(joursRappelField.getText());

            if (titre == null || titre.isEmpty()) {
                showAlert("Erreur", "Le titre de la tâche est obligatoire.");
                return;
            }

            Tache nouvelleTache = new Tache(titre, description, dateEcheance, priorite, categorie, joursRappel);
            TacheDAO.ajouterTache(nouvelleTache);
            taches.add(nouvelleTache); // Ajouter la nouvelle tâche à la liste observable

            tacheTable.refresh();
            cacherFormulaireAjout();
            updateDashboard();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez entrer un nombre valide pour les jours de rappel.");
        } catch (RuntimeException e) {
            showAlert("Erreur", e.getMessage());
        }
    }

    @FXML
    private void sauvegarderTacheModif() {
        Tache selectedTache = tacheTable.getSelectionModel().getSelectedItem();
        if (selectedTache != null) {
            selectedTache.setTitre(titreModifField.getText());
            selectedTache.setDescription(descriptionModifField.getText());
            selectedTache.setDateEcheance(dateModifPicker.getValue());
            selectedTache.setPriorite(prioriteModifComboBox.getValue());
            selectedTache.setCategorie(categorieModifComboBox.getValue());
            selectedTache.setJoursRappel(Integer.parseInt(joursRappelModifField.getText()));

            TacheDAO.modifierTache(selectedTache, selectedTache.getTitre());
            tacheTable.refresh();
            cacherFormulaireModification();
            updateDashboard();
        } else {
            showAlert("Erreur", "Aucune tâche sélectionnée.");
        }
    }

    

    @FXML
    private void supprimerTacheModif() {
        Tache selectedTache = tacheTable.getSelectionModel().getSelectedItem();
        if (selectedTache != null) {
            // Demander confirmation à l'utilisateur
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Êtes-vous sûr de vouloir supprimer cette tâche ?");
            alert.setContentText("Cette action est irréversible.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Supprimer la tâche si l'utilisateur confirme
                TacheDAO.supprimerTache(selectedTache.getTitre());
                taches.remove(selectedTache);
                tacheTable.refresh();
                updateDashboard();
                cacherFormulaireModification();
            }
        } else {
            showAlert("Erreur", "Aucune tâche sélectionnée.");
        }
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
   
    @FXML
    private void filterTaches() {
        String filter = filterComboBox.getValue();
        if (filter != null) {
            if (filter.equals("Afficher tout")) {
                tacheTable.setItems(taches);
            } else {
                ObservableList<Tache> filteredTaches = FXCollections.observableArrayList();
                for (Tache tache : taches) {
                    if (tache.getPriorite().equals(filter) || tache.getCategorie().equals(filter)) {
                        filteredTaches.add(tache);
                    }
                }
                tacheTable.setItems(filteredTaches);
            }
        }
    }

    @FXML
    private void rechercherTaches() {
        String keyword = searchField.getText();
        if (keyword != null && !keyword.isEmpty()) {
            List<Tache> resultats = TacheDAO.rechercherTaches(keyword);
            taches.clear();
            taches.addAll(resultats);
            tacheTable.setItems(taches);
        } else {
            // Si le champ de recherche est vide, afficher toutes les tâches
            taches.clear();
            taches.addAll(TacheDAO.getTaches());
            tacheTable.setItems(taches);
        }
    }
  
    @FXML
    private void afficherToutesTaches() {
        // Réinitialiser le champ de recherche
        searchField.clear();

        // Réinitialiser le filtre à "Afficher tout"
        filterComboBox.setValue("Afficher tout");

        // Recharger toutes les tâches depuis la base de données
        taches.clear();
        try {
            taches.addAll(TacheDAO.getTaches());
        } catch (Exception e) {
            showAlert("Erreur", "Impossible de charger les tâches depuis la base de données.");
            e.printStackTrace();
        }
        tacheTable.setItems(taches);

        // Mettre à jour le tableau de bord
        updateDashboard();
    }

    @FXML
    private void updateDashboard() {
        int tachesEnCours = 0;
        int tachesUrgentes = 0;
        int tachesTerminees = 0;

        for (Tache tache : taches) {
            if (!tache.isEstTerminee()) {
                tachesEnCours++;
                if (tache.getPriorite().equals("Haute")) {
                    tachesUrgentes++;
                }
            } else {
                tachesTerminees++;
            }
           
        }

        // Mettre à jour les labels
        tachesEnCoursLabel.setText(String.valueOf(tachesEnCours));
        tachesUrgentesLabel.setText(String.valueOf(tachesUrgentes));

        // Calculer la progression globale
        int totalTaches = taches.size();
        double progression = (totalTaches == 0) ? 0 : (double) tachesTerminees / totalTaches;

        // Mettre à jour la ProgressBar
        progressBar.setProgress(progression);
    }
    private void loadTachesAVenir() {
       
        tachesAVenirFlowPane.getChildren().clear(); 
        List<Tache> taches = TacheDAO.getTachesAVenir();
        // 3. Create and add cards to the FlowPane
        for (Tache tache : taches) {
            VBox card = createTaskCard(tache);
            tachesAVenirFlowPane.getChildren().add(card);
        }
    }
    
 
    @FXML
    private void terminerTacheModif() {
        Tache selectedTache = tacheTable.getSelectionModel().getSelectedItem();
        if (selectedTache != null) {
            selectedTache.setEstTerminee(true);
            TacheDAO.marquerTacheTerminee(selectedTache.getTitre());
            tacheTable.refresh();
            updateDashboard(); // Ajoutez cette ligne
            cacherFormulaireModification();
        } else {
            showAlert("Erreur", "Aucune tâche sélectionnée.");
        }
    }

    private void clearFields() {
        titreField.clear();
        descriptionField.clear();
        datePicker.setValue(null);
        prioriteComboBox.setValue(null);
        categorieComboBox.setValue(null);
        joursRappelField.clear();

        titreModifField.clear();
        descriptionModifField.clear();
        dateModifPicker.setValue(null);
        prioriteModifComboBox.setValue(null);
        categorieModifComboBox.setValue(null);
        joursRappelModifField.clear();
    }

   
    @FXML
    private void afficherDiagramme() {
        // Compter les tâches terminées et non terminées
        int tachesTerminees = 0;
        int tachesNonTerminees = 0;

        for (Tache tache : taches) {
            if (tache.isEstTerminee()) {
                tachesTerminees++;
            } else {
                tachesNonTerminees++;
            }
        }

        // Créer les données pour le PieChart
        PieChart.Data terminees = new PieChart.Data("Terminées", tachesTerminees);
        PieChart.Data nonTerminees = new PieChart.Data("Non Terminées", tachesNonTerminees);

        // Créer le PieChart
        PieChart pieChart = new PieChart();
        pieChart.getData().addAll(terminees, nonTerminees);
        pieChart.setTitle("Progression des Tâches");

        // Créer une nouvelle fenêtre pour afficher le diagramme
        VBox vbox = new VBox(pieChart);
        Scene scene = new Scene(vbox, 400, 300);

        Stage stage = new Stage();
        stage.setTitle("Diagramme des Tâches");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private Button terminerModifButton; // Référence au bouton "Terminer Tâche"

    @FXML
    private void afficherFormulaireModification() {
        Tache selectedTache = tacheTable.getSelectionModel().getSelectedItem();
        if (selectedTache != null) {
            // Remplir les champs du formulaire de modification
            titreModifField.setText(selectedTache.getTitre());
            descriptionModifField.setText(selectedTache.getDescription());
            dateModifPicker.setValue(selectedTache.getDateEcheance());
            prioriteModifComboBox.setValue(selectedTache.getPriorite());
            categorieModifComboBox.setValue(selectedTache.getCategorie());
            joursRappelModifField.setText(String.valueOf(selectedTache.getJoursRappel()));

            // Cacher ou afficher le bouton "Terminer Tâche" en fonction de l'état de la tâche
            if (selectedTache.isEstTerminee()) {
                terminerModifButton.setVisible(false); // Cacher le bouton si la tâche est déjà terminée
            } else {
                terminerModifButton.setVisible(true); // Afficher le bouton si la tâche n'est pas terminée
            }

            formulaireModificationBox.setVisible(true); // Afficher le formulaire de modification
            formulaireAjoutBox.setVisible(false); // Cacher le formulaire d'ajout
        } else {
            showAlert("Erreur", "Aucune tâche sélectionnée.");
        }
    }
    
    @FXML
    private void handleGlobalSearchFilter() {
        String keyword = searchField.getText().trim();
        String filter = filterComboBox.getValue();

        // Mettre à jour les variables de filtre et de recherche
        setCurrentSearchKeyword(keyword);
        setCurrentFilter(filter);

        // Appliquer le filtre et la recherche à la vue active
        if (mainBorderPane.getCenter().getUserData() != null) {
            String activeView = mainBorderPane.getCenter().getUserData().toString();
            switch (activeView) {
                case "messages":
                    loadMessagesWithFilter(keyword, filter);
                    break;
                case "taches-a-venir":
                    loadTachesAVenirWithFilter(keyword, filter);
                    break;
                default:
                    filterTachesTable(keyword, filter);
            }
        } else {
            filterTachesTable(keyword, filter);
        }
    }
    private void filterTachesTable(String keyword, String filter) {
        // Retrieve tasks based on the search keyword
        List<Tache> resultats = TacheDAO.rechercherTaches(keyword);
        ObservableList<Tache> filteredTaches = FXCollections.observableArrayList(resultats);

        // Apply additional filter if selected
        if (filter != null && !filter.isEmpty() && !filter.equals("Afficher tout")) {
            filteredTaches = filteredTaches.stream()
                    .filter(tache -> 
                        tache.getPriorite().equals(filter) || 
                        tache.getCategorie().equals(filter))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
        }

        // Update the table with the filtered results
        tacheTable.setItems(filteredTaches);
    }

    private void loadMessagesWithFilter(String keyword, String filter) {
        try {
            javafx.scene.Node currentView = mainBorderPane.getCenter();
            MessagesController controller = (MessagesController) currentView.getProperties().get("controller");
            controller.applyFilter(keyword, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadTachesAVenirWithFilter(String keyword, String filter) {
        try {
            javafx.scene.Node currentView = mainBorderPane.getCenter();
            TachesAVenirController controller = (TachesAVenirController) currentView.getProperties().get("controller");
            controller.applyFilter(keyword, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML void resetSearchFilter() {
        // Réappliquer les filtres et la recherche
        searchField.setText(currentSearchKeyword);
        filterComboBox.setValue(currentFilter);

        // Rafraîchir la vue active avec les filtres et la recherche actuels
        if (mainBorderPane.getCenter().getUserData() != null) {
            String activeView = mainBorderPane.getCenter().getUserData().toString();
            switch (activeView) {
                case "messages":
                    loadMessagesWithFilter(currentSearchKeyword, currentFilter);
                    break;
                case "taches-a-venir":
                    loadTachesAVenirWithFilter(currentSearchKeyword, currentFilter);
                    break;
                default:
                    tacheTable.setItems(FXCollections.observableArrayList(TacheDAO.getTaches()));
            }
        }
    }
    private String currentFilter = "Afficher tout"; // Filtre par défaut
    private String currentSearchKeyword = ""; // Recherche par défaut (vide)

    // Getters et setters pour les filtres et la recherche
    public String getCurrentFilter() {
        return currentFilter;
    }

    public void setCurrentFilter(String currentFilter) {
        this.currentFilter = currentFilter;
    }

    public String getCurrentSearchKeyword() {
        return currentSearchKeyword;
    }

    public void setCurrentSearchKeyword(String currentSearchKeyword) {
        this.currentSearchKeyword = currentSearchKeyword;
    }
    @FXML
    private void afficherTachesTerminees() {
        loadView("taches_terminees.fxml");
    }
   
   
    
}