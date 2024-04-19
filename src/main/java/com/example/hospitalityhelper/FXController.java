package com.example.hospitalityhelper;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class FXController {
    Connection con;
    //Navigation buttons
    @FXML
    Button homeBtn, menuBtn, stockBtn, salesBtn, predictedBtn, orderBtn;

    @FXML
    public void initialize() throws SQLException {
        //connect to database
        connectToDatabase();

    }

    //navigation button functionallity
    //AddMenuItem button
    @FXML
    protected void onAddMenuItemClicked() {
        //get fxml scene
        try {
            Parent root = FXMLLoader.load(getClass().getResource("AddMenuItem.fxml"));
            Stage window = (Stage) homeBtn.getScene().getWindow();
            window.setScene(new Scene(root, 651, 414));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //Stock Item Page

    //Home Button
    @FXML
    protected void onAddHomeClicked() {
        //get fxml scene
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Index.fxml"));
            Stage window = (Stage) homeBtn.getScene().getWindow();
            window.setScene(new Scene(root, 651, 414));
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
            window.setScene(new Scene(root, 651, 414));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //sales data button
    @FXML
    protected void onAddSaleDataClicked() {
        //get fxml scene
        try {
            Parent root = FXMLLoader.load(getClass().getResource("AddSaleData.fxml"));
            Stage window = (Stage) homeBtn.getScene().getWindow();
            window.setScene(new Scene(root, 651, 414));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //predicted sales button
    @FXML
    protected void onPredictedSalesClicked() {
        //get fxml scene
        try {
            Parent root = FXMLLoader.load(getClass().getResource("PredictedSalesData.fxml"));
            Stage window = (Stage) homeBtn.getScene().getWindow();
            window.setScene(new Scene(root, 651, 414));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onOrderSheetPressed() {
        //get fxml scene
        try {
            Parent root = FXMLLoader.load(getClass().getResource("OrderSheet.fxml"));
            Stage window = (Stage) homeBtn.getScene().getWindow();
            window.setScene(new Scene(root, 651, 414));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //stock item button

    protected void connectToDatabase() {

        try {

            String url = "jdbc:mysql://localhost:3306/hospohelper";
            String username = "root";
            String password = "";
            this.con = DriverManager.getConnection(url, username, password);
            System.out.println("connection made");
        } catch (Exception e) {
            System.out.println("can not connect to database /" + e);
        }
    }


}