package com.example.hospitalityhelper;

import java.time.LocalDate;


public class DailySalesData {
    Sale[] sales;
    double salesTotal;
    LocalDate date;

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
