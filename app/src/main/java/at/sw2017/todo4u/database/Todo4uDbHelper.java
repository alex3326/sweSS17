package at.sw2017.todo4u.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Todo4uDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Todo4u.db";
    private static final String SQL_CREATE_TASK =
            "CREATE TABLE " + Todo4uContract.Task._TABLE_NAME + " (" +
                    Todo4uContract.Task._ID + " INTEGER PRIMARY KEY," +
                    Todo4uContract.Task.TITLE + " TEXT," +
                    Todo4uContract.Task.DESCRIPTION + " TEXT," +
                    Todo4uContract.Task.DUE_DATE + " INTEGER," +
                    Todo4uContract.Task.CREATION_DATE + " INTEGER," +
                    Todo4uContract.Task.REMINDER_DATE + " INTEGER," +
                    Todo4uContract.Task.CATEGORY_ID + " INTEGER," +
                    Todo4uContract.Task.STATE + " INTEGER);";
    private static final String SQL_CREATE_TASKCATEGORY =
            "CREATE TABLE " + Todo4uContract.TaskCategory._TABLE_NAME + " (" +
                    Todo4uContract.TaskCategory._ID + " INTEGER PRIMARY KEY," +
                    Todo4uContract.TaskCategory.NAME + " TEXT UNIQUE);";
    private static final String SQL_DELETE_TASK =
            "DROP TABLE IF EXISTS " + Todo4uContract.Task._TABLE_NAME;
    private static final String SQL_DELETE_TASKCATEGORY =
            "DROP TABLE IF EXISTS " + Todo4uContract.TaskCategory._TABLE_NAME;
    private final Context context;

    public Todo4uDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TASK);
        db.execSQL(SQL_CREATE_TASKCATEGORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO: Maybe change this
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_TASKCATEGORY);
        db.execSQL(SQL_DELETE_TASK);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public Context getContext() {
        return context;
    }
}
