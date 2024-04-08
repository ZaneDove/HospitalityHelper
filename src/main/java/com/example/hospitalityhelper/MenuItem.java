package com.example.hospitalityhelper;

public class MenuItem {
    double price;
    UsedStockItem[] usedStock;
    String name;

    public MenuItem(String name, Double price, UsedStockItem[] usedStock) {
        this.price = price;
        this.usedStock = usedStock;
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public UsedStockItem[] getStockUsed() {
        return usedStock;
    }

    public void setStockUsed(UsedStockItem[] stockUsed) {
        this.usedStock = stockUsed;
    }
}
