package gestion_de_tâches;

import java.util.List;

public class GestionTaches {

    public void ajouterTache(Tache tache) {
        if (tache.getTitre() == null || tache.getTitre().isEmpty()) {
            System.out.println("Erreur : Le titre de la tâche est obligatoire.");
        } else {
            TacheDAO.ajouterTache(tache);
            System.out.println("Tâche ajoutée avec succès.");
        }
    }

    public void supprimerTache(String titre) {
        TacheDAO.supprimerTache(titre);
        System.out.println("Tâche supprimée avec succès.");
    }

    public void marquerTacheTerminee(String titre) {
        TacheDAO.marquerTacheTerminee(titre);
        System.out.println("Tâche marquée comme terminée.");
    }

    public List<Tache> getTaches() {
        return TacheDAO.getTaches();
    }
}