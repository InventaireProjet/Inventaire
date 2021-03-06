package com.androidprojects.inventaireii.db.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.androidprojects.inventaireii.ObjectChange;
import com.androidprojects.inventaireii.ObjectProducts;
import com.androidprojects.inventaireii.ObjectStock;
import com.androidprojects.inventaireii.ObjectWarehouse;
import com.androidprojects.inventaireii.db.InventoryContract;
import com.androidprojects.inventaireii.db.SQLiteHelper;
import com.example.myapplication.backend.objectProductsApi.ObjectProductsApi;

import java.util.ArrayList;
import java.util.List;


public class StockDataSource {

    private static StockDataSource instance;
    private SQLiteDatabase db;
    private Context context = null;
    private ProductDataSource productDataSource ;
    private WarehouseDataSource warehouseDataSource;
    private ChangeDataSource changeDataSource;

    private  StockDataSource (Context context) {
        productDataSource = ProductDataSource.getInstance(context);
        warehouseDataSource = WarehouseDataSource.getInstance(context);
        changeDataSource = ChangeDataSource.getInstance(context);
        SQLiteHelper sqLiteHelper = SQLiteHelper.getInstance(context);
        db = sqLiteHelper.getWritableDatabase();
        this.context = context;
    }

    public static StockDataSource getInstance(Context context) {
        if (instance == null)
            instance = new StockDataSource(context);

        return instance;
    }

    /* New Stock creation */
    public  long createStock (ObjectStock stock) {
        long id;
        ContentValues values = new ContentValues();
        values.put(InventoryContract.StockEntry.KEY_QUANTITY, stock.getQuantity());
        values.put(InventoryContract.StockEntry.KEY_CONTROLLED, stock.isControlled());
        values.put(InventoryContract.StockEntry.KEY_PRODUCT_ID, stock.getProduct());
        values.put(InventoryContract.StockEntry.KEY_WAREHOUSE_ID, stock.getWarehouse().getId());

        id = this.db.insert(InventoryContract.StockEntry.TABLE_STOCKS, null, values);

        // Save this insert in changes table
        changeDataSource.createChange(new ObjectChange(ObjectChange.TABLE_STOCKS,
                id, ObjectChange.TypeOfChange.insertObject));

        return  id;
    }

    /* Find a stock by Id */

    public ObjectStock getStockById (long id) {
        String sql = "SELECT * FROM " + InventoryContract.StockEntry.TABLE_STOCKS
                + " WHERE " + InventoryContract.ProductEntry.KEY_ID + " = " + id;

        Cursor cursor = this.db.rawQuery(sql, null);

        ObjectStock stock = null;

        if (cursor.moveToFirst()) {
            stock = new ObjectStock();
            stock.setId(cursor.getInt(cursor.getColumnIndex(InventoryContract.StockEntry.KEY_ID)));
            stock.setQuantity(cursor.getInt(cursor.getColumnIndex(InventoryContract.StockEntry.KEY_QUANTITY)));
            stock.setControlled(cursor.getInt(cursor.getColumnIndex(InventoryContract.StockEntry.KEY_CONTROLLED)) > 0);

            // get the Warehouse
            int warehouseId = cursor.getInt(cursor.getColumnIndex(InventoryContract.StockEntry.KEY_WAREHOUSE_ID));
            ObjectWarehouse warehouse = warehouseDataSource.getWarehouseById(warehouseId);
            stock.setWarehouse(warehouse);

            // get the Product
            int productId = cursor.getInt(cursor.getColumnIndex(InventoryContract.StockEntry.KEY_PRODUCT_ID));
            ObjectProducts product = productDataSource.getProductById(productId);
            stock.setProduct(product);
        }
        return stock;
    }



    /* Get all stocks of a product */
    public ArrayList<ObjectStock> getAllStocksByProduct(ObjectProducts product) {
        ArrayList<ObjectStock> stocks = new ArrayList<>();
        String sql = "SELECT s.* FROM " + InventoryContract.StockEntry.TABLE_STOCKS + " AS s, "
                + InventoryContract.WarehouseEntry.TABLE_WAREHOUSES + " AS w"
                + " WHERE s." + InventoryContract.StockEntry.KEY_PRODUCT_ID + " = " + product.getId()
                + " AND w." + InventoryContract.WarehouseEntry.KEY_ID + " = s." + InventoryContract.StockEntry.KEY_WAREHOUSE_ID
                + " ORDER BY w." + InventoryContract.WarehouseEntry.KEY_NAME + " ASC;";

        Cursor cursor = this.db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                ObjectStock stock = new ObjectStock();
                stock.setId(cursor.getInt(cursor.getColumnIndex(InventoryContract.StockEntry.KEY_ID)));
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









    /* Get all stocks of a warehouse */
    public ArrayList<ObjectStock> getAllStocksByWarehouse(long id) {
        ArrayList<ObjectStock> stocks = new ArrayList<>();
        String sql = "SELECT s.* FROM " + InventoryContract.StockEntry.TABLE_STOCKS + " AS s, "
                + InventoryContract.WarehouseEntry.TABLE_WAREHOUSES + " AS w"
                + " WHERE s." + InventoryContract.StockEntry.KEY_WAREHOUSE_ID + " = " + id
                + ";";

        Cursor cursor = this.db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                ObjectStock stock = new ObjectStock();
                stock.setId(cursor.getInt(cursor.getColumnIndex(InventoryContract.StockEntry.KEY_ID)));
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

                // Add this stock to the list
                stocks.add(stock);

            } while (cursor.moveToNext());
        }

        return stocks;
    }

    /* Get all stocks */
    public List<ObjectStock> getAllStocks() {
        List<ObjectStock> stocks = new ArrayList<>();
        String sql = "SELECT * FROM " + InventoryContract.StockEntry.TABLE_STOCKS;

        Cursor cursor = this.db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                ObjectStock stock = new ObjectStock();
                stock.setId(cursor.getInt(cursor.getColumnIndex(InventoryContract.StockEntry.KEY_ID)));
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

                // Add this stock to the list
                stocks.add(stock);

            } while (cursor.moveToNext());
        }

        return stocks;
    }


    // Get number of inventoried objects (pieces) in a Warehouse
    public int getInventoriedObjects(long warehouseId) {
        String sql = "SELECT SUM(" + InventoryContract.StockEntry.KEY_QUANTITY + ") AS Number " +
                        "FROM " + InventoryContract.StockEntry.TABLE_STOCKS +
                        " WHERE " + InventoryContract.StockEntry.KEY_WAREHOUSE_ID + " = " + warehouseId +
                        " AND " + InventoryContract.StockEntry.KEY_CONTROLLED + " > 0";

        int quantity = 0;
        Cursor cursor = this.db.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
            quantity = cursor.getInt(cursor.getColumnIndex("Number"));
        }

        return quantity;
    }

    // Get number of pieces stocked in a Warehouse
    public int getNumberObjects(long warehouseId) {
        String sql = "SELECT SUM(" + InventoryContract.StockEntry.KEY_QUANTITY + ") AS Number " +
                "FROM " + InventoryContract.StockEntry.TABLE_STOCKS +
                " WHERE " + InventoryContract.StockEntry.KEY_WAREHOUSE_ID + " = " + warehouseId;

        int quantity = 0;
        Cursor cursor = this.db.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
            quantity = cursor.getInt(cursor.getColumnIndex("Number"));
        }
        return quantity ;
    }



    /* Update a stock */
    public int updateStock(ObjectStock stock) {
        ContentValues values = new ContentValues();
        values.put(InventoryContract.StockEntry.KEY_QUANTITY, stock.getQuantity());
        values.put(InventoryContract.StockEntry.KEY_CONTROLLED, stock.isControlled());
        values.put(InventoryContract.StockEntry.KEY_WAREHOUSE_ID, stock.getWarehouse().getId());
        values.put(InventoryContract.StockEntry.KEY_PRODUCT_ID, stock.getProduct());

        // Save this update in the changes table
        changeDataSource.createChange(new ObjectChange(ObjectChange.TABLE_STOCKS,
                stock.getId(), ObjectChange.TypeOfChange.updateObject));

        return this.db.update(InventoryContract.StockEntry.TABLE_STOCKS, values,
                InventoryContract.StockEntry.KEY_ID + " = ?",
                new String[]{ String.valueOf(stock.getId())});
    }

    /* Delete a stock */
    public void deleteStock(ObjectStock stock) {

        // Save this delete in the changes table
        changeDataSource.createChange(new ObjectChange(ObjectChange.TABLE_STOCKS,
                stock.getId(), ObjectChange.TypeOfChange.deleteObject));

        this.db.delete(InventoryContract.StockEntry.TABLE_STOCKS,
                InventoryContract.StockEntry.KEY_ID + " = ?",
                new String[]{String.valueOf(stock.getId())});
    }

    /* Delete all stocks of a Warehouse */
    public void deleteAllStocksByWarehouse(long id) {

        // to save all those 'delete' in the changes table, we need the list
        List<ObjectStock> stocksToDelete = getAllStocksByWarehouse(id);

        // save those 'delete in the changes table
        for (ObjectStock s : stocksToDelete) {
            changeDataSource.createChange(new ObjectChange(ObjectChange.TABLE_STOCKS,
                    s.getId(), ObjectChange.TypeOfChange.deleteObject));
        }

        this.db.delete(InventoryContract.StockEntry.TABLE_STOCKS,
                InventoryContract.StockEntry.KEY_WAREHOUSE_ID + " = ?",
                new String[]{String.valueOf(id)});
    }





    /* Get all stocks of a product */
    public ArrayList<com.example.myapplication.backend.objectStockApi.model.ObjectStock> getAllStocksByProduct(com.example.myapplication.backend.objectProductsApi.model.ObjectProducts product) {
        ArrayList<com.example.myapplication.backend.objectStockApi.model.ObjectStock> stocks = new ArrayList<>();
        String sql = "SELECT s.* FROM " + InventoryContract.StockEntry.TABLE_STOCKS + " AS s, "
                + InventoryContract.WarehouseEntry.TABLE_WAREHOUSES + " AS w"
                + " WHERE s." + InventoryContract.StockEntry.KEY_PRODUCT_ID + " = " + product.getId()
                + " AND w." + InventoryContract.WarehouseEntry.KEY_ID + " = s." + InventoryContract.StockEntry.KEY_WAREHOUSE_ID
                + " ORDER BY w." + InventoryContract.WarehouseEntry.KEY_NAME + " ASC;";

        Cursor cursor = this.db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                com.example.myapplication.backend.objectStockApi.model.ObjectStock stock = new com.example.myapplication.backend.objectStockApi.model.ObjectStock();
                stock.setId(cursor.getLong(cursor.getColumnIndex(InventoryContract.StockEntry.KEY_ID)));
                stock.setQuantity(cursor.getInt(cursor.getColumnIndex(InventoryContract.StockEntry.KEY_QUANTITY)));
                stock.setControlled(cursor.getInt(cursor.getColumnIndex(InventoryContract.StockEntry.KEY_CONTROLLED))>0);


                // get the Warehouse
                int warehouseId = cursor.getInt(cursor.getColumnIndex(InventoryContract.StockEntry.KEY_WAREHOUSE_ID));
                ObjectWarehouse warehouse = warehouseDataSource.getWarehouseById(warehouseId);
                stock.setWarehouseID((long) warehouse.getId());

                // the Product is well known
                stock.setProductID(product.getId());

                // Add this stock to the list
                stocks.add(stock);

            } while (cursor.moveToNext());
        }

        return stocks;
    }



    /* Find a stock by Id for synchronization */

    public com.example.myapplication.backend.objectStockApi.model.ObjectStock getStockByIdSync (long id) {
        String sql = "SELECT * FROM " + InventoryContract.StockEntry.TABLE_STOCKS
                + " WHERE " + InventoryContract.ProductEntry.KEY_ID + " = " + id;

        Cursor cursor = this.db.rawQuery(sql, null);

        com.example.myapplication.backend.objectStockApi.model.ObjectStock stock = null;

        if (cursor.moveToFirst()) {
            stock = new com.example.myapplication.backend.objectStockApi.model.ObjectStock();
            stock.setId(cursor.getLong(cursor.getColumnIndex(InventoryContract.StockEntry.KEY_ID)));
            stock.setQuantity(cursor.getInt(cursor.getColumnIndex(InventoryContract.StockEntry.KEY_QUANTITY)));
            stock.setControlled(cursor.getInt(cursor.getColumnIndex(InventoryContract.StockEntry.KEY_CONTROLLED)) > 0);
            stock.setWarehouseID(cursor.getLong(cursor.getColumnIndex(InventoryContract.StockEntry.KEY_WAREHOUSE_ID)));
            stock.setProductID(cursor.getLong(cursor.getColumnIndex(InventoryContract.StockEntry.KEY_PRODUCT_ID)));

        }
        return stock;
    }


}
