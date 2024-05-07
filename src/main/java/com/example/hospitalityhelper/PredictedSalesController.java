package com.example.hospitalityhelper;

import com.google.gson.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.HashMap;
import java.util.Map;


public class PredictedSalesController extends FXController {
    @FXML
    TableView salesDataTable;
    @FXML
    TableColumn dateCol, totalCol;
    @FXML
    TextField dateText;
    @FXML
    Button submitBtn;
    @FXML
    Text predictedSalesDate, predictedSalesTotal, weatherText, labourText;
    @FXML
    LineChart<String, Integer> lineChart;

    @FXML
    NumberAxis salesAxis;
    @FXML
    CategoryAxis hoursAxis;

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd:MM:yyyy");

    Map<LocalDate, DailySalesData> dailySalesDataMap = new HashMap<>();

    @Override
    public void initialize() throws SQLException {
        connectToDatabase();
        getSalesData();
    }


    public void getSalesData() throws SQLException {
        String sql = "SELECT `json` FROM `dailysalesdata`";
        ResultSet data = con.createStatement().executeQuery(sql);
        ObservableList<DailySalesData> dailySalesDataList = FXCollections.observableArrayList();
        //inni gson
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
        while (data.next()) {
            String json = data.getString("json");
            DailySalesData tempSalesData = gson.fromJson(json, DailySalesData.class);
            LocalDate date = tempSalesData.getDate();
            date.format(dateFormatter);
            dailySalesDataMap.put(date, tempSalesData);
            dailySalesDataList.add(tempSalesData);
        }

        dateCol.setCellValueFactory(new PropertyValueFactory<>("Date"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("salesTotal"));
        salesDataTable.setItems(dailySalesDataList);
    }

    public void onButtonPress() throws Exception {
        LocalDate dateToPredict = null;

        try {
            dateToPredict = LocalDate.parse(dateText.getText(), dateFormatter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (dateToPredict != null) {
            //figure out dates using WeekFields
            //Create WeekFields
            WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 1);
            //create temporal field for week of year
            TemporalField weekOfYearField = weekFields.weekOfYear();
            //weekOfYear = date.get WeekOfYear
            int weekOfYear = dateToPredict.get(weekOfYearField);
            //Create temporal field for day of week
            TemporalField dayOfWeekField = weekFields.dayOfWeek();
            int dayOfWeek = dateToPredict.get(dayOfWeekField);
            //Get date for this week this day last year
            LocalDate lastYear = LocalDate.now().with(WeekFields.ISO.weekBasedYear(), dateToPredict.getYear() - 1).with(WeekFields.ISO.weekOfYear(), weekOfYear).with(WeekFields.ISO.dayOfWeek(), dayOfWeek);
            //get date this day last week
            LocalDate lastWeek = LocalDate.now().with(WeekFields.ISO.weekBasedYear(), dateToPredict.getYear()).with(WeekFields.ISO.weekOfYear(), weekOfYear - 1).with(WeekFields.ISO.dayOfWeek(), dayOfWeek);
            //get date last week last year
            LocalDate lastWeekLastYear = LocalDate.now().with(WeekFields.ISO.weekBasedYear(), dateToPredict.getYear() - 1).with(WeekFields.ISO.weekOfYear(), weekOfYear - 1).with(WeekFields.ISO.dayOfWeek(), dayOfWeek);
            System.out.println("predicted date " + dateToPredict + '\n' + "last year " + lastYear + '\n' + "last week " + lastWeek + '\n' + "last week last year " + lastWeekLastYear);

            //Get boolean to check sales
            boolean checkHasData = dailySalesDataMap.containsKey(lastWeek) && dailySalesDataMap.containsKey(lastYear) && dailySalesDataMap.containsKey(lastWeekLastYear);
            if (checkHasData) {
                compareSalesData(dateToPredict, lastWeek, lastYear, lastWeekLastYear);
            } else {
                String errorString = "Missing sales data" + '\n' + lastYear + " " + dailySalesDataMap.containsKey(lastYear) + '\n' + lastWeek + " " + dailySalesDataMap.containsKey(lastWeek) + '\n' + lastWeekLastYear + " " + dailySalesDataMap.containsKey(lastWeekLastYear);
                System.out.println(errorString);
                showPopup(errorString);

            }
        }
        setWeather();

    }

    private void showPopup(String string) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(salesDataTable.getScene().getWindow());
        VBox dialogVBox = new VBox(20);
        dialogVBox.getChildren().add(new Text(string));
        Scene dialogScene = new Scene(dialogVBox, 100, 100);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    public void compareSalesData(LocalDate dateToPredict, LocalDate lastWeek, LocalDate lastYear, LocalDate lastYearLastWeek) throws Exception {
        //get sales data

        DailySalesData lastWeekSalesData = dailySalesDataMap.get(lastWeek);
        DailySalesData lastYearSalesData = dailySalesDataMap.get(lastYear);
        DailySalesData lastWeekLastYearSalesData = dailySalesDataMap.get(lastYearLastWeek);
        //Check if sales data for these dates are not null
        if (lastWeekSalesData == null || lastYearSalesData == null || lastWeekLastYearSalesData == null) {
            System.out.println("Missing sales data for some of the requested dates. Calculation cannot be done.");
            return;
        }
        //compare last weeks and last week last year's difference
        double lastWeeksTotal = lastWeekSalesData.getSalesTotal();
        double lastWeeksLastYearsTotal = lastWeekLastYearSalesData.getSalesTotal();
        double lastYearsTotal = lastYearSalesData.getSalesTotal();
        double difference = lastWeeksTotal - lastWeeksLastYearsTotal;
        double lastWeeksPercentageDifference = (difference / lastWeeksLastYearsTotal) * 100;
        //if lastWeeksDifference = 0 predictedTotals = lastYearsSales
        double predictedTotal = lastYearsTotal;
        if (lastWeeksPercentageDifference != 0) {
            predictedTotal = lastYearsTotal + (lastYearsTotal / lastWeeksPercentageDifference);
        }
        //work out sales per hour
        Sale[] sales = lastYearSalesData.getSales();
        double totalSales = 0;
        //create hashmap for sales per hour
        Map<Integer, Double> salesPerHour = new HashMap<>();
        //create 24 cells in hash map
        for (int i = 0; i < 24; i++) {
            salesPerHour.put(i + 1, 0.0);
        }
        //run through sales hashmap(sales.get(hour), +1)
        for (Sale sale : sales) {
            //get sales time
            LocalTime tempTime = sale.getTime();
            int tempHour = tempTime.getHour();
            Double previousValue = salesPerHour.get(tempHour);
            // if sales not null tempHour = previousValue + 1
            if (previousValue != null) {
                salesPerHour.put(tempHour, previousValue + 1);
                System.out.println(tempHour + "+");
            } else {
                //else hour = 1
                salesPerHour.put(tempHour, 1.0);
                System.out.println(tempHour + "+");
            }
        }
        //loop through 24 hours
        if (lastWeeksPercentageDifference != 0) {
            for (int i = 0; i < 24; i++) {
                double tempSales = salesPerHour.get(i + 1);
                System.out.println(i + 1 + " " + tempSales);
                //add predicted differences
                tempSales = tempSales + (tempSales / lastWeeksPercentageDifference);
                //round up
                tempSales = Math.round(tempSales);
                //set new data
                salesPerHour.replace(i, tempSales);
            }
        }

        EstimatedSalesData estimatedSalesData = new EstimatedSalesData(salesPerHour, predictedTotal, dateToPredict);
        predictedSalesDate.setText("Predicted sales date: " + estimatedSalesData.getDate());
        predictedSalesTotal.setText("Predicted sales total: " + estimatedSalesData.getSalesTotals());
        System.out.println(estimatedSalesData.getSalesTotals());
        XYChart.Series dataSeries1 = new XYChart.Series();
        dataSeries1.setName("sales");


        //set data for line chart

        for (Map.Entry<Integer, Double> entry : salesPerHour.entrySet()) {
            int key = entry.getKey();
            String hour = String.valueOf(key);
            int tempSales = entry.getValue().intValue();
            dataSeries1.getData().add(new XYChart.Data(hour, tempSales));
        }
        lineChart.getData().add(dataSeries1);
    }
        //get weather data and set text
        public void setWeather() throws Exception {
            JSONObject json = readJsonFromUrl();
            JSONObject list = json.getJSONObject("main");
            JSONArray weather = json.getJSONArray("weather");
            JSONObject todayWeather = weather.getJSONObject(0);
            String weatherString = todayWeather.getString("main");
            String text;
            if (weatherString == "Rain"){
                text = "It looks like rain tomorrow. expect to be busy on delivery but slower in house. Maybe sent a FOH member home? or get some extra cleaning done ";
            } else if (weatherString == "Clear") {
                text = "It looks like a clear day tomorrow. expect business as normal";
            }else{
                text = "The weather is looking uncertain tomorrow. keep an eye out for rain";
            }

            weatherText.setText(text);
            labourText.setText("It looks like your busiest time is around 12 and 3, prioritize these hours when scheduling labour. ");
        }
    }

