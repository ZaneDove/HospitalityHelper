package com.example.hospitalityhelper;


import java.util.ArrayList;
import java.util.List;

public class StockTake {
    List<UsedStockItem> items = new ArrayList<UsedStockItem>();

    public StockTake(List<UsedStockItem> items) {
        this.items = items;
    }

    public List<UsedStockItem> getItems() {
        return items;
    }

    public void setItems(List<UsedStockItem> items) {
        this.items = items;
    }

    public UsedStockItem addItems(UsedStockItem item) {
        this.items.add(item);
        return item;
    }


}
