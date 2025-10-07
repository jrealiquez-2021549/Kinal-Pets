
module Veterinaria2025BM {
    requires javafx.swt;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.media;
    requires javafx.swing;
    requires javafx.web;
    requires mysql.connector.java;
    requires java.sql;
    
    opens org.juliorealiquez.controller to javafx.fxml;
    opens org.juliorealiquez.system to javafx.fxml;
    opens org.juliorealiquez.models to javafx.base;
    exports org.juliorealiquez.system to javafx.graphics;  // Esto es lo que debes agregar
}