package com.example.hospitalityhelper;

public class UsedStockItem extends StockItem {
    public double amount;

    public UsedStockItem(String name, String measuredIn, double amount) {
        super(name, measuredIn);
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
