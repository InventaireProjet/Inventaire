package com.androidprojects.inventaireii.db.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.androidprojects.inventaireii.ObjectProducts;
import com.androidprojects.inventaireii.db.InventoryContract;
import com.androidprojects.inventaireii.db.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;

public class ProductDataSource {


    private SQLiteDatabase db;
    private Context context;
    private CategoryDataSource categoryDataSource ;

    public  ProductDataSource (Context context) {
        categoryDataSource = new CategoryDataSource(context);
        SQLiteHelper sqLiteHelper = SQLiteHelper.getInstance(context);
        db = sqLiteHelper.getWritableDatabase();
        this.context = context;
    }

    //New Product creation

    public long createProduct (ObjectProducts product ) {
        long id;
        ContentValues values = new ContentValues();
        values.put(InventoryContract.ProductEntry.KEY_ART_NB, product.getArtNb());
        values.put(InventoryContract.ProductEntry.KEY_NAME, product.getName());
        values.put(InventoryContract.ProductEntry.KEY_DESCRIPTION, product.getDescription());
        values.put(InventoryContract.ProductEntry.KEY_PRICE, product.getPrice());
        values.put(InventoryContract.ProductEntry.KEY_CATEGORY_ID, product.getCategory().getId());

        id = this.db.insert(InventoryContract.ProductEntry.TABLE_PRODUCTS, null, values);

        return  id;
    }

    // Get the number of products
    public int getCountProduct() {
        int number = 0;
        String sql = "SELECT COUNT(*) FROM " + InventoryContract.ProductEntry.TABLE_PRODUCTS;
        Cursor cursor = this.db.rawQuery(sql, null);

        if (cursor != null){
            cursor.moveToFirst();
        }

        number = cursor.getInt(1);
        return number;
    }

    //Find a product by Id

    public ObjectProducts getProductById (long id) {
        String sql = "SELECT * FROM " + InventoryContract.ProductEntry.TABLE_PRODUCTS +
                " WHERE " + InventoryContract.ProductEntry.KEY_ID + " = " + id;

        Cursor cursor = this.db.rawQuery(sql, null);

        if (cursor!=null) {
            cursor.moveToFirst();
        }

        ObjectProducts product = new ObjectProducts();
        product.setId(cursor.getInt(cursor.getColumnIndex(InventoryContract.ProductEntry.KEY_ID)));
        product.setArtNb(cursor.getString(cursor.getColumnIndex(InventoryContract.ProductEntry.KEY_ART_NB)));
        product.setName(cursor.getString(cursor.getColumnIndex(InventoryContract.ProductEntry.KEY_NAME)));
        product.setDescription(cursor.getString(cursor.getColumnIndex(InventoryContract.ProductEntry.KEY_DESCRIPTION)));
        product.setPrice(cursor.getDouble(cursor.getColumnIndex(InventoryContract.ProductEntry.KEY_PRICE)));
        //TODO To adapt
        // product.setCategory(cursor.getInt(cursor.getColumnIndex(InventoryContract.ProductEntry.KEY_CATEGORY_ID)));

        return product;
    }

    // Get all products

    public List<ObjectProducts> getAllProducts () {
        List<ObjectProducts> products = new ArrayList<>();
        String sql = "SELECT * FROM " + InventoryContract.ProductEntry.TABLE_PRODUCTS + " ORDER BY " + InventoryContract.ProductEntry.KEY_NAME;

        Cursor cursor = this.db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                ObjectProducts product = new ObjectProducts();
                product.setId(cursor.getInt(cursor.getColumnIndex((InventoryContract.ProductEntry.KEY_ID))));
                product.setArtNb(cursor.getString(cursor.getColumnIndex(InventoryContract.ProductEntry.KEY_ART_NB)));
                product.setName(cursor.getString(cursor.getColumnIndex(InventoryContract.ProductEntry.KEY_NAME)));
                product.setDescription(cursor.getString(cursor.getColumnIndex(InventoryContract.ProductEntry.KEY_DESCRIPTION)));
                product.setPrice(cursor.getDouble(cursor.getColumnIndex(InventoryContract.ProductEntry.KEY_PRICE)));
                //TODO To adapt
                // product.setCategory(cursor.getInt(cursor.getColumnIndex(InventoryContract.ProductEntry.KEY_CATEGORY_ID)));


                products.add(product);
            } while (cursor.moveToNext());

        }
        return products;
    }


    // Get all products by category

    public List<ObjectProducts> getAllProductsByCategory (long category_id) {
        List<ObjectProducts> products = new ArrayList<>();
        String sql = "SELECT * FROM " + InventoryContract.ProductEntry.TABLE_PRODUCTS
                +" WHERE " +InventoryContract.ProductEntry.KEY_CATEGORY_ID +" = " +category_id
                + " ORDER BY " + InventoryContract.ProductEntry.KEY_NAME;


        Cursor cursor = this.db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                ObjectProducts product = new ObjectProducts();
                product.setArtNb(cursor.getString(cursor.getColumnIndex(InventoryContract.ProductEntry.KEY_ART_NB)));
                product.setName(cursor.getString(cursor.getColumnIndex(InventoryContract.ProductEntry.KEY_NAME)));
                product.setDescription(cursor.getString(cursor.getColumnIndex(InventoryContract.ProductEntry.KEY_DESCRIPTION)));
                product.setPrice(cursor.getDouble(cursor.getColumnIndex(InventoryContract.ProductEntry.KEY_PRICE)));
                 product.setCategory(categoryDataSource.getCategoryById(cursor.getInt(cursor.getColumnIndex(InventoryContract.ProductEntry.KEY_CATEGORY_ID))));

                CategoryDataSource cd;

                products.add(product);
            } while (cursor.moveToNext());

        }
        return products;
    }

    // Get all products by warehouse

    public List<ObjectProducts> getAllProductsByWarehouse (long warehouse_id) {
        List<ObjectProducts> products = new ArrayList<>();
        String sql = "SELECT * FROM " + InventoryContract.ProductEntry.TABLE_PRODUCTS + " p, "
                + InventoryContract.StockEntry.TABLE_STOCKS + " s "
                + " WHERE s." + InventoryContract.StockEntry.KEY_WAREHOUSE_ID + " = " +warehouse_id
                + " AND s." + InventoryContract.StockEntry.KEY_PRODUCT_ID + " = " + "p." + InventoryContract.ProductEntry.KEY_ID
                + " ORDER BY " + InventoryContract.ProductEntry.KEY_NAME;


        Cursor cursor = this.db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                ObjectProducts product = new ObjectProducts();
                product.setArtNb(cursor.getString(cursor.getColumnIndex(InventoryContract.ProductEntry.KEY_ART_NB)));
                product.setName(cursor.getString(cursor.getColumnIndex(InventoryContract.ProductEntry.KEY_NAME)));
                product.setDescription(cursor.getString(cursor.getColumnIndex(InventoryContract.ProductEntry.KEY_DESCRIPTION)));
                product.setPrice(cursor.getDouble(cursor.getColumnIndex(InventoryContract.ProductEntry.KEY_PRICE)));
                product.setCategory(categoryDataSource.getCategoryById(cursor.getInt(cursor.getColumnIndex(InventoryContract.ProductEntry.KEY_CATEGORY_ID))));


                products.add(product);
            } while (cursor.moveToNext());

        }
        return products;
    }


    //Update a product

   public int updateProduct(ObjectProducts product) {
        ContentValues values = new ContentValues();
        values.put(InventoryContract.ProductEntry.KEY_ART_NB, product.getArtNb());
        values.put(InventoryContract.ProductEntry.KEY_NAME, product.getName());
        values.put(InventoryContract.ProductEntry.KEY_DESCRIPTION, product.getDescription());
        values.put(InventoryContract.ProductEntry.KEY_PRICE, product.getPrice());
        values.put(InventoryContract.ProductEntry.KEY_CATEGORY_ID, product.getCategory().getId());

        return this.db.update(InventoryContract.ProductEntry.TABLE_PRODUCTS, values, InventoryContract.ProductEntry.KEY_ID + " = ?",
                new String[] { String.valueOf(product.getId()) });
    }


// Delete a product

    public void deleteProduct(long id){

        this.db.delete(InventoryContract.ProductEntry.TABLE_PRODUCTS, InventoryContract.ProductEntry.KEY_ID + " = ?",
                new String[]{String.valueOf(id)});

    }
}