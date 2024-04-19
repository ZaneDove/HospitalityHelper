package com.example.hospitalityhelper;

import com.google.gson.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class OrderSheetController extends FXController {
    //create FXML elements
    @FXML
    ChoiceBox stockBox;
    @FXML
    Text measuredInTxt;
    @FXML
    TextField amountTxtField;
    @FXML
    Button generateBtn, addBtn;
    @FXML
    TableView stockTakeTable, estimatedStockTakeTable;
    // st = stock take table, est = estimated stock take table
    @FXML
    TableColumn stStockCol, stAmountCol, estStockCol, estAmountCol, estMeasurementCol;
    //date formatter

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd:MM:yyyy");
    //observable list UsedStockItem for stockTake table
    ObservableList<UsedStockItem> stockTake = FXCollections.observableArrayList();
    //HashMap dailySalesData
    Map<LocalDate, DailySalesData> dailySalesDataMap = new HashMap<>();
    //observableList estimatedOrderSheet for estimated order table

    ObservableList<UsedStockItem> estimatedOrderSheet = FXCollections.observableArrayList();
    //create daily sales data list
    ObservableList<DailySalesData> dailySalesDataList = FXCollections.observableArrayList();

    //initalize
    @Override
    public void initialize() throws SQLException {
        //connect to database
        connectToDatabase();
        //Fill Choice box
        choiceBoxStock();
        //get daily sales data
        getSalesData();
        //set stockTakeTable values
        stStockCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        stAmountCol.setCellValueFactory(new PropertyValueFactory<>("Amount"));
    }

    //on add bttn press
    @FXML
    public void onAddBtnPress() {
        //selectedAmount init set to 0
        double selectedAmount = 0.0;
        //try selected amount = text Field getText() else print error + show pop up
        try {
            selectedAmount = Double.parseDouble(amountTxtField.getText());
        } catch (Exception e) {
            e.printStackTrace();
            showPopup("Amount must be a whole number");

        }
        //Of selected amount > 0 check
        if (selectedAmount > 0) {
            //init stock string
            String stock = null;
            //try stockBox.getValue else print error + popup box
            try {
                stock = (String) stockBox.getValue();
            } catch (Exception e) {
                e.printStackTrace();
                showPopup("Please select a stock item");
            }
            //if stock not null set measuredInTxt + update Stock
            if (stock != null) {
                String measurement = measuredInTxt.getText();
                updateOrAddStock(new UsedStockItem(stock, measurement, selectedAmount));
            }

        }
    }


    public void choiceBoxStock() {
        //connectToDatabase
        connectToDatabase();
        //get stock
        String sql = "SELECT  `Name`, `MeasuredIn` FROM `stockItem`";
        //try get data from MySQL else show error + pop up
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

    //function updated or add stock
    private void updateOrAddStock(UsedStockItem itemToCompare) {
        //get variables from usedStockItem
        String stockName = itemToCompare.getName();
        double newAmount = itemToCompare.getAmount();
        String measurement = itemToCompare.getMeasuredIn();
        //boolean item found = false
        boolean found = false;
        Iterator<UsedStockItem> iterator = stockTake.iterator();
        //loop through stock take
        while (iterator.hasNext()) {
            UsedStockItem stockItem = iterator.next();
            //if stockItem is in list update amount
            if (stockItem.getName().equalsIgnoreCase(stockName)) {
                double totalAmount = stockItem.getAmount() + newAmount;
                stockItem.setAmount(totalAmount);
                found = true;
                iterator.remove();
                stockTake.add(stockItem);
                break;
            }
        }
        //If not found add stock item
        if (!found) {
            stockTake.add(itemToCompare);
        }
        //add to table
        stockTakeTable.setItems(stockTake);
    }

    //Pop up
    private void showPopup(String string) {
        //create dialog box
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(addBtn.getScene().getWindow());
        VBox dialogVBox = new VBox(20);
        //add string to dialog box
        dialogVBox.getChildren().add(new Text(string));
        Scene dialogScene = new Scene(dialogVBox, 100, 100);
        //set scene
        dialog.setScene(dialogScene);
        //show pop up box
        dialog.show();
    }

    //generate stock function
    @FXML
    private void genrateStockBtnPressed() {
        //needed stock = hashmap
        Map<String, Double> neededStock = new HashMap<>();
        //localDate now = now
        LocalDate now = LocalDate.now();
        //int weekOfYear, dayOfWeek = now.get(weekFields week/day of year/week)
        int weekOfYear = now.get(WeekFields.ISO.weekOfYear());
        int dayOfWeek = now.get(WeekFields.ISO.dayOfWeek());
        //local date last week = now -1 week
        LocalDate lastWeek = LocalDate.now().with(WeekFields.ISO.weekBasedYear(), LocalDate.now().getYear()).with(WeekFields.ISO.weekOfYear(), weekOfYear - 1).with(WeekFields.ISO.dayOfWeek(), dayOfWeek);
        //daily sales = null
        DailySalesData salesData = null;
        //if dailySales.hasKey sales data = get(lastWeek)
        if (dailySalesDataMap.containsKey(lastWeek)) {
            salesData = dailySalesDataMap.get(lastWeek);
        } else {
            //else show popup "need last week's data"
            showPopup("System does not have needed sales data " + lastWeek);
        }
        //if sales not null
        if (salesData != null) {
            //sale[] tempSales
            Sale[] tempSales = salesData.getSales();
            //Loop through sales
            for (Sale item : tempSales) {
                MenuItem[] items = item.getItems();
                //loop through menu items in sales
                for (MenuItem item1 : items) {
                    UsedStockItem[] usedStockItems = item1.getStockUsed();
                    //loop Through UsedStockItems
                    for (UsedStockItem usedStockItem : usedStockItems) {
                        //if neededStock.hasKey neededStock(key) = amount + new amount
                        if (neededStock.containsKey(usedStockItem.getName())) {
                            double newAmount = neededStock.get(usedStockItem.getName()) + usedStockItem.getAmount();
                            neededStock.put(usedStockItem.getName(), newAmount);
                        } else {
                            //else neededStock.put(stock)
                            neededStock.put(usedStockItem.getName(), usedStockItem.getAmount());
                        }
                    }
                }
            }
            //Loop through neededStock
            for (Map.Entry<String, Double> set : neededStock.entrySet()) {
                //loop through stockTake
                for (UsedStockItem item : stockTake) {
                    //if neededStock.has Used Stock + amount
                    if (set.getKey().equals(item.getName())) {
                        double newAmount = set.getValue() - item.getAmount();
                        estimatedOrderSheet.add(new UsedStockItem(item.getName(), item.getMeasuredIn(), newAmount));
                        break;
                    }
                    //get measurements from sql
                    //sql string
                    String sql = "SELECT  `Name`, `MeasuredIn` FROM `stockItem`";
                    //measuredIn map <name, measurement>
                    Map<String, String> measuredInList = new HashMap<>();
                    try {
                        //get data
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(sql);
                        //loop through data
                        while (rs.next()) {
                            //set values
                            measuredInList.put(rs.getString("Name"), rs.getString("MeasuredIn"));
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    String measuredIn = measuredInList.get(set.getKey());
                    estimatedOrderSheet.add(new UsedStockItem((set.getKey()), measuredIn, set.getValue()));
                }
            }
            fillEstimatedStockTakeTable();
        }
    }

    //fill estimated stock table functions.
    public void fillEstimatedStockTakeTable() {
        estStockCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        estAmountCol.setCellValueFactory(new PropertyValueFactory<>("Amount"));
        estMeasurementCol.setCellValueFactory(new PropertyValueFactory<>("measuredIn"));
        estimatedStockTakeTable.setItems(estimatedOrderSheet);
    }

    //get sales data
    public void getSalesData() throws SQLException {
        //get json string
        String sql = "SELECT `json` FROM `dailysalesdata`";
        ResultSet data = con.createStatement().executeQuery(sql);

        //inni gson builder
        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new JsonSerializer<LocalDate>() {
            @Override
            public JsonElement serialize(LocalDate localDate, Type type, JsonSerializationContext jsonSerializationContext) {
                return new JsonPrimitive(localDate.toString());
            }
        }).registerTypeAdapter(LocalTime.class, new JsonSerializer<LocalTime>() {
            @Override
            public JsonElement serialize(LocalTime localTime, Type type, JsonSerializationContext jsonSerializationContext) {
                return new JsonPrimitive(localTime.toString());
            }
        }).registerTypeAdapter(LocalTime.class, new JsonDeserializer<LocalTime>() {
            @Override
            public LocalTime deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                return LocalTime.parse(json.getAsString());
            }
        }).registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
            @Override
            public LocalDate deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                return LocalDate.parse(json.getAsString());
            }
        }).create();
        //while data has next dailySalesDataMap.put(date, salesData)
        while (data.next()) {
            String json = data.getString("json");
            DailySalesData tempSalesData = gson.fromJson(json, DailySalesData.class);
            LocalDate date = tempSalesData.getDate();
            date.format(dateFormatter);
            dailySalesDataMap.put(date, tempSalesData);
            dailySalesDataList.add(tempSalesData);
        }

    }
}
