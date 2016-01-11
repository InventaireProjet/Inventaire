package com.androidprojects.inventaireii.db.adapter;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.androidprojects.inventaireii.ObjectCategories;
import com.androidprojects.inventaireii.ObjectChange;
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
    private ChangeDataSource changeDataSource;

    private   CategoryDataSource (Context context) {
        SQLiteHelper sqLiteHelper = SQLiteHelper.getInstance(context);
        db= sqLiteHelper.getWritableDatabase();
        changeDataSource = ChangeDataSource.getInstance(context);
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

        // save this insert in the change table
        changeDataSource.createChange(new ObjectChange(ObjectChange.TABLE_CATEGORIES, id, ObjectChange.TypeOfChange.insertObject));

        return  id;
    }

    //Find a category by Id

    public ObjectCategories getCategoryById (long id) {
        String sql = "SELECT * FROM " + InventoryContract.CategorieEntry.TABLE_CATEGORIES +
                " WHERE " + InventoryContract.CategorieEntry.KEY_ID + " = " + id;

        Cursor cursor = this.db.rawQuery(sql, null);

        ObjectCategories category = null;
        if (cursor.moveToFirst()) {
            category = new ObjectCategories();
            category.setId(cursor.getInt(cursor.getColumnIndex(InventoryContract.CategorieEntry.KEY_ID)));
            category.setName(cursor.getString(cursor.getColumnIndex(InventoryContract.CategorieEntry.KEY_NAME)));
        }
        cursor.close();
        return category;
    }


    //Find a category by Id for synchronization

    public com.example.myapplication.backend.objectCategoriesApi.model.ObjectCategories getCategoryByIdSync (long id) {
        String sql = "SELECT * FROM " + InventoryContract.CategorieEntry.TABLE_CATEGORIES +
                " WHERE " + InventoryContract.CategorieEntry.KEY_ID + " = " + id;

        Cursor cursor = this.db.rawQuery(sql, null);

        if (cursor!=null) {
            cursor.moveToFirst();
        }

        com.example.myapplication.backend.objectCategoriesApi.model.ObjectCategories category = new com.example.myapplication.backend.objectCategoriesApi.model.ObjectCategories();
        category.setId(cursor.getLong(cursor.getColumnIndex(InventoryContract.CategorieEntry.KEY_ID)));
        category.setName(cursor.getString(cursor.getColumnIndex(InventoryContract.CategorieEntry.KEY_NAME)));
        cursor.close();
        return category;
    }


    //Find a category by Id for synchronization with products from backend

    public com.example.myapplication.backend.objectProductsApi.model.ObjectCategories getCategoryByIdSyncProd (long id) {
        String sql = "SELECT * FROM " + InventoryContract.CategorieEntry.TABLE_CATEGORIES +
                " WHERE " + InventoryContract.CategorieEntry.KEY_ID + " = " + id;

        Cursor cursor = this.db.rawQuery(sql, null);

        if (cursor!=null) {
            cursor.moveToFirst();
        }

        com.example.myapplication.backend.objectProductsApi.model.ObjectCategories category = new com.example.myapplication.backend.objectProductsApi.model.ObjectCategories();
        category.setId(cursor.getLong(cursor.getColumnIndex(InventoryContract.CategorieEntry.KEY_ID)));
        category.setName(cursor.getString(cursor.getColumnIndex(InventoryContract.CategorieEntry.KEY_NAME)));
        cursor.close();
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

            cursor.close();
        }
        return categories;
    }




    //Update a category

    public int updateCategory(ObjectCategories category) {
        ContentValues values = new ContentValues();
        values.put(InventoryContract.CategorieEntry.KEY_NAME, category.getName());

        // save this update in the changes table
        changeDataSource.createChange(new ObjectChange(ObjectChange.TABLE_CATEGORIES, category.getId(), ObjectChange.TypeOfChange.updateObject));

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

        // save this delete int the changes table
        changeDataSource.createChange(new ObjectChange(ObjectChange.TABLE_CATEGORIES, id, ObjectChange.TypeOfChange.deleteObject));

    }

}
