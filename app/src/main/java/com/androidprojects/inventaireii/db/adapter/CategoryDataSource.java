package com.androidprojects.inventaireii.db.adapter;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.androidprojects.inventaireii.Category;
import com.androidprojects.inventaireii.ObjectCategories;
import com.androidprojects.inventaireii.db.InventoryContract;
import com.androidprojects.inventaireii.db.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;

public class CategoryDataSource {

    private SQLiteDatabase db;
    private Context context;

    public  CategoryDataSource (Context context) {
        SQLiteHelper sqLiteHelper = SQLiteHelper.getInstance(context);
        db= sqLiteHelper.getWritableDatabase();
        this.context = context;
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

        this.db.delete(InventoryContract.CategorieEntry.TABLE_CATEGORIES, InventoryContract.CategorieEntry.KEY_ID + " = ?",
                new String[] { String.valueOf(id) });

    }
}
