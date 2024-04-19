package com.example.hospitalityhelper;

import java.time.LocalDate;
import java.util.Map;

public class EstimatedSalesData {
    Map<Integer, Double> salesPerHours;
    double salesTotals;
    LocalDate date;

    public EstimatedSalesData(Map<Integer, Double> salesPerHours, double salesTotals, LocalDate date) {
        this.salesPerHours = salesPerHours;
        this.salesTotals = salesTotals;
        this.date = date;
    }

    public Map<Integer, Double> getSalesPerHours() {
        return salesPerHours;
    }

    public double getSalesTotals() {
        return salesTotals;
    }

    public LocalDate getDate() {
        return date;
    }

}
