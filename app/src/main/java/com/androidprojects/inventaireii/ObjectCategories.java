package com.androidprojects.inventaireii;

public class ObjectCategories {

    private String color;
    private String name;
    private String inventoryState;

    public ObjectCategories (String color, String name, String inventoryState) {

        this.color = color;
        this.name = name;

        this.inventoryState = inventoryState;

    }

    public String getColor() {
        return color;
    }

    public String getInventoryState() {
        return inventoryState;
    }

    public String getName() {
        return name;
    }
}
