package com.example.hospitalityhelper;

import com.google.gson.*;
import com.opencsv.CSVReader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;


public class SaleDataController extends FXController {
    @FXML
    Button chooseFileBtn, submitBtn;
    @FXML
    TextField dateOfSaleTxt;
    @FXML
    TableView salesDataTable;
    @FXML
    TableColumn dateCol, totalCol;

    Map<String, MenuItem> menuItemMap = new HashMap<>();
    ArrayList<Sale> salesList = new ArrayList<>();
    ObservableList<DailySalesData> dailySalesDataList = FXCollections.observableArrayList();

    @Override
    public void initialize() throws SQLException {
        connectToDatabase();
        getMenuItems();
        getSalesData();

    }

    @FXML
    public void chooseFileBtnOnClick() throws SQLException, IOException {
        openFile();
    }

    public void getSalesData() throws SQLException {
        String sql = "SELECT `json` FROM `dailysalesdata`";
        ResultSet data = con.createStatement().executeQuery(sql);

        //inni gson
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new JsonSerializer<LocalDate>() {
                    @Override
                    public JsonElement serialize(LocalDate localDate, Type type, JsonSerializationContext jsonSerializationContext) {
                        return new JsonPrimitive(localDate.toString());
                    }
                })
                .registerTypeAdapter(LocalTime.class, new JsonSerializer<LocalTime>() {
                    @Override
                    public JsonElement serialize(LocalTime localTime, Type type, JsonSerializationContext jsonSerializationContext) {
                        return new JsonPrimitive(localTime.toString());
                    }
                })
                .registerTypeAdapter(LocalTime.class, new JsonDeserializer<LocalTime>() {
                    @Override
                    public LocalTime deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                        return LocalTime.parse(json.getAsString());
                    }
                })
                .registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
                    @Override
                    public LocalDate deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                        return LocalDate.parse(json.getAsString());
                    }
                })
                .create();
        while (data.next()) {
            String json = data.getString("json");
            DailySalesData tempSalesData = gson.fromJson(json, DailySalesData.class);
            dailySalesDataList.add(tempSalesData);
        }

        dateCol.setCellValueFactory(new PropertyValueFactory<>("Date"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("salesTotal"));
        salesDataTable.setItems(dailySalesDataList);
    }

    private void openFile() throws IOException, SQLException {


        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try {
                //create file reader
                FileReader fileReader = new FileReader(file);
                //create csvReader object passing
                //file reader as a parameter
                CSVReader csvReader = new CSVReader(fileReader);
                String[] nextRecord;
                while ((nextRecord = csvReader.readNext()) != null) {
                    for (String cell : nextRecord) {
                        String[] data = cell.split(",");
                        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
                        ArrayList<MenuItem> menuItemList = new ArrayList<>();
                        LocalTime time = LocalTime.parse(data[1], timeFormatter);
                        for (int i = 0; i < parseInt(data[2]); i++) {
                            MenuItem menuItem = menuItemMap.get("burger");
                            menuItemList.add(menuItem);
                        }
                        for (int i = 0; i < parseInt(data[3]); i++) {
                            MenuItem menuItem = menuItemMap.get("fries");
                            menuItemList.add(menuItem);
                        }
                        for (int i = 0; i < parseInt(data[4]); i++) {
                            MenuItem menuItem = menuItemMap.get("coke");
                            menuItemList.add(menuItem);
                        }
                        Sale tempSale = new Sale(time, menuItemList.toArray(new MenuItem[0]));
                        salesList.add(tempSale);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void getMenuItems() throws SQLException {
        String sql = "SELECT  `Name`, `Price`, `UsedStock` FROM `menuitem`";
        ResultSet data = con.createStatement().executeQuery(sql);
        while (data.next()) {
            String json = data.getString("UsedStock");
            Gson gson = new Gson();
            UsedStockItem[] stockItems = gson.fromJson(json, UsedStockItem[].class);
            String name = data.getString("Name");
            double price = data.getDouble("Price");
            String stockName = data.getString("UsedStock");
            menuItemMap.put(name.toLowerCase(), new MenuItem(name, price, stockItems));
        }
    }


    @FXML
    private void onSumbitPressed() {
        try {
            DateTimeFormatter parser = DateTimeFormatter.ofPattern("dd:MM:yyyy");
            LocalDate date = LocalDate.parse(dateOfSaleTxt.getText(), parser);
            Sale[] arr = new Sale[salesList.size()];
            //create Gson object
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
            String tempJson = gson.toJson(new DailySalesData(salesList.toArray(arr), date));

            String SQL_INSERT = "INSERT INTO `dailysalesdata`( `date`, `json`) VALUES (?,?)";
            try {
                PreparedStatement pstmt = con.prepareStatement(SQL_INSERT);
                pstmt.setDate(1, Date.valueOf(date));
                String json = String.valueOf(tempJson);
                pstmt.setString(2, json);

                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Throwable cause = ex.getCause();
            if (cause != null) {
                cause.printStackTrace();
            }
        }
    }
}