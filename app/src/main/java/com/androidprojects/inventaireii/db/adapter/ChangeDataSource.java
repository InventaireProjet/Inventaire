package com.androidprojects.inventaireii.db.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.androidprojects.inventaireii.ObjectCategories;
import com.androidprojects.inventaireii.ObjectChange;
import com.androidprojects.inventaireii.ObjectProducts;
import com.androidprojects.inventaireii.ObjectStock;
import com.androidprojects.inventaireii.ObjectWarehouse;
import com.androidprojects.inventaireii.db.InventoryContract.ChangeEntry;
import com.androidprojects.inventaireii.db.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;

import static com.androidprojects.inventaireii.ObjectChange.*;

public class ChangeDataSource {

    private static ChangeDataSource instance;
    private SQLiteDatabase db;
    private Context context;
    private CategoryDataSource categoryDataSource;
    private WarehouseDataSource warehouseDataSource;
    private ProductDataSource productDataSource;
    private StockDataSource stockDataSource;

    // Private constructor (Singleton)
    private ChangeDataSource (Context context) {
        SQLiteHelper sqLiteHelper = SQLiteHelper.getInstance(context);
        db = sqLiteHelper.getWritableDatabase();
        this.context = context;
    }

    public static ChangeDataSource getInstance(Context context) {
        if (instance == null)
            instance = new ChangeDataSource(context);

        return instance;
    }

    /**
     * Insertion of a new line
     *
     * An important part of the strategy of synchronization is managed
     * at this moment : which data do we have to synchronize ?
     * For this reason, _createChange is private
     */
    public long createChange(ObjectChange change) {
        long id = 0;

        // According to the type of change, the strategy is not the same.
        // Our strategy doesn't allow to have more than one line for a
        // specific object.
        // An 'object' is defined by its table and id.
        TypeOfChange typeOfChange = change.getTypeOfChange();
        switch (typeOfChange) {
            case insertObject:
                // Since the object did not exist before the insertion, we have
                // to insert it.
                _createChange(change);
                break;
            case updateObject:
                // if there is already a mention of this object, it is updateObject or insertObject;
                // (a previously deleted object can not be now updated)
                // if it's updateObject, there is no need to update it twice.
                // if it's createObject, there is no need to update it. It will be
                // created in its current state...
                // we just let the mention and don't add anything :
                if (getChangeByObject(change.getTable(), change.getElementId()) == null)
                    _createChange(change);
                break;
            case deleteObject:
                // first we look if there is a mention of this object
                ObjectChange existingChange = getChangeByObject(change.getTable(), change.getElementId());

                // if the mention is a 'insert', we just have to suppress this 'insert'
                // the object will never be created in the cloud
                if (existingChange.getTypeOfChange().equals(TypeOfChange.insertObject)) {
                    deleteChange(existingChange.getId());
                    break;
                }

                // if the mention is a 'update', we don't need any more to update, so we
                // suppress this mention.
                // we don't break because we have to add the 'delete' line.
                if (existingChange.getTypeOfChange().equals(TypeOfChange.updateObject)) {
                    deleteChange(existingChange.getId());
                }

                // if there is no mention, or an 'update' we add the delete line:
                _createChange(change);
                break;
        }
        return id;
    }

    private long _createChange(ObjectChange change) {
        long id;
        ContentValues values = new ContentValues();
        values.put(ChangeEntry.KEY_TABLE, change.getTable());
        values.put(ChangeEntry.KEY_ELEMENT_ID, change.getElementId());
        values.put(ChangeEntry.KEY_TYPE_OF_CHANGE, change.getTypeOfChange().toString());

        id = this.db.insert(ChangeEntry.TABLE_CHANGES, null, values);

        return id;
    }

    /**
     * Delete a 'change'
     */
    public void deleteChange(int id) {
        this.db.delete(ChangeEntry.TABLE_CHANGES,
                ChangeEntry.KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    /**
     * Delete all lines in the table (after synchronization)
     */
    public void deleteAllChanges() {
        this.db.delete(ChangeEntry.TABLE_CHANGES, null, null);
    }

    /**
     * Get functions :
     * private ObjectChange getChangeByObject(String tableName, long elementId)
     * private ArrayList<ObjectChange> getChangesByTableAndTypeOfChange(String tableName, TypeOfChange typeOfChange)
     *
     * public ArrayList<ObjectCategories> getCategoriesToUpdate()
     * public ArrayList<ObjectCategories> getCategoriesToInsert()
     * public ArrayList<ObjectCategories> getCategoriesToDelete()
     *
     * public ArrayList<ObjectCategories> getCategoriesTo(TypeOfChange typeOfChange)
     * public ArrayList<ObjectWarehouse> getWarehousesTo(TypeOfChange typeOfChange)
     * public ArrayList<ObjectProducts> getProductsTo(TypeOfChange typeOfChange)
     * public ArrayList<ObjectStock> getStocksTo(TypeOfChange typeOfChange)
     *
     * private ObjectChange getChangeFromCursor(Cursor cursor)
     */

    /**
     * Get a change for one object (by table name and id)
     */
    private ObjectChange getChangeByObject(String tableName, long elementId) {
        ObjectChange change = null;
        String sql = "SELECT * FROM " + ChangeEntry.TABLE_CHANGES
                + " WHERE " + ChangeEntry.KEY_TABLE + " = '" + tableName + "'"
                + " AND " + ChangeEntry.KEY_ID + " = " + elementId;

        Cursor cursor = this.db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            change = getChangeFromCursor(cursor);
            cursor.close();
        }
        return change;
    }

    /**
     * Get a list of a type of change for one table
     * For example, all 'delete' from table 'Categories'
     */
    private ArrayList<ObjectChange> getChangesByTableAndTypeOfChange(String tableName, TypeOfChange typeOfChange) {
        ArrayList<ObjectChange> changes = new ArrayList<ObjectChange>();
        String sql = "SELECT * FROM " + ChangeEntry.TABLE_CHANGES
                + " WHERE " + ChangeEntry.KEY_TABLE + " = '" + tableName + "'"
                + " AND " + ChangeEntry.KEY_TYPE_OF_CHANGE + " = '" + typeOfChange.toString() + "';";

        Cursor cursor = this.db.rawQuery(sql, null);

        if(cursor.moveToFirst()) {
            do {
                ObjectChange change = getChangeFromCursor(cursor);

                changes.add(change);
            } while (cursor.moveToNext());

            cursor.close();
        }
        return changes;
    }
    /**
     * Get all categories to update
     */
    public ArrayList<ObjectCategories> getCategoriesToUpdate() {
        ArrayList<ObjectCategories> categories = new ArrayList<ObjectCategories>();
        categoryDataSource = CategoryDataSource.getInstance(context);

        // Get all lines in table 'changes'
        ArrayList<ObjectChange> changes = getChangesByTableAndTypeOfChange(ObjectChange.TABLE_CATEGORIES, TypeOfChange.updateObject);

        // Get corresponding categories
        for (ObjectChange change : changes) {
            categories.add(categoryDataSource.getCategoryById(change.getElementId()));
        }

        return categories;
    }

    /**
     * Get all categories to insert
     */
    public ArrayList<ObjectCategories> getCategoriesToInsert() {
        ArrayList<ObjectCategories> categories = new ArrayList<ObjectCategories>();
        categoryDataSource = CategoryDataSource.getInstance(context);

        // Get all lines in table 'changes'
        ArrayList<ObjectChange> changes = getChangesByTableAndTypeOfChange(ObjectChange.TABLE_CATEGORIES, TypeOfChange.insertObject);

        // Get corresponding categories
        for (ObjectChange change : changes) {
            categories.add(categoryDataSource.getCategoryById(change.getElementId()));
        }

        return categories;
    }

    /**
     * Get all categories to delete
     */
    public ArrayList<ObjectCategories> getCategoriesToDelete() {
        ArrayList<ObjectCategories> categories = new ArrayList<ObjectCategories>();
        categoryDataSource = CategoryDataSource.getInstance(context);

        // Get all lines in table 'changes'
        ArrayList<ObjectChange> changes = getChangesByTableAndTypeOfChange(ObjectChange.TABLE_CATEGORIES, TypeOfChange.deleteObject);

        // Get corresponding categories
        for (ObjectChange change : changes) {
            categories.add(categoryDataSource.getCategoryById(change.getElementId()));
        }

        return categories;
    }

    /**
     * Get all categories to delete, update, insert
     */
    public ArrayList<ObjectCategories> getCategoriesTo(TypeOfChange typeOfChange) {
        ArrayList<ObjectCategories> categories = new ArrayList<ObjectCategories>();
        categoryDataSource = CategoryDataSource.getInstance(context);

        // Get all lines in table 'changes'
        ArrayList<ObjectChange> changes = getChangesByTableAndTypeOfChange(ObjectChange.TABLE_CATEGORIES, typeOfChange);

        // Get corresponding categories
        for (ObjectChange change : changes) {
            categories.add(categoryDataSource.getCategoryById(change.getElementId()));
        }

        return categories;
    }

    /**
     * Get all warehouses to update, delete or insert
     */
    public ArrayList<ObjectWarehouse> getWarehousesTo(TypeOfChange typeOfChange) {
        ArrayList<ObjectWarehouse> warehouses = new ArrayList<ObjectWarehouse>();
        warehouseDataSource = WarehouseDataSource.getInstance(context);

        // Get all lines in table 'changes'
        ArrayList<ObjectChange> changes = getChangesByTableAndTypeOfChange(ObjectChange.TABLE_WAREHOUSES, typeOfChange);

        // Get corresponding warehouses
        for (ObjectChange change : changes) {
            warehouses.add(warehouseDataSource.getWarehouseById(change.getElementId()));
        }

        return warehouses;
    }

    /**
     * Get all products to update, delete or insert
     */
    public ArrayList<ObjectProducts> getProductsTo(TypeOfChange typeOfChange) {
        ArrayList<ObjectProducts> products = new ArrayList<ObjectProducts>();
        productDataSource = ProductDataSource.getInstance(context);

        // Get all lines in table 'changes'
        ArrayList<ObjectChange> changes = getChangesByTableAndTypeOfChange(ObjectChange.TABLE_PRODUCTS, typeOfChange);

        // Get corresponding products
        for (ObjectChange change : changes) {
            products.add(productDataSource.getProductById(change.getElementId()));
        }

        return products;
    }

    /**
     * Get all stocks to update, delete or insert
     */
    public ArrayList<ObjectStock> getStocksTo(TypeOfChange typeOfChange) {
        ArrayList<ObjectStock> stocks = new ArrayList<ObjectStock>();
        stockDataSource = StockDataSource.getInstance(context);

        // Get all lines in table 'changes'
        ArrayList<ObjectChange> changes = getChangesByTableAndTypeOfChange(ObjectChange.TABLE_STOCKS, typeOfChange);

        // Get corresponding products
        for (ObjectChange change : changes) {
            stocks.add(stockDataSource.getStockById(change.getElementId()));
        }

        return stocks;
    }



    /**
     * Get the change from the cursor
     */
    private ObjectChange getChangeFromCursor(Cursor cursor) {
        ObjectChange change = new ObjectChange();
        change.setId(cursor.getInt(cursor.getColumnIndex(ChangeEntry.KEY_ID)));
        change.setTable(cursor.getString(cursor.getColumnIndex(ChangeEntry.KEY_TABLE)));
        change.setElementId(cursor.getInt(cursor.getColumnIndex(ChangeEntry.KEY_ELEMENT_ID)));
        change.setTypeOfChange(TypeOfChange.valueOf(cursor.getString(cursor.getColumnIndex(ChangeEntry.KEY_TYPE_OF_CHANGE))));
        return change;
    }


}
