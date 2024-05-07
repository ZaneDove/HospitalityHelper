package com.example.hospitalityhelper;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;


public class FXController {
    Connection con;
    //Navigation buttons
    @FXML
    Button homeBtn, menuBtn, stockBtn, salesBtn, predictedBtn, orderBtn;

    @FXML
    public void initialize() throws Exception {
        //connect to database
        connectToDatabase();

    }

    //navigation button functionallity
    //AddMenuItem button
    @FXML
    protected void onAddMenuItemClicked() throws SQLException {
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
    //readAll for OpenWeather data
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
    //Get OpenWeather data
    public static JSONObject readJsonFromUrl() throws Exception {
        String city = "Bristol";
        final String API_KEY = "580b0cbd3b76c229d6b6221097fdc2fd";
        String apiUrl = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY;

        URL url = new URL(apiUrl);
        InputStream is = new URL(apiUrl).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }
}


