package com.androidprojects.inventaireii.db.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.androidprojects.inventaireii.ObjectChange;
import com.androidprojects.inventaireii.db.InventoryContract.ChangeEntry;
import com.androidprojects.inventaireii.db.SQLiteHelper;

import static com.androidprojects.inventaireii.ObjectChange.*;

public class ChangeDataSource {

    private static ChangeDataSource instance;
    private SQLiteDatabase db;
    private Context context;

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

        // TODO: 10.01.2016 suppress this toast...
        String message = change.getTable()+"/"+id + " created";
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

        return id;
    }

    /**
     * Find a change for one object (by table name and id)
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

    private ObjectChange getChangeFromCursor(Cursor cursor) {
        ObjectChange change = new ObjectChange();
        change.setId(cursor.getInt(cursor.getColumnIndex(ChangeEntry.KEY_ID)));
        change.setTable(cursor.getString(cursor.getColumnIndex(ChangeEntry.KEY_TABLE)));
        change.setElementId(cursor.getInt(cursor.getColumnIndex(ChangeEntry.KEY_ELEMENT_ID)));
        change.setTypeOfChange(TypeOfChange.valueOf(cursor.getString(cursor.getColumnIndex(ChangeEntry.KEY_TYPE_OF_CHANGE))));
        return change;
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
}
