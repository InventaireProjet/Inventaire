package com.example.myapplication.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class ObjectCategories {

    @Id
    private Long id;
    private String color;
    private String name;
    private String inventoryState;




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
