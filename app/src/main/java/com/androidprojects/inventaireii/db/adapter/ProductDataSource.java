package com.androidprojects.inventaireii.db.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.androidprojects.inventaireii.ObjectCategories;
import com.androidprojects.inventaireii.ObjectChange;
import com.androidprojects.inventaireii.ObjectProducts;
import com.androidprojects.inventaireii.ObjectStock;
import com.androidprojects.inventaireii.db.InventoryContract;
import com.androidprojects.inventaireii.db.SQLiteHelper;


import java.util.ArrayList;
import java.util.List;

public class ProductDataSource {

    private static ProductDataSource instance;
    private SQLiteDatabase db;
    private Context context;
    private CategoryDataSource categoryDataSource ;
    private StockDataSource stockDataSource;
    private ChangeDataSource changeDataSource;

    private  ProductDataSource (Context context) {
        SQLiteHelper sqLiteHelper = SQLiteHelper.getInstance(context);
        db = sqLiteHelper.getWritableDatabase();
        changeDataSource = ChangeDataSource.getInstance(context);
        this.context = context;
    }

    public static ProductDataSource getInstance(Context context) {
        if(instance == null)
            instance = new ProductDataSource(context);

        return instance;
    }

    //New Product creation

    public long createProduct (ObjectProducts product ) {
        long id;
        ContentValues values = new ContentValues();
        values.put(InventoryContract.ProductEntry.KEY_ART_NB, product.getArtNb());
        values.put(InventoryContract.ProductEntry.KEY_NAME, product.getName());
        values.put(InventoryContract.ProductEntry.KEY_DESCRIPTION, product.getDescription());
        values.put(InventoryContract.ProductEntry.KEY_PRICE, product.getPrice());
        int categoryId = 0;
        if (product.getCategory() != null)
            categoryId = product.getCategory().getId();
        values.put(InventoryContract.ProductEntry.KEY_CATEGORY_ID, categoryId);

        id = this.db.insert(InventoryContract.ProductEntry.TABLE_PRODUCTS, null, values);

        // save this insert in the changes table
        changeDataSource.createChange(new ObjectChange(ObjectChange.TABLE_PRODUCTS,
                id, ObjectChange.TypeOfChange.insertObject));

        return  id;
    }


    //Find a product by Id

    public ObjectProducts getProductById (long id) {
        ObjectProducts product = null;
        String sql = "SELECT * FROM " + InventoryContract.ProductEntry.TABLE_PRODUCTS +
                " WHERE " + InventoryContract.ProductEntry.KEY_ID + " = " + id;

        Cursor cursor = this.db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            product = getProductFromCursor(cursor);
        }
        cursor.close();

        return product;
    }

    // Get all products

    public List<ObjectProducts> getAllProducts () {
        List<ObjectProducts> products = new ArrayList<>();
        String sql = "SELECT * FROM " + InventoryContract.ProductEntry.TABLE_PRODUCTS + " ORDER BY " + InventoryContract.ProductEntry.KEY_NAME;

        Cursor cursor = this.db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                ObjectProducts product = getProductFromCursor(cursor);
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
                ObjectProducts product = getProductFromCursor(cursor);
                products.add(product);
            } while (cursor.moveToNext());

        }
        return products;
    }

    // Get all products by warehouse

    public List<ObjectProducts> getAllProductsByWarehouse (long warehouse_id) {
        List<ObjectProducts> products = new ArrayList<>();
        String sql = "SELECT p.* FROM " + InventoryContract.ProductEntry.TABLE_PRODUCTS + " p, "
                + InventoryContract.StockEntry.TABLE_STOCKS + " s "
                + " WHERE s." + InventoryContract.StockEntry.KEY_WAREHOUSE_ID + " = " +warehouse_id
                + " AND s." + InventoryContract.StockEntry.KEY_PRODUCT_ID + " = " + "p." + InventoryContract.ProductEntry.KEY_ID
                + " ORDER BY " + InventoryContract.ProductEntry.KEY_NAME;


        Cursor cursor = this.db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                ObjectProducts product = getProductFromCursor(cursor);
                products.add(product);
            } while (cursor.moveToNext());

        }
        return products;
    }

    private ObjectProducts getProductFromCursor(Cursor cursor) {
        ObjectProducts product = new ObjectProducts();
        product.setId(cursor.getInt(cursor.getColumnIndex(InventoryContract.ProductEntry.KEY_ID)));
        product.setArtNb(cursor.getString(cursor.getColumnIndex(InventoryContract.ProductEntry.KEY_ART_NB)));
        product.setName(cursor.getString(cursor.getColumnIndex(InventoryContract.ProductEntry.KEY_NAME)));
        product.setDescription(cursor.getString(cursor.getColumnIndex(InventoryContract.ProductEntry.KEY_DESCRIPTION)));
        product.setPrice(cursor.getDouble(cursor.getColumnIndex(InventoryContract.ProductEntry.KEY_PRICE)));

        // Get the category, if there is one (else KEY_CATEGORY_ID = 0)
        int categoryId = cursor.getInt(cursor.getColumnIndex(InventoryContract.ProductEntry.KEY_CATEGORY_ID));
        if (categoryId > 0) {
            categoryDataSource = CategoryDataSource.getInstance(context);
            ObjectCategories cat = categoryDataSource.getCategoryById(categoryId);
            product.setCategory(cat);
        }

        // Get the stocks
        stockDataSource = StockDataSource.getInstance(context);
        ArrayList<ObjectStock> stocks = stockDataSource.getAllStocksByProduct(product);
        product.setStocks(stocks);
        return product;
    }


    //Update a product

    public int updateProduct(ObjectProducts product) {
        ContentValues values = new ContentValues();
        values.put(InventoryContract.ProductEntry.KEY_ART_NB, product.getArtNb());
        values.put(InventoryContract.ProductEntry.KEY_NAME, product.getName());
        values.put(InventoryContract.ProductEntry.KEY_DESCRIPTION, product.getDescription());
        values.put(InventoryContract.ProductEntry.KEY_PRICE, product.getPrice());

        int categoryId = 0;
        if (product.getCategory() != null)
            categoryId = product.getCategory().getId();
        values.put(InventoryContract.ProductEntry.KEY_CATEGORY_ID, categoryId);

        // save this update in the changes table
        changeDataSource.createChange(new ObjectChange(ObjectChange.TABLE_PRODUCTS,
                product.getId(), ObjectChange.TypeOfChange.updateObject));

        return this.db.update(InventoryContract.ProductEntry.TABLE_PRODUCTS, values, InventoryContract.ProductEntry.KEY_ID + " = ?",
                new String[] { String.valueOf(product.getId()) });
    }


// Delete a product

    public void deleteProduct(ObjectProducts product){
        // We need to destroy all associated stocks
        List<ObjectStock> productStocks = product.getStocks();
        for (ObjectStock s : productStocks) {
            product.removeStock(s);
            stockDataSource.deleteStock(s);
        }

        // save this delete in the changes table
        changeDataSource.createChange(new ObjectChange(ObjectChange.TABLE_PRODUCTS,
                product.getId(), ObjectChange.TypeOfChange.deleteObject));

        this.db.delete(InventoryContract.ProductEntry.TABLE_PRODUCTS, InventoryContract.ProductEntry.KEY_ID + " = ?",
                new String[]{String.valueOf(product.getId())});

    }


    //Transposition of find a category by Id for synchronization, retuning backend object being necessary
    public com.example.myapplication.backend.objectProductsApi.model.ObjectProducts getProductByIdSync(long id) {

        com.example.myapplication.backend.objectProductsApi.model.ObjectProducts product = null;
        String sql = "SELECT * FROM " + InventoryContract.ProductEntry.TABLE_PRODUCTS +
                " WHERE " + InventoryContract.ProductEntry.KEY_ID + " = " + id;

        Cursor cursor = this.db.rawQuery(sql, null);

        if (cursor!=null) {
            cursor.moveToFirst();
            product = getProductFromCursorSync(cursor);
            cursor.close();
        }

        return product;

    }

    //Transposition of the original method for synchronization, retuning backend object being necessary

    private com.example.myapplication.backend.objectProductsApi.model.ObjectProducts getProductFromCursorSync(Cursor cursor) {
        com.example.myapplication.backend.objectProductsApi.model.ObjectProducts product = new com.example.myapplication.backend.objectProductsApi.model.ObjectProducts();
        product.setId(cursor.getLong(cursor.getColumnIndex(InventoryContract.ProductEntry.KEY_ID)));
        product.setArtNb(cursor.getString(cursor.getColumnIndex(InventoryContract.ProductEntry.KEY_ART_NB)));
        product.setName(cursor.getString(cursor.getColumnIndex(InventoryContract.ProductEntry.KEY_NAME)));
        product.setDescription(cursor.getString(cursor.getColumnIndex(InventoryContract.ProductEntry.KEY_DESCRIPTION)));
        product.setPrice(cursor.getDouble(cursor.getColumnIndex(InventoryContract.ProductEntry.KEY_PRICE)));

        // Get the category, if there is one (else KEY_CATEGORY_ID = 0)
        int categoryId = cursor.getInt(cursor.getColumnIndex(InventoryContract.ProductEntry.KEY_CATEGORY_ID));
        if (categoryId > 0) {
            categoryDataSource = CategoryDataSource.getInstance(context);
            com.example.myapplication.backend.objectProductsApi.model.ObjectCategories cat = categoryDataSource.getCategoryByIdSyncProd(categoryId);
            product.setCategory(cat);
        }


        return product;
    }

}