package com.example.hospitalityhelper;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class HelloController {
    //Navigation buttons
    @FXML
    Button homeBtn, menuBtn, stockBtn, salesBtn, predictedBtn;

    @FXML
    protected void onAddMenuItemClicked() {
        //get fxml scene
        try {
            Parent root = FXMLLoader.load(getClass().getResource("AddMenuItem.fxml"));
            Stage window = (Stage) homeBtn.getScene().getWindow();
            window.setScene(new Scene(root, 600, 500));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onAddHomeClicked() {
        //get fxml scene
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Index.fxml"));
            Stage window = (Stage) homeBtn.getScene().getWindow();
            window.setScene(new Scene(root, 600, 500));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onAddSaleDataClicked() {
        //get fxml scene
        try {
            Parent root = FXMLLoader.load(getClass().getResource("AddSaleData.fxml"));
            Stage window = (Stage) homeBtn.getScene().getWindow();
            window.setScene(new Scene(root, 600, 500));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onPredictedSalesClicked() {
        //get fxml scene
        try {
            Parent root = FXMLLoader.load(getClass().getResource("PredictedSalesData.fxml"));
            Stage window = (Stage) homeBtn.getScene().getWindow();
            window.setScene(new Scene(root, 600, 500));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onStockItemClicked() {
        //get fxml scene
        try {
            Parent root = FXMLLoader.load(getClass().getResource("AddStockItem.fxml"));
            Stage window = (Stage) homeBtn.getScene().getWindow();
            window.setScene(new Scene(root, 600, 500));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}