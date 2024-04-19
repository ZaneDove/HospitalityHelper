package com.example.hospitalityhelper;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class MenuItemController extends FXController {
    UsedStockItem[] usedStockItems = {};
    @FXML
    TableColumn amountCol, measuredInCol, nameItemCol;
    @FXML
    ChoiceBox stockBox;
    @FXML
    TextField measuredInTxt;
    @FXML
    TextField amountInput;
    @FXML
    Button addStockToMenuItem;
    @FXML
    TableView usedStockTable;
    @FXML
    TextField menuItemName, price;
    @FXML
    TableView menuItemTable;
    @FXML
    TableColumn itemNameCol, itemPriceCol;

    @Override
    @FXML
    public void initialize() throws SQLException {

        choiceBoxStock();
        menuItemTableData();
        menuItemTableData();
    }

    public void choiceBoxStock() {
        connectToDatabase();
        //get stock
        String sql = "SELECT  `Name`, `MeasuredIn` FROM `stockItem`";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                //set values
                stockBox.getItems().add(rs.getString("Name"));
                measuredInTxt.setText(rs.getString("MeasuredIn"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void addUsedStockClicked() {
        connectToDatabase();
        String selectedStock = (String) stockBox.getValue();
        int amount = 0;
        try {
            amount = Integer.parseInt(amountInput.getText());
        } catch (NumberFormatException e) {
            System.err.println("Invalid format for the amount, please enter an integer value: " + e.getMessage());
            return;
        }
        String measurement = measuredInTxt.getText();
        UsedStockItem usedStockItem = new UsedStockItem(selectedStock, measurement, amount);
        usedStockItems = Arrays.copyOf(usedStockItems, usedStockItems.length + 1);
        usedStockItems[usedStockItems.length - 1] = usedStockItem;
        usedStockTable.getItems().add(usedStockItem);
        //add values to columns
        nameItemCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        measuredInCol.setCellValueFactory(new PropertyValueFactory<>("MeasuredIn"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("Amount"));

    }

    @FXML
    protected void onAddMenuItemClicked() {
        connectToDatabase();
        ObjectMapper mapper = new ObjectMapper();
        String usedStockItemsJson;
        try {
            usedStockItemsJson = mapper.writeValueAsString(usedStockItems);
        } catch (Exception e) {
            throw new RuntimeException("Error converting UsedStockItems to JSON", e);
        }
        String SQL_INSERT = "INSERT INTO `menuitem`(`Name`, `Price`, `UsedStock`) VALUES (?,?,?)";
        try {
            PreparedStatement pstmt = con.prepareStatement(SQL_INSERT);
            pstmt.setString(1, menuItemName.getText());
            pstmt.setDouble(2, Double.parseDouble(price.getText()));
            pstmt.setString(3, usedStockItemsJson);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        stockBox.getItems().clear();
        amountInput.clear();
        usedStockTable.getItems().clear();
    }

    public void menuItemTableData() throws SQLException {
        String sql = "SELECT  `Name`, `Price`  FROM `menuitem`";
        ResultSet data = con.createStatement().executeQuery(sql);

        ObservableList<MenuItem> stockList = FXCollections.observableArrayList();

        while (data.next()) {
            String name = data.getString("Name");
            double price = data.getDouble("Price");
            UsedStockItem i = new UsedStockItem("stock", "G", 100);
            UsedStockItem[] list = {i};
            stockList.add(new MenuItem(name, price, list));
        }

        itemNameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        itemPriceCol.setCellValueFactory(new PropertyValueFactory<>("Price"));
        menuItemTable.setItems(stockList);


    }


}