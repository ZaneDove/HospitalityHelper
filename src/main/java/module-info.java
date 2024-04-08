module com.example.hospitalityhelper {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.hospitalityhelper to javafx.fxml;
    exports com.example.hospitalityhelper;
}