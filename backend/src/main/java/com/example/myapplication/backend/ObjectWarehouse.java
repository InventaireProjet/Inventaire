package com.example.myapplication.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;


/* Creation of the ObjectWarehouse Entity in the backend with its parameters, the same as in the
corresponding table
 */


@Entity
public class ObjectWarehouse {

    @Id
    private  Long id;
    private String name;
    private int stockCapacity;
    private String telNumber;
    private String street;
    private String streetNumber;
    private String postalCode;
    private String location;
    private String country;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public int getStockCapacity() {
        return stockCapacity;
    }

    public void setStockCapacity(int stockCapacity) {
        this.stockCapacity = stockCapacity;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
