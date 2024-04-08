package com.example.hospitalityhelper;

import java.time.LocalDate;

public class EstimatedSalesData {
    int[] salesPerHours;
    double salesTotals;
    LocalDate date;

    public EstimatedSalesData(int[] salesPerHours, double salesTotals, LocalDate date) {
        this.salesPerHours = salesPerHours;
        this.salesTotals = salesTotals;
        this.date = date;
    }

    public int[] getSalesPerHours() {
        return salesPerHours;
    }

    public double getSalesTotals() {
        return salesTotals;
    }

    public LocalDate getDate() {
        return date;
    }

}
