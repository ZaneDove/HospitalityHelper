package com.example.hospitalityhelper;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;

public class IndexController extends FXController{
    @FXML
    Text weatherText;
    @Override
    public void initialize() throws Exception {
        JSONObject json = readJsonFromUrl();
        JSONObject list = json.getJSONObject("main");
        JSONArray weather = json.getJSONArray("weather");
        JSONObject todayWeather = weather.getJSONObject(0);
        String weatherString = todayWeather.getString("main");
        String text;
        if (weatherString == "Rain"){
            text = "It looks like rain today. expect to be busy on delivery but slower in house. Maybe sent a FOH member home? or get some extra cleaning done ";
        } else if (weatherString == "Clear") {
            text = "It looks like a clear day today. expect business as normal";
        }else{
            text = "The weather is looking uncertain today. keep an eye out for rain";
        }
        weatherText.setText(text);
    }
}
