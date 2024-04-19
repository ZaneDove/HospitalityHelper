package com.example.hospitalityhelper;

public class MenuItem {
    public double price;
    public UsedStockItem[] usedStock;
    public String name;

    public MenuItem(String name, Double price, UsedStockItem[] usedStock) {
        this.price = price;
        this.usedStock = usedStock;
        this.name = name;
    }

    public UsedStockItem[] getUsedStock() {
        return usedStock;
    }

    public void setUsedStock(UsedStockItem[] usedStock) {
        this.usedStock = usedStock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
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
