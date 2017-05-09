package at.sw2017.todo4u.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import at.sw2017.todo4u.model.BaseModel;

abstract class AbstractDataSource<T extends BaseModel> {
    protected SQLiteDatabase database;
    protected final String[] allColumns;
    protected final String tableName;
    private final Todo4uDbHelper dbHelper;

    AbstractDataSource(Context context, String[] allColumns, String tableName) {
        dbHelper = new Todo4uDbHelper(context);
        this.allColumns = allColumns;
        this.tableName = tableName;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void openReadonly() throws SQLException {
        database = dbHelper.getReadableDatabase();
    }

    public boolean isDatabaseOpen() {
        return (database != null && database.isOpen());
    }

    public boolean isDatabaseWritable() {
        return (database != null && database.isOpen() && !database.isReadOnly());
    }

    public void close() {
        dbHelper.close();
    }

    public boolean insertOrUpdate(T object) {
        ContentValues values = getContentValues(object);

        if (object.getId() == 0) {
            long insertId = database.insert(tableName, null, values);
            if (insertId != -1) {
                object.setId(insertId);
                return true;
            } else {
                return false;
            }
        } else {
            int affectedRows = database.update(
                    tableName,
                    values,
                    BaseColumns._ID + " = ?",
                    new String[]{Long.toString(object.getId())}
            );

            return (affectedRows != 0);
        }
    }

    protected abstract ContentValues getContentValues(T object);

    public T getById(long id) {
        Cursor cursor = database.query(
                tableName,
                allColumns,
                BaseColumns._ID + " = ?",
                new String[]{Long.toString(id)},
                null, null, null
        );
        cursor.moveToFirst();
        T obj = cursorToObject(cursor);
        cursor.close();
        return obj;
    }

    public boolean delete(T obj) {
        return delete(obj.getId());
    }

    public boolean delete(long id) {
        int affectedRows = database.delete(
                tableName,
                BaseColumns._ID + " = ?",
                new String[]{Long.toString(id)}
        );
        return (affectedRows != 0);
    }

    public List<T> getAll() {
        return getSelection(null, null, null, null, null);
    }

    protected List<T> getSelection(String selection, String[] selectionArgs) {
        return getSelection(selection, selectionArgs, null, null, null);
    }

    protected List<T> getSelection(String selection,
                                String[] selectionArgs, String groupBy, String having,
                                String orderBy) {
        List<T> objects = new ArrayList<>();

        Cursor cursor = database.query(
                tableName, allColumns,
                selection, selectionArgs,
                groupBy, having, orderBy
        );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            T obj = cursorToObject(cursor);
            objects.add(obj);
            cursor.moveToNext();
        }
        cursor.close();
        return objects;
    }

    protected abstract T cursorToObject(Cursor cursor);

    protected Context getContext() {
        return dbHelper.getContext();
    }
}
