package gestion_de_tâches;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
public class TacheDAO {
    public static void ajouterTache(Tache tache) {
        // Check if a task with the same title already exists
        String checkQuery = "SELECT COUNT(*) FROM taches WHERE titre = ?";
        try (Connection conn = ConnexionBDD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(checkQuery)) {
            pstmt.setString(1, tache.getTitre());
            ResultSet rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                throw new IllegalArgumentException("Une tâche avec ce titre existe déjà.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la vérification des doublons.", e);
        }

        // Insert the task if no duplicate is found
        String insertQuery = "INSERT INTO taches (titre, description, dateEcheance, priorite, estTerminee, categorie, joursRappel) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnexionBDD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
            pstmt.setString(1, tache.getTitre());
            pstmt.setString(2, tache.getDescription());
            pstmt.setDate(3, Date.valueOf(tache.getDateEcheance()));
            pstmt.setString(4, tache.getPriorite());
            pstmt.setBoolean(5, tache.isEstTerminee());
            pstmt.setString(6, tache.getCategorie());
            pstmt.setInt(7, tache.getJoursRappel());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'ajout de la tâche.", e);
        }
    }

    public static List<Tache> getTaches() {
        List<Tache> taches = new ArrayList<>(); // Ensure ArrayList is used correctly
        String query = "SELECT * FROM taches";
        try (Connection conn = ConnexionBDD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Tache tache = new Tache(
                    rs.getString("titre"),
                    rs.getString("description"),
                    rs.getDate("dateEcheance").toLocalDate(),
                    rs.getString("priorite"),
                    rs.getString("categorie"),
                    rs.getInt("joursRappel") // Ajout du champ joursRappel
                );
                tache.setEstTerminee(rs.getBoolean("estTerminee"));
                taches.add(tache);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return taches;
    }

    public static void marquerTacheTerminee(String titre) {
        String query = "UPDATE taches SET estTerminee = true WHERE titre = ?";
        try (Connection conn = ConnexionBDD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, titre);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void supprimerTache(String titre) {
        String query = "DELETE FROM taches WHERE titre = ?";
        try (Connection conn = ConnexionBDD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, titre);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Tache> rechercherTaches(String keyword) {
        List<Tache> taches = new ArrayList<>(); // Ensure ArrayList is used correctly
        String query = "SELECT * FROM taches WHERE titre LIKE ? OR description LIKE ?";
        try (Connection conn = ConnexionBDD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Tache tache = new Tache(
                    rs.getString("titre"),
                    rs.getString("description"),
                    rs.getDate("dateEcheance").toLocalDate(),
                    rs.getString("priorite"),
                    rs.getString("categorie"), 0
                );
                tache.setEstTerminee(rs.getBoolean("estTerminee"));
                taches.add(tache);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return taches;
    }

    public static void modifierTache(Tache tache, String ancienTitre) {
        String query = "UPDATE taches SET titre = ?, description = ?, dateEcheance = ?, priorite = ?, categorie = ?, joursRappel = ? WHERE titre = ?";
        try (Connection conn = ConnexionBDD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, tache.getTitre());
            pstmt.setString(2, tache.getDescription());
            pstmt.setDate(3, Date.valueOf(tache.getDateEcheance()));
            pstmt.setString(4, tache.getPriorite());
            pstmt.setString(5, tache.getCategorie());
            pstmt.setInt(6, tache.getJoursRappel());
            pstmt.setString(7, ancienTitre); // Utiliser l'ancien titre pour identifier la tâche
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la modification de la tâche.", e);
        }
    }
    public static List<Tache> getTachesAVenir() {
        List<Tache> taches = new ArrayList<>();
        String query = "SELECT * FROM taches WHERE dateEcheance > ?";
        try (Connection conn = ConnexionBDD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setDate(1, Date.valueOf(LocalDate.now()));
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Tache tache = new Tache(
                    rs.getString("titre"),
                    rs.getString("description"),
                    rs.getDate("dateEcheance").toLocalDate(),
                    rs.getString("priorite"),
                    rs.getString("categorie"),
                    rs.getInt("joursRappel")
                );
                tache.setEstTerminee(rs.getBoolean("estTerminee"));
                taches.add(tache);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return taches;
    }
    public static List<Tache> getTachesNonTerminees() {
        List<Tache> taches = new ArrayList<>();
        String query = "SELECT * FROM taches WHERE estTerminee = false";
        try (Connection conn = ConnexionBDD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Tache tache = new Tache(
                    rs.getString("titre"),
                    rs.getString("description"),
                    rs.getDate("dateEcheance").toLocalDate(),
                    rs.getString("priorite"),
                    rs.getString("categorie"),
                    rs.getInt("joursRappel")
                );
                tache.setEstTerminee(rs.getBoolean("estTerminee"));
                taches.add(tache);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return taches;
    }
 
 /*****/
    public static List<Tache> rechercherMessages(String keyword, String filter) {
        List<String> priorities = Arrays.asList("Faible", "Moyenne", "Haute");
        List<String> categories = Arrays.asList("Travail", "Personnel", "Urgent");
        
        String baseQuery = "SELECT * FROM taches WHERE estTerminee = false " +
                           "AND (titre LIKE ? OR description LIKE ?) ";
        
        if (filter != null && !filter.isEmpty()) {
            if (priorities.contains(filter)) {
                baseQuery += "AND priorite = ?";
            } else if (categories.contains(filter)) {
                baseQuery += "AND categorie = ?";
            }
        }
        
        return executerRechercheAvancee(baseQuery, keyword, filter);
    }

    public static List<Tache> rechercherTachesAVenir(String keyword, String filter) {
        List<String> priorities = Arrays.asList("Faible", "Moyenne", "Haute");
        List<String> categories = Arrays.asList("Travail", "Personnel", "Urgent");
        
        String baseQuery = "SELECT * FROM taches WHERE dateEcheance > ? " +
                           "AND (titre LIKE ? OR description LIKE ?) ";
        
        if (filter != null && !filter.isEmpty()) {
            if (priorities.contains(filter)) {
                baseQuery += "AND priorite = ?";
            } else if (categories.contains(filter)) {
                baseQuery += "AND categorie = ?";
            }
        }
        
        return executerRechercheAvancee(baseQuery, keyword, filter, Date.valueOf(LocalDate.now()));
    }
    private static List<Tache> executerRechercheAvancee(String query, String keyword, String filter, Object... params) {
        List<Tache> taches = new ArrayList<>();
        try (Connection conn = ConnexionBDD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            int paramIndex = 1;
            
            // Add additional parameters (like date)
            for (Object param : params) {
                if (param instanceof Date) {
                    pstmt.setDate(paramIndex++, (Date) param);
                }
            }
            
            // Set the keyword parameters
            pstmt.setString(paramIndex++, "%" + keyword + "%");
            pstmt.setString(paramIndex++, "%" + keyword + "%");
            
            // Add the filter only if it matches a priority or category
            if (filter != null && !filter.isEmpty()) {
                // Check if the query has a placeholder for the filter
                if (query.contains("?")) {
                    pstmt.setString(paramIndex++, filter);
                }
            }
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Tache tache = new Tache(
                    rs.getString("titre"),
                    rs.getString("description"),
                    rs.getDate("dateEcheance").toLocalDate(),
                    rs.getString("priorite"),
                    rs.getString("categorie"),
                    rs.getInt("joursRappel")
                );
                tache.setEstTerminee(rs.getBoolean("estTerminee"));
                taches.add(tache);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return taches;
    }
    
 

    public static List<Tache> getTachesTerminees() {
        List<Tache> taches = new ArrayList<>();
        String query = "SELECT * FROM taches WHERE estTerminee = true";
        try (Connection conn = ConnexionBDD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Tache tache = new Tache(
                    rs.getString("titre"),
                    rs.getString("description"),
                    rs.getDate("dateEcheance").toLocalDate(),
                    rs.getString("priorite"),
                    rs.getString("categorie"),
                    rs.getInt("joursRappel")
                );
                tache.setEstTerminee(rs.getBoolean("estTerminee"));
                taches.add(tache);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return taches;
    }
  
}