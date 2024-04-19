package com.example.hospitalityhelper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StockItemController extends FXController {
    @FXML
    TextField stockName;
    @FXML
    ChoiceBox<String> measuredIn = new ChoiceBox<>();
    @FXML
    TableView stockTable;
    @FXML
    TableColumn menuItemCol, measuredInCol;

    @Override
    public void initialize() throws SQLException {
        //connect to database
        connectToDatabase();
        choiceBoxMeasuredIn();
        stockTableData();

    }


    public void choiceBoxMeasuredIn() {
        measuredIn.setItems(FXCollections.observableArrayList("G", "KG", "L", "ML"));
    }

    @FXML
    protected void onCreateStockItemClicked() {
        String name = stockName.getText();
        String measure = measuredIn.getValue();
        StockItem stockItem = new StockItem(name, measure);
        insertStockIntoDatabase(stockItem);

    }

    private void insertStockIntoDatabase(StockItem item) {

        String SQL_INSERT = "INSERT INTO `stockitem`(`Name`, `MeasuredIn`) VALUES (?,?)";

        try (
                PreparedStatement preparedStatement = con.prepareStatement(SQL_INSERT)) {

            preparedStatement.setString(1, item.getName());
            preparedStatement.setString(2, item.getMeasuredIn());

            int row = preparedStatement.executeUpdate();

            // rows affected
            System.out.println(row); // You can remove this line if you want to

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stockTableData() throws SQLException {
        String sql = "SELECT  `Name`, `MeasuredIn` FROM `stockitem`";
        ResultSet data = con.createStatement().executeQuery(sql);

        ObservableList<StockItem> stockList = FXCollections.observableArrayList();
        while (data.next()) {
            String name = data.getString("Name");
            String measuredIn = data.getString("MeasuredIn");
            stockList.add(new StockItem(name, measuredIn));
        }

        menuItemCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        measuredInCol.setCellValueFactory(new PropertyValueFactory<>("MeasuredIn"));
        stockTable.setItems(stockList);
    }
}
