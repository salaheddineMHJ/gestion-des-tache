package gestion_de_tâches;

import java.time.LocalDate;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Tache {
    private SimpleStringProperty titre;
    private SimpleStringProperty description;
    private SimpleObjectProperty<LocalDate> dateEcheance;
    private SimpleStringProperty priorite;
    private SimpleStringProperty categorie;
    private SimpleIntegerProperty joursRappel; // Nouveau champ
    private boolean estTerminee;

    public Tache(String titre, String description, LocalDate dateEcheance, String priorite, String categorie, int joursRappel) {
        this.titre = new SimpleStringProperty(titre);
        this.description = new SimpleStringProperty(description != null ? description : "");
        this.dateEcheance = new SimpleObjectProperty<>(dateEcheance);
        this.priorite = new SimpleStringProperty(priorite != null ? priorite : "Moyenne");
        this.categorie = new SimpleStringProperty(categorie != null ? categorie : "Personnel"); // Handle null
        this.joursRappel = new SimpleIntegerProperty(joursRappel);
        this.estTerminee = false;
    }

    // Getters et setters pour le nouveau champ
    public int getJoursRappel1() {
        return joursRappel.get();
    }

    public void setJoursRappel1(int joursRappel) {
        this.joursRappel.set(joursRappel);
    }

    public SimpleIntegerProperty joursRappelProperty1() {
        return joursRappel;
    }

    // Getters et setters pour le nouveau champ
    public int getJoursRappel() {
        return joursRappel.get();
    }

    public void setJoursRappel(int joursRappel) {
        this.joursRappel.set(joursRappel);
    }

    public SimpleIntegerProperty joursRappelProperty() {
        return joursRappel;
    }

    // Méthode pour valider la date d'échéance
    public void validerDateEcheance() {
        if (dateEcheance.get() != null && dateEcheance.get().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La date d'échéance ne peut pas être antérieure à la date actuelle.");
        }
    }

    public String getTitre() {
        return titre.get();
    }

    public void setTitre(String titre) {
        this.titre.set(titre);
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public LocalDate getDateEcheance() {
        return dateEcheance.get();
    }

    public void setDateEcheance(LocalDate dateEcheance) {
        this.dateEcheance.set(dateEcheance);
    }

    public String getPriorite() {
        return priorite.get();
    }

    public void setPriorite(String priorite) {
        this.priorite.set(priorite);
    }

    public String getCategorie() {
        return categorie.get();
    }

    public void setCategorie(String categorie) {
        this.categorie.set(categorie);
    }

    public boolean isEstTerminee() {
        return estTerminee;
    }

    public void setEstTerminee(boolean estTerminee) {
        this.estTerminee = estTerminee;
    }

    public SimpleStringProperty titreProperty() {
        return titre;
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public SimpleObjectProperty<LocalDate> dateEcheanceProperty() {
        return dateEcheance;
    }

    public SimpleStringProperty prioriteProperty() {
        return priorite;
    }

    public SimpleStringProperty categorieProperty() {
        return categorie;
    }

    public SimpleStringProperty statutProperty() {
        return new SimpleStringProperty(estTerminee ? "Terminée" : "En cours");
    }
}