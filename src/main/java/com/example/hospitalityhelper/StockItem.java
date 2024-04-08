package com.example.hospitalityhelper;

public class StockItem {
    public String name;
    public String measuredIn;

    public StockItem(String name, String measuredIn) {
        this.name = name;
        this.measuredIn = measuredIn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeasuredIn() {
        return measuredIn;
    }

    public void setMeasuredIn(String measuredIn) {
        this.measuredIn = measuredIn;
    }

    @Override
    public String toString() {
        return "StockItem{" +
                "name='" + name + '\'' +
                ", measuredIn='" + measuredIn + '\'' +
                '}';
    }
}
