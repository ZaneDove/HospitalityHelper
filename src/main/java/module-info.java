module com.example.hospitalityhelper {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.fasterxml.jackson.databind;
    requires org.json;
    requires com.google.gson;
    requires opencsv;
    requires java.net.http;


    opens com.example.hospitalityhelper to javafx.fxml;
    exports com.example.hospitalityhelper;
}