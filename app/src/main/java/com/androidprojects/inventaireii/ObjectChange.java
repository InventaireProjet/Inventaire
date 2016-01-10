package com.androidprojects.inventaireii;

import com.androidprojects.inventaireii.db.InventoryContract.*;


public class ObjectChange {

    // Name of the tables to synchronize
    public final static String TABLE_WAREHOUSES = WarehouseEntry.TABLE_WAREHOUSES;
    public final static String TABLE_CATEGORIES = CategorieEntry.TABLE_CATEGORIES;
    public final static String TABLE_PRODUCTS = ProductEntry.TABLE_PRODUCTS;
    public final static String TABLE_STOCKS = StockEntry.TABLE_STOCKS;

    // Type of changes : we use an enum
    public enum TypeOfChange {updateObject, deleteObject, insertObject}

    // Element of table CHANGES
    private int id;

    private String table;
    private long elementId;
    private TypeOfChange typeOfChange;

    // Constructors
    public ObjectChange() {}

    public ObjectChange(String table, long elementId, TypeOfChange typeOfChange) {
        this.setTable(table);
        this.setElementId(elementId);
        this.setTypeOfChange(typeOfChange);
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public long getElementId() {
        return elementId;
    }

    public void setElementId(long elementId) {
        this.elementId = elementId;
    }

    public TypeOfChange getTypeOfChange() {
        return typeOfChange;
    }

    public void setTypeOfChange(TypeOfChange typeOfChange) {
        this.typeOfChange = typeOfChange;
    }
}
