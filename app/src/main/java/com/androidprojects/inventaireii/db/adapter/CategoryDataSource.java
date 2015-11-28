package com.androidprojects.inventaireii.db.adapter;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.androidprojects.inventaireii.ObjectCategories;
import com.androidprojects.inventaireii.ObjectProducts;
import com.androidprojects.inventaireii.db.InventoryContract;
import com.androidprojects.inventaireii.db.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;

public class CategoryDataSource {

    private static CategoryDataSource instance;
    private SQLiteDatabase db;
    private Context context;
    private ProductDataSource productDataSource;

    private   CategoryDataSource (Context context) {
        SQLiteHelper sqLiteHelper = SQLiteHelper.getInstance(context);
        db= sqLiteHelper.getWritableDatabase();
        this.context = context;
    }

    public static CategoryDataSource getInstance(Context context) {
        if(instance == null)
            instance = new CategoryDataSource(context);

        return instance;
    }

    //New Category creation

    public long createCategory (ObjectCategories category ) {
        long id;
        ContentValues values = new ContentValues();
        values.put(InventoryContract.CategorieEntry.KEY_NAME, category.getName());

        id = this.db.insert(InventoryContract.CategorieEntry.TABLE_CATEGORIES, null, values);

        return  id;
    }

    //Find a category by Id

    public ObjectCategories getCategoryById (long id) {
        String sql = "SELECT * FROM " + InventoryContract.CategorieEntry.TABLE_CATEGORIES +
                " WHERE " + InventoryContract.CategorieEntry.KEY_ID + " = " + id;

        Cursor cursor = this.db.rawQuery(sql, null);

        if (cursor!=null) {
            cursor.moveToFirst();
        }

        ObjectCategories category = new ObjectCategories();
        category.setId(cursor.getInt(cursor.getColumnIndex(InventoryContract.CategorieEntry.KEY_ID)));
        category.setName(cursor.getString(cursor.getColumnIndex(InventoryContract.CategorieEntry.KEY_NAME)));

        return category;
    }

    // Get all categories

    public List<ObjectCategories> getAllCategories () {
        List<ObjectCategories> categories = new ArrayList<>();
        String sql = "SELECT * FROM " + InventoryContract.CategorieEntry.TABLE_CATEGORIES + " ORDER BY " + InventoryContract.CategorieEntry.KEY_NAME;

        Cursor cursor = this.db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                ObjectCategories category = new ObjectCategories();
                category.setId(cursor.getInt(cursor.getColumnIndex(InventoryContract.CategorieEntry.KEY_ID)));
                category.setName(cursor.getString(cursor.getColumnIndex(InventoryContract.CategorieEntry.KEY_NAME)));

                categories.add(category);
            } while (cursor.moveToNext());

        }
        return categories;
    }

    //Update a category

    public int updateCategory(ObjectCategories category) {
        ContentValues values = new ContentValues();
        values.put(InventoryContract.CategorieEntry.KEY_NAME, category.getName());

        return this.db.update(InventoryContract.CategorieEntry.TABLE_CATEGORIES, values, InventoryContract.CategorieEntry.KEY_ID + " = ?",
                new String[] { String.valueOf(category.getId()) });
    }

    // Delete a Category without deletion of its products

    public void deleteCategory(long id){

        productDataSource = ProductDataSource.getInstance(context);
        List<ObjectProducts> products = productDataSource.getAllProductsByCategory(id);
        for (ObjectProducts p : products) {
            p.setCategory(null);
            productDataSource.updateProduct(p);
        }

        this.db.delete(InventoryContract.CategorieEntry.TABLE_CATEGORIES, InventoryContract.CategorieEntry.KEY_ID + " = ?",
                new String[] { String.valueOf(id) });

    }

/*

    // Number of inventoried objects in a category
    public int getInventoriedObjects(long categoryId) {
        String sql = "SELECT SUM(" + InventoryContract.StockEntry.KEY_QUANTITY + ") AS Number " +
                "FROM " + InventoryContract.CategorieEntry.TABLE_CATEGORIES + " C, "
                + InventoryContract.StockEntry.TABLE_STOCKS + " S, "
                +InventoryContract.ProductEntry.TABLE_PRODUCTS + " P, "
                + " WHERE " + InventoryContract.CategorieEntry.KEY_ID + " = " + categoryId
                +  " AND " +  "C." +InventoryContract.CategorieEntry.KEY_ID
                + " = " +  "P." +InventoryContract.ProductEntry.KEY_CATEGORY_ID
                + " AND " +  "P." +InventoryContract.ProductEntry.KEY_ID
                + " = " +  "S." +InventoryContract.StockEntry.KEY_PRODUCT_ID
                + " AND " + InventoryContract.StockEntry.KEY_CONTROLLED + " > 0";

        int quantity = 0;
        Cursor cursor = this.db.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
            quantity = cursor.getInt(cursor.getColumnIndex("Number"));
        }

        return quantity;
    }

    // Number of pieces in a category
    public int getNumberObjects(long categoryId) {
        String sql = "SELECT SUM(" + InventoryContract.StockEntry.KEY_QUANTITY + ") AS Number " +
                "FROM " + InventoryContract.CategorieEntry.TABLE_CATEGORIES + " C, "
                + InventoryContract.StockEntry.TABLE_STOCKS + " S, "
                +InventoryContract.ProductEntry.TABLE_PRODUCTS + " P, "
                + "WHERE " + InventoryContract.CategorieEntry.KEY_ID + " = " + categoryId
                +  " AND " +  "C." +InventoryContract.CategorieEntry.KEY_ID
                + " = " +  "P." +InventoryContract.CategorieEntry.KEY_ID
                + " AND " +  "P." +InventoryContract.ProductEntry.KEY_ID
                + " = " +  "S." +InventoryContract.ProductEntry.KEY_ID;

        int quantity = 0;
        Cursor cursor = this.db.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
            quantity = cursor.getInt(cursor.getColumnIndex("Number"));
        }
        return quantity ;
    }*/


}
