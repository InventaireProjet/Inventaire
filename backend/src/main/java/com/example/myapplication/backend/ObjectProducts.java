package com.example.myapplication.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.util.ArrayList;



/* Creation of the ObjectProducts Entity in the backend with its parameters, the same as in the
corresponding table
 */

@Entity
public class ObjectProducts {

    @Id
    private Long id;
    private String artNb;
    private String name;
    private String description;
    private double price;
    private ObjectCategories category;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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



}
