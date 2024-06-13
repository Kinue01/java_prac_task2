module org.example.prac2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires java.sql;
    requires org.postgresql.jdbc;

    opens org.example.prac2 to javafx.fxml;
    exports org.example.prac2;
    exports org.example.prac2.controller;
    opens org.example.prac2.controller to javafx.fxml;
}