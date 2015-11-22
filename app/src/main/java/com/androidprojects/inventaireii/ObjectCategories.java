package com.androidprojects.inventaireii;

public class ObjectCategories {

    private int id;
    private String color;
    private String name;
    private String inventoryState;


    public ObjectCategories() {
    }

    //TODO remove
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

    public void setColor(String color) {
        this.color = color;
    }

    public String getInventoryState() {
        return inventoryState;
    }

    public void setInventoryState(String inventoryState) {
        this.inventoryState = inventoryState;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
