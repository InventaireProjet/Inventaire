package com.androidprojects.inventaireii;

/**
 * Created by Administrateur on 26.10.2015.
 */
public class ObjectStock {
    private int id;
    private int quantity;
    private boolean controlled;
    private int productID;
    private ObjectWarehouse warehouse;

    public ObjectStock(int quantity, boolean controlled, int productID, ObjectWarehouse warehouse) {
        this.quantity = quantity;
        this.controlled = controlled;
        this.productID = productID;
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

    public int getProduct() {
        return productID;
    }

    public void setProduct(ObjectProducts product) {
        this.productID = product.getId();
    }

    public ObjectWarehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(ObjectWarehouse warehouse) {
        this.warehouse = warehouse;
    }
}
