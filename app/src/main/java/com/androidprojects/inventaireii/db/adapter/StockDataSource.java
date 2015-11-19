package com.androidprojects.inventaireii.db.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.androidprojects.inventaireii.ObjectProducts;
import com.androidprojects.inventaireii.ObjectStock;
import com.androidprojects.inventaireii.ObjectWarehouse;
import com.androidprojects.inventaireii.db.InventoryContract;
import com.androidprojects.inventaireii.db.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 19.11.2015.
 */
public class StockDataSource {

    private SQLiteDatabase db;
    private Context context = null;
    private ProductDataSource productDataSource = new ProductDataSource(context);
    private WarehouseDataSource warehouseDataSource = new WarehouseDataSource(context);

    public  StockDataSource (Context context) {
        SQLiteHelper sqLiteHelper = SQLiteHelper.getInstance(context);
        db = sqLiteHelper.getWritableDatabase();
        this.context = context;
    }

    /* New Stock creation */
    public  long createStock (ObjectStock stock) {
        long id;
        ContentValues values = new ContentValues();
        values.put(InventoryContract.StockEntry.KEY_QUANTITY, stock.getQuantity());
        values.put(InventoryContract.StockEntry.KEY_CONTROLLED, stock.isControlled());
        values.put(InventoryContract.StockEntry.KEY_PRODUCT_ID, stock.getProduct().getId());
        values.put(InventoryContract.StockEntry.KEY_WAREHOUSE_ID, stock.getWarehouse().getId());

        id = this.db.insert(InventoryContract.StockEntry.TABLE_STOCKS, null, values);

        return  id;
    }

    /* Find a stock by Id */

    public ObjectStock getStockById (long id) {
        String sql = "SELECT * FROM " + InventoryContract.StockEntry.TABLE_STOCKS
                + " WHERE " + InventoryContract.ProductEntry.KEY_ID + " = " + id;

        Cursor cursor = this.db.rawQuery(sql, null);

        ObjectStock stock = new ObjectStock();
        stock.setQuantity(cursor.getInt(cursor.getColumnIndex(InventoryContract.StockEntry.KEY_QUANTITY)));
        stock.setControlled(cursor.getInt(cursor.getColumnIndex(InventoryContract.StockEntry.KEY_CONTROLLED))>0);

        // get the Warehouse
        int warehouseId = cursor.getInt(cursor.getColumnIndex(InventoryContract.StockEntry.KEY_WAREHOUSE_ID));
        ObjectWarehouse warehouse = warehouseDataSource.getWarehouseById(warehouseId);
        stock.setWarehouse(warehouse);

        // get the Product
        int productId = cursor.getInt(cursor.getColumnIndex(InventoryContract.StockEntry.KEY_PRODUCT_ID));
        ObjectProducts product = productDataSource.getProductById(productId);
        stock.setProduct(product);

        return stock;
    }

    /* Get all stocks of a product */
    public List<ObjectStock> getAllStocksByProduct(ObjectProducts product) {
        List<ObjectStock> stocks = new ArrayList<>();
        String sql = "SELECT * FROM " + InventoryContract.StockEntry.TABLE_STOCKS
                + " WHERE " + InventoryContract.StockEntry.KEY_PRODUCT_ID + " = " + product.getId();
        // TODO : jointure et ORDER BY nom du magasin...

        Cursor cursor = this.db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                ObjectStock stock = new ObjectStock();
                stock.setQuantity(cursor.getInt(cursor.getColumnIndex(InventoryContract.StockEntry.KEY_QUANTITY)));
                stock.setControlled(cursor.getInt(cursor.getColumnIndex(InventoryContract.StockEntry.KEY_CONTROLLED))>0);

                // get the Warehouse
                int warehouseId = cursor.getInt(cursor.getColumnIndex(InventoryContract.StockEntry.KEY_WAREHOUSE_ID));
                ObjectWarehouse warehouse = warehouseDataSource.getWarehouseById(warehouseId);
                stock.setWarehouse(warehouse);

                // the Product is well known
                stock.setProduct(product);

                // Add this stock to the list
                stocks.add(stock);

            } while (cursor.moveToNext());
        }

        return stocks;
    }

    /* Update a stock */
    public int updateStock(ObjectStock stock) {
        ContentValues values = new ContentValues();
        values.put(InventoryContract.StockEntry.KEY_QUANTITY, stock.getQuantity());
        values.put(InventoryContract.StockEntry.KEY_CONTROLLED, stock.isControlled());
        values.put(InventoryContract.StockEntry.KEY_WAREHOUSE_ID, stock.getWarehouse().getId());
        values.put(InventoryContract.StockEntry.KEY_PRODUCT_ID, stock.getProduct().getId());

        return this.db.update(InventoryContract.StockEntry.TABLE_STOCKS, values,
                InventoryContract.StockEntry.KEY_ID + " = ?",
                new String[]{ String.valueOf(stock.getId())});
    }

    /* Delete a stock */
    public void deleteStock(ObjectStock stock) {
        this.db.delete(InventoryContract.StockEntry.TABLE_STOCKS,
                InventoryContract.StockEntry.KEY_ID + " = ?",
                new String[]{String.valueOf(stock.getId())});
    }

}