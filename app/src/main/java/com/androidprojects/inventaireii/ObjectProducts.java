package com.androidprojects.inventaireii;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Administrateur on 25.10.2015.
 */
public class ObjectProducts {


    private int id;
    private String artNb;
    private String name;
    private String description;
    private double price;
    private ObjectCategories category;
    private ArrayList<ObjectStock> stocks = new ArrayList<ObjectStock>();

    public ObjectProducts() {
    }

    public ObjectProducts(String artNb, String name, ObjectCategories category,
                          double price, String description) {
        this.artNb = artNb;
        this.name = name;
        this.category = category;
        this.price = price;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getArtNb() {
        return artNb;
    }

    public void setArtNb(String artNb) {
        this.artNb = artNb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ObjectCategories getCategory() {
        return category;
    }

    public void setCategory(ObjectCategories category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<ObjectStock> getStocks() {
        return stocks;
    }

    public void setStocks(ArrayList<ObjectStock> stocks) {
        this.stocks = stocks;
    }

    public void addStock(ObjectStock stock) {
        stocks.add(stock);
    }

    public void removeStock(ObjectStock stock) {stocks.remove(stock); }

    public int getQuantity() {
        int quantity = 0;
        for (ObjectStock stock : stocks) {
            quantity += stock.getQuantity();
        }

        return quantity;
    }

}
