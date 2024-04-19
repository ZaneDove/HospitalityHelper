package com.example.hospitalityhelper;

import java.time.LocalDate;


public class DailySalesData {
    public Sale[] sales;
    public double salesTotal;
    public LocalDate date;

    public DailySalesData(Sale[] sales, LocalDate date) {
        this.sales = sales;
        setSalesTotal();
        this.date = date;
    }

    public void setSalesTotal() {
        this.salesTotal = 0;
        //works out total of Sale
        //for each item in items
        for (Sale sale : sales) {
            //add price to total price

            salesTotal = salesTotal + sale.getPriceTotal();
        }
        this.salesTotal = Math.round(salesTotal * 100.0) / 100.0;
    }

    public double getSalesTotal() {
        setSalesTotal();
        return salesTotal;
    }

    public Sale[] getSales() {
        return sales;
    }

    public void setSales(Sale[] sales) {
        this.sales = sales;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
