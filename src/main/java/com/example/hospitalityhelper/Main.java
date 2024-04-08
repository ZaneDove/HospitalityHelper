package com.example.hospitalityhelper;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Label label = new Label("Hello, JavaFX!");
                StackPane root = new StackPane(label);
                Scene scene = new Scene(root, 300, 200);
                primaryStage.setTitle("JavaFX Window");
                primaryStage.setScene(scene);
                primaryStage.show();
            }
        });
    }
}