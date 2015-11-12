package com.androidprojects.inventaireii.db;

import android.provider.BaseColumns;

import com.androidprojects.inventaireii.Product;

/**
 * Created by David on 07.11.2015.
 */
public final class InventoryContract {

    // Should never be instantiated
    public InventoryContract() { }

    /* PRODUCTS */
    public static abstract class ProductEntry implements BaseColumns {
        // Table name
        public static final String TABLE_PRODUCTS = "products";

        // Products Column names
        public static final String KEY_ID = "id";
        public static final String KEY_ART_NB = "artNb";
        public static final String KEY_NAME = "name";
        public static final String KEY_DESCRIPTION = "description";
        public static final String KEY_PRICE = "price";
        public static final String KEY_CATEGORY_ID = "categoryId";

        // Table record create statement
        public static final String CREATE_TABLE_PRODUCTS = "CREATE TABLE "
                + TABLE_PRODUCTS + "("
                + ProductEntry.KEY_ID + " INTEGER PRIMARY KEY, "
                + ProductEntry.KEY_ART_NB + " TEXT, "
                + ProductEntry.KEY_NAME + " TEXT, "
                + ProductEntry.KEY_DESCRIPTION + " TEXT, "
                + ProductEntry.KEY_PRICE + " DOUBLE, "
                + "FOREIGN KEY (" + ProductEntry.KEY_CATEGORY_ID
                    + ") REFERENCES " + CategorieEntry.TABLE_CATEGORIES
                    + " (" + KEY_ID + ") "
                + ");";
    }

    /* CATEGORIES */
    public static abstract class CategorieEntry implements BaseColumns {
        // Table name
        public static final String TABLE_CATEGORIES = "categories";

        // Categories Column names
        public static final String KEY_ID = "id";
        public static final String KEY_NAME = "name";

        // Table Categories Create statement
        public static final String CREATE_TABLE_CATEGORIES = "CREATE TABLE "
                + TABLE_CATEGORIES + "("
                + CategorieEntry.KEY_ID + " INTEGER PRIMARY KEY, "
                + CategorieEntry.KEY_NAME + " TEXT, "
                + ");";
    }

    /* WAREHOUSES */
    public static abstract class WarehouseEntry implements BaseColumns {
        // Table name
        public static final String TABLE_WAREHOUSES = "warehouses";

        // Warehouses Column names
        public static final String KEY_ID = "id";
        public static final String KEY_NAME = "name";
        public static final String KEY_CAPACITY = "capacity";
        public static final String KEY_STREET = "street";
        public static final String KEY_STREET_NUMBER = "streetNumber";
        public static final String KEY_ZIPCODE = "zipcode";
        public static final String KEY_CITY = "city";
        public static final String KEY_COUNTRY = "country";
        public static final String KEY_PHONE_NUMBER = "phoneNumber";

        // Table Warehouses Create statement
        public static final String CREATE_TABLE_WAREHOUSES = "CREATE TABLE "
                + TABLE_WAREHOUSES + "("
                + WarehouseEntry.KEY_ID + " INTEGER PRIMARY KEY, "
                + WarehouseEntry.KEY_NAME + " TEXT, "
                + WarehouseEntry.KEY_CAPACITY + " INTEGER, "
                + WarehouseEntry.KEY_STREET + " TEXT, "
                + WarehouseEntry.KEY_STREET_NUMBER + " TEXT, "
                + WarehouseEntry.KEY_ZIPCODE + " TEXT, "
                + WarehouseEntry.KEY_CITY + " TEXT, "
                + WarehouseEntry.KEY_COUNTRY + " TEXT, "
                + WarehouseEntry.KEY_PHONE_NUMBER + " TEXT "
                + ");";

    }

    /* STOCKS */
    public static abstract class StockEntry implements BaseColumns {
        // Table name
        public static final String TABLE_STOCKS = "stocks";

        // Stocks Column names
        public static final String KEY_ID = "id";
        public static final String KEY_PRODUCT_ID = "productId";
        public static final String KEY_WAREHOUSE_ID = "warehouseId";
        public static final String KEY_QUANTITY = "quantity";
        public static final String KEY_CONTROLLED = "controlled";

        // Table stocks create statement
        public static final String CREATE_TABLE_STOCKS = "CREATE TABLE "
                + TABLE_STOCKS + "("
                + StockEntry.KEY_ID + " INTEGER PRIMARY KEY, "
                + StockEntry.KEY_QUANTITY + " INTEGER, "
                + StockEntry.KEY_CONTROLLED + " INTEGER, "
                + "FOREIGN KEY (" + StockEntry.KEY_PRODUCT_ID
                    + ") REFERENCES " + ProductEntry.TABLE_PRODUCTS
                    + " (" + ProductEntry.KEY_ID + "), "
                + "FOREIGN KEY (" + StockEntry.KEY_WAREHOUSE_ID
                    + ") REFERENCES " + WarehouseEntry.TABLE_WAREHOUSES
                    + " (" + WarehouseEntry.KEY_ID + ") "
                + ");";
    }
}

