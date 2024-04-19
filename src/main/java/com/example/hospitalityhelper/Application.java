package com.example.hospitalityhelper;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {

    private Stage stage;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("index.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 651, 414);
        stage.setTitle("Hospitality Helper");
        stage.setScene(scene);
        stage.show();

    }


}