package com.androidprojects.inventaireii;

/**
 * Created by Administrateur on 25.10.2015.
 */
public class ObjectProducts {

    private String artNb;
    private String name;
    // TODO private String description;
    private int quantity;
    private double price;
    private String inventoryState; // TODO ...
    private ObjectCategories category;
    // TODO private ObjectWarehouses[] warehouses;


    public ObjectProducts(String artNb, String name, ObjectCategories category,
                          int quantity, double price, String inventoryState) {
        this.artNb = artNb;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
        this.inventoryState = inventoryState;
    }

    public String getArtNb() {
        return artNb;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
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
}
