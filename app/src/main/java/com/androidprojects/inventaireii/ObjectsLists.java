package com.androidprojects.inventaireii;


import java.util.ArrayList;

public class ObjectsLists {

    static ArrayList<ObjectCategories> categoryList = new ArrayList<ObjectCategories>();
    static ArrayList<ObjectStock> stockList = new ArrayList<ObjectStock>();
    static ArrayList<ObjectProducts> productList = new ArrayList<ObjectProducts>();
    static ArrayList<ObjectWarehouse> warehouseList = new ArrayList<ObjectWarehouse>();

    public ObjectsLists()
    {
    };

    public static ArrayList<ObjectWarehouse> getWarehouseList() {
        return warehouseList;
    }

    public static void setWarehouseList(ArrayList<ObjectWarehouse> warehouseList) {
        ObjectsLists.warehouseList = warehouseList;
    }

    public static ArrayList<ObjectStock> getStockList() {
        return stockList;
    }

    public static void setStockList(ArrayList<ObjectStock> stockList) {
        ObjectsLists.stockList = stockList;
    }

    public static ArrayList<ObjectProducts> getProductList() {
        return productList;
    }

    public static void setProductList(ArrayList<ObjectProducts> productList) {
        ObjectsLists.productList = productList;
    }

    public ArrayList<ObjectCategories> getCategoryList() {
        return categoryList;
    }

    public static void setCategoryList(ArrayList<ObjectCategories> categoryList) {
        ObjectsLists.categoryList = categoryList;
    }


}
