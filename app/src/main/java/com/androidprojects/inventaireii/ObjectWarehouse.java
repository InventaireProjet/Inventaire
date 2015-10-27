package com.androidprojects.inventaireii;

public class ObjectWarehouse {

    private  int id;
    private String color;
    private String name;
    private int inventoriedObjects;
    private int numberObjects;
    private int stockCapacity;
    private String telNumber;
    private String strasse;
    private String strasseNumber;
    private String npa;
    private String ort;
    private String land;


    public ObjectWarehouse(String color, String name, int inventoriedObjects, int numberObjects, int stockCapacity, String telNumber, String strasse, String strasseNumber, String npa, String ort, String land) {
        this.color = color;
        this.name = name;
        this.inventoriedObjects = inventoriedObjects;
        this.numberObjects = numberObjects;
        this.stockCapacity = stockCapacity;
        this.telNumber = telNumber;
        this.strasse = strasse;
        this.strasseNumber = strasseNumber;
        this.npa = npa;
        this.ort = ort;
        this.land = land;
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

    public String getStrasse() {
        return strasse;
    }

    public String getStrasseNumber() {
        return strasseNumber;
    }

    public String getNpa() {
        return npa;
    }

    public String getOrt() {
        return ort;
    }

    public String getLand() {
        return land;
    }


    public String getColor() {
        return color;
    }


    public String getName() {
        return name;
    }
}
