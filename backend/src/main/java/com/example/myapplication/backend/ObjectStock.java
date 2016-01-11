package com.example.myapplication.backend;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class ObjectStock {

   @Id
    private Long id;
    private int quantity;
    private boolean controlled;
   private Long productID;
    private Long warehouseID;



    public Long getId() {

        return id;
    }

    public void setId(Long id) {this.id = id; }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isControlled() {
        return controlled;
    }

    public void setControlled(boolean controlled) {
        this.controlled = controlled;
    }

   public Long getProductID() {
        return  productID;
    }

    public void setProductID(Long productID) {
        this.productID = productID;
    }

    public Long getWarehouseID() {
        return warehouseID;
    }

    public void setWarehouseID(Long warehouseID) {
        this.warehouseID = warehouseID;
    }
}
