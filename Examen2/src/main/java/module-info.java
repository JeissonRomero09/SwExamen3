module co.edu.poli.Examen2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.mariadb.jdbc;

    opens co.edu.poli.Examen2.infraestructura.adaptador.entrada.javafx to javafx.fxml;

    exports co.edu.poli.Examen2.vista;
}
