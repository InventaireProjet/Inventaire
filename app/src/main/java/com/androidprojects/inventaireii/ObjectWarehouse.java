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

    public int getInventoriedObjects() {
        return inventoriedObjects;
    }

    public int getNumberObjects() {
        return numberObjects;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public String getStreet() {
        return street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getLocation() {
        return location;
    }

    public String getCountry() {
        return country;
    }


    public String getColor() {
        return color;
    }


    public String getName() {
        return name;
    }
}
