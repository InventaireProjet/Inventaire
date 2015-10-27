package com.androidprojects.inventaireii;

public class ObjectCategories {


    private  int id;
    private String color;
    private String name;
    private String inventoryState;

    public ObjectCategories (String color, String name, String inventoryState) {

        this.color = color;
        this.name = name;

        this.inventoryState = inventoryState;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
