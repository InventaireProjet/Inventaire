package com.androidprojects.inventaireii.db.adapter;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.androidprojects.inventaireii.ObjectWarehouse;
import com.androidprojects.inventaireii.db.InventoryContract;
import com.androidprojects.inventaireii.db.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;

public class WarehouseDataSource {

    private SQLiteDatabase db;
    private Context context;

    public  WarehouseDataSource (Context context) {
        SQLiteHelper sqLiteHelper = SQLiteHelper.getInstance(context);
        db = sqLiteHelper.getWritableDatabase();
        this.context = context;
    }

    //New Warehouse creation

    public long createWarehouse (ObjectWarehouse warehouse ) {
        long id;
        ContentValues values = new ContentValues();
        values.put(InventoryContract.WarehouseEntry.KEY_NAME, warehouse.getName());
        values.put(InventoryContract.WarehouseEntry.KEY_CAPACITY, warehouse.getStockCapacity());
        values.put(InventoryContract.WarehouseEntry.KEY_PHONE_NUMBER, warehouse.getTelNumber());
        values.put(InventoryContract.WarehouseEntry.KEY_STREET, warehouse.getStreet());
        values.put(InventoryContract.WarehouseEntry.KEY_STREET_NUMBER, warehouse.getStreetNumber());
        values.put(InventoryContract.WarehouseEntry.KEY_CITY, warehouse.getLocation());
        values.put(InventoryContract.WarehouseEntry.KEY_ZIPCODE, warehouse.getPostalCode());
        values.put(InventoryContract.WarehouseEntry.KEY_COUNTRY, warehouse.getCountry());

        id = this.db.insert(InventoryContract.WarehouseEntry.TABLE_WAREHOUSES, null, values);

        return  id;
    }


    //Find a category by Id

    public ObjectWarehouse getWarehouseById (long id) {
        String sql = "SELECT * FROM " + InventoryContract.WarehouseEntry.TABLE_WAREHOUSES +
                " WHERE " + InventoryContract.WarehouseEntry.KEY_ID + " = " + id;

        Cursor cursor = this.db.rawQuery(sql, null);

        if (cursor!=null) {
            cursor.moveToFirst();
        }

        ObjectWarehouse warehouse = new ObjectWarehouse();
        warehouse.setId(cursor.getInt(cursor.getColumnIndex(InventoryContract.WarehouseEntry.KEY_ID)));
        warehouse.setName(cursor.getString(cursor.getColumnIndex(InventoryContract.WarehouseEntry.KEY_NAME)));
        warehouse.setStockCapacity(cursor.getInt(cursor.getColumnIndex(InventoryContract.WarehouseEntry.KEY_CAPACITY)));
        warehouse.setTelNumber(cursor.getString(cursor.getColumnIndex(InventoryContract.WarehouseEntry.KEY_PHONE_NUMBER)));
        warehouse.setStreet(cursor.getString(cursor.getColumnIndex(InventoryContract.WarehouseEntry.KEY_STREET)));
        warehouse.setStreetNumber(cursor.getString(cursor.getColumnIndex(InventoryContract.WarehouseEntry.KEY_STREET_NUMBER)));
        warehouse.setLocation(cursor.getString(cursor.getColumnIndex(InventoryContract.WarehouseEntry.KEY_CITY)));
        warehouse.setPostalCode(cursor.getString(cursor.getColumnIndex(InventoryContract.WarehouseEntry.KEY_ZIPCODE)));
        warehouse.setCountry(cursor.getString(cursor.getColumnIndex(InventoryContract.WarehouseEntry.KEY_COUNTRY)));


        return warehouse;
    }


    // Get all warehouses

    public List<ObjectWarehouse> getAllWarehouses () {
        List<ObjectWarehouse> warehouses = new ArrayList<>();
        String sql = "SELECT * FROM " + InventoryContract.WarehouseEntry.TABLE_WAREHOUSES + " ORDER BY " + InventoryContract.WarehouseEntry.KEY_NAME;

        Cursor cursor = this.db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                ObjectWarehouse warehouse = new ObjectWarehouse();
                warehouse.setId(cursor.getInt(cursor.getColumnIndex(InventoryContract.WarehouseEntry.KEY_ID)));
                warehouse.setName(cursor.getString(cursor.getColumnIndex(InventoryContract.WarehouseEntry.KEY_NAME)));
                warehouse.setStockCapacity(cursor.getInt(cursor.getColumnIndex(InventoryContract.WarehouseEntry.KEY_CAPACITY)));
                warehouse.setTelNumber(cursor.getString(cursor.getColumnIndex(InventoryContract.WarehouseEntry.KEY_PHONE_NUMBER)));
                warehouse.setStreet(cursor.getString(cursor.getColumnIndex(InventoryContract.WarehouseEntry.KEY_STREET)));
                warehouse.setStreetNumber(cursor.getString(cursor.getColumnIndex(InventoryContract.WarehouseEntry.KEY_STREET_NUMBER)));
                warehouse.setLocation(cursor.getString(cursor.getColumnIndex(InventoryContract.WarehouseEntry.KEY_CITY)));
                warehouse.setPostalCode(cursor.getString(cursor.getColumnIndex(InventoryContract.WarehouseEntry.KEY_ZIPCODE)));
                warehouse.setCountry(cursor.getString(cursor.getColumnIndex(InventoryContract.WarehouseEntry.KEY_COUNTRY)));


                warehouses.add(warehouse);
            } while (cursor.moveToNext());

        }
        return warehouses;
    }



//Update a warehouse

    public int updateWarehouse(ObjectWarehouse warehouse) {
        ContentValues values = new ContentValues();
        values.put(InventoryContract.WarehouseEntry.KEY_NAME, warehouse.getName());
        values.put(InventoryContract.WarehouseEntry.KEY_CAPACITY, warehouse.getStockCapacity());
        values.put(InventoryContract.WarehouseEntry.KEY_PHONE_NUMBER, warehouse.getTelNumber());
        values.put(InventoryContract.WarehouseEntry.KEY_STREET, warehouse.getStreet());
        values.put(InventoryContract.WarehouseEntry.KEY_STREET_NUMBER, warehouse.getStreetNumber());
        values.put(InventoryContract.WarehouseEntry.KEY_CITY, warehouse.getLocation());
        values.put(InventoryContract.WarehouseEntry.KEY_ZIPCODE, warehouse.getPostalCode());
        values.put(InventoryContract.WarehouseEntry.KEY_COUNTRY, warehouse.getCountry());


        return this.db.update(InventoryContract.WarehouseEntry.TABLE_WAREHOUSES, values, InventoryContract.WarehouseEntry.KEY_ID + " = ?",
                new String[] { String.valueOf(warehouse.getId()) });
    }

    // Delete a Warehouse without deletion of its products

    public void deleteWarehouse(long id){

        this.db.delete(InventoryContract.WarehouseEntry.TABLE_WAREHOUSES, InventoryContract.WarehouseEntry.KEY_ID + " = ?",
                new String[]{String.valueOf(id)});

    }

    // TODO Delete a Warehouse with deletion of its products

    public void deleteWarehouseAndProducts(long id){



        //Warehouse deletion
        this.db.delete(InventoryContract.WarehouseEntry.TABLE_WAREHOUSES, InventoryContract.WarehouseEntry.KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

}
