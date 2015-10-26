package com.androidprojects.inventaireii;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Administrateur on 25.10.2015.
 */
public class ObjectProducts {

    private String artNb;
    private String name;
    private String description;
    private double price;
    private String inventoryState; // TODO ...
    private ObjectCategories category;
    private ArrayList<ObjectStock> stocks = new ArrayList<ObjectStock>();


    public ObjectProducts(String artNb, String name, ObjectCategories category,
                          int quantity, double price, String inventoryState) {
        this.artNb = artNb;
        this.name = name;
        this.category = category;
        this.price = price;
        this.inventoryState = inventoryState;
    }

    public String getArtNb() {
        return artNb;
    }

    public String getName() {
        return name;
    }


    public double getPrice() {
        return price;
    }

    public ObjectCategories getCategory() {
        return category;
    }

    public String getInventoryState() {
        return inventoryState;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addStock(ObjectStock stock) {
        stocks.add(stock);
    }

    public int getQuantity() {
        int quantity = 0;
        for (ObjectStock stock : stocks) {
            quantity += stock.getQuantity();
        }

        // TODO suppress those lines...
        if (quantity == 0) {
            quantity = 12;
        }

        return quantity;
    }

}
