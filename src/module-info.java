module gestion_de_tâches {
    requires javafx.graphics;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires javafx.controls;
    opens gestion_de_tâches to javafx.fxml;

    exports gestion_de_tâches;
}