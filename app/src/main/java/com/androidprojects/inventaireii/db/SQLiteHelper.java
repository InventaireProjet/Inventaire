package com.androidprojects.inventaireii.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.androidprojects.inventaireii.db.InventoryContract.CategorieEntry;
import static com.androidprojects.inventaireii.db.InventoryContract.ProductEntry;
import static com.androidprojects.inventaireii.db.InventoryContract.StockEntry;
import static com.androidprojects.inventaireii.db.InventoryContract.WarehouseEntry;
import static com.androidprojects.inventaireii.db.InventoryContract.ChangeEntry;

public class SQLiteHelper extends SQLiteOpenHelper {

    // Infos about database
    private static final String DATABASE_NAME = "inventory";
    private static final int DATABASE_VERSION = 1;
    private static SQLiteHelper instance;
    private SQLiteDatabase db;

    // SINGLETON !
    private SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = this.getWritableDatabase();
    }

    public static SQLiteHelper getInstance(Context context) {
        if (instance == null) {
            instance = new SQLiteHelper(context.getApplicationContext());
        }

        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CategorieEntry.CREATE_TABLE_CATEGORIES);
        db.execSQL(WarehouseEntry.CREATE_TABLE_WAREHOUSES);
        db.execSQL(ProductEntry.CREATE_TABLE_PRODUCTS);
        db.execSQL(StockEntry.CREATE_TABLE_STOCKS);
        db.execSQL(ChangeEntry.CREATE_TABLE_CHANGES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + StockEntry.TABLE_STOCKS);
        db.execSQL("DROP TABLE IF EXISTS " + ProductEntry.TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + WarehouseEntry.TABLE_WAREHOUSES);
        db.execSQL("DROP TABLE IF EXISTS " + CategorieEntry.TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + ChangeEntry.TABLE_CHANGES);

        // Create new tables
        onCreate(db);
    }
}
