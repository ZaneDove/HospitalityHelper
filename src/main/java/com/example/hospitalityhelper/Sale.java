package com.example.hospitalityhelper;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class Sale {
    public LocalTime time;
    public MenuItem[] items;
    public double priceTotal;

    public Sale(LocalTime time, MenuItem[] items) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        this.time = LocalTime.parse(time.format(dtf));
        this.items = items;
        setPriceTotal();
    }

    public LocalTime getTime() {
        return time;
    }

    public MenuItem[] getItems() {
        return items;
    }

    public double getPriceTotal() {
        return priceTotal;
    }

    void setPriceTotal() {
        this.priceTotal = 0;
        for (MenuItem item : items) {
            this.priceTotal = this.priceTotal + item.getPrice();
        }
        this.priceTotal = Math.round(priceTotal * 100.0) / 100.0;
    }

    // remove setPriceTotal method
}
