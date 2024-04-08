package com.example.hospitalityhelper;

import java.time.LocalTime;


public class Sale {
    LocalTime time;
    MenuItem[] items;
    double priceTotal;

    public Sale(LocalTime time, MenuItem[] items) {
        this.time = time;
        this.items = items;
        setPriceTotal(100.0);
    }

    public double getPriceTotal() {
        return priceTotal;
    }

    void setPriceTotal(double v) {
        this.priceTotal = 0;
        for (MenuItem item : items) {
            this.priceTotal = this.priceTotal + item.getPrice();
        }
    }

    // remove setPriceTotal method
}
