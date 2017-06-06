package at.sw2017.todo4u.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.List;

import at.sw2017.todo4u.model.TaskCategory;

public class TaskCategoriesDataSource extends AbstractDataSource<TaskCategory> {

    public TaskCategoriesDataSource(Context context) {
        super(context,
                new String[]{
                        Todo4uContract.TaskCategory._ID,
                        Todo4uContract.TaskCategory.NAME,
                        Todo4uContract.TaskCategory.COLOR
                },
                Todo4uContract.TaskCategory._TABLE_NAME
        );
    }

    @Override
    protected ContentValues getContentValues(TaskCategory object) {
        ContentValues values = new ContentValues();
        values.put(Todo4uContract.TaskCategory.NAME, object.getName());
        values.put(Todo4uContract.TaskCategory.COLOR, object.getColorId());
        return values;
    }

    @Override
    protected TaskCategory cursorToObject(Cursor cursor) {
        TaskCategory obj = new TaskCategory();
        obj.setId(cursor.getLong(0));
        obj.setName(cursor.getString(1));
        obj.setColor(cursor.getInt(2));
        return obj;
    }

    public List<TaskCategory> getCategoriesWithName(String name) {
        return getSelection(
                Todo4uContract.TaskCategory.NAME + " LIKE ?",
                new String[]{"%" + name + "%"},
                null, null, null);
    }

}
