package com.androidprojects.inventaireii;

/**
 * Created by Administrateur on 26.10.2015.
 */
public class ObjectStock {
    private int id;
    private int quantity;
    private boolean controlled;
    private ObjectProducts product;
    private ObjectWarehouse warehouse;

    public ObjectStock(int quantity, boolean controlled, ObjectProducts product, ObjectWarehouse warehouse) {
        this.quantity = quantity;
        this.controlled = controlled;
        this.product = product;
        this.warehouse = warehouse;
    }

    public ObjectStock() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public ObjectProducts getProduct() {
        return product;
    }

    public void setProduct(ObjectProducts product) {
        this.product = product;
    }

    public ObjectWarehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(ObjectWarehouse warehouse) {
        this.warehouse = warehouse;
    }
}
