package com.androidprojects.inventaireii;

public class ObjectWarehouse {

    private  int id;
    private String color;
    private String name;
    private int inventoriedObjects;
    private int numberObjects;
    private int stockCapacity;
    private String telNumber;
    private String street;
    private String streetNumber;
    private String postalCode;
    private String location;
    private String country;


    public ObjectWarehouse(String name, int inventoriedObjects, int numberObjects, int stockCapacity, String telNumber, String street, String streetNumber, String postalCode, String location, String country) {

        this.name = name;
        this.inventoriedObjects = inventoriedObjects;
        this.numberObjects = numberObjects;
        this.stockCapacity = stockCapacity;
        this.telNumber = telNumber;
        this.street = street;
        this.streetNumber = streetNumber;
        this.postalCode = postalCode;
        this.location = location;
        this.country = country;

        if (inventoriedObjects==numberObjects) {
            this.color = "done";
        }

        else if (inventoriedObjects==0) {
                this.color = "todo";
        }
        else {
            this.color = "doing";
        }

    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getStockCapacity() {
        return stockCapacity;
    }

    public void setStockCapacity(int stockCapacity) {
        this.stockCapacity = stockCapacity;
    }

    public int getInventoriedObjects() {
        return inventoriedObjects;
    }

    public void setInventoriedObjects(int inventoriedObjects) {
        this.inventoriedObjects = inventoriedObjects;
    }

    public int getNumberObjects() {
        return numberObjects;
    }

    public void setNumberObjects(int numberObjects) {
        this.numberObjects = numberObjects;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
