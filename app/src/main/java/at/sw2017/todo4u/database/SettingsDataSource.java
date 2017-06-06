package at.sw2017.todo4u.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.List;

import at.sw2017.todo4u.model.Setting;

public class SettingsDataSource extends AbstractDataSource<Setting> {

    public SettingsDataSource(Context context) {
        super(context, new String[]{
                    Todo4uContract.Setting._ID,
                    Todo4uContract.Setting.KEY,
                    Todo4uContract.Setting.VALUE
                },
                Todo4uContract.Setting._TABLE_NAME);
    }

    @Override
    protected ContentValues getContentValues(Setting object) {
        ContentValues values = new ContentValues();
        values.put(Todo4uContract.Setting.KEY, object.getKey());
        values.put(Todo4uContract.Setting.VALUE, object.getValue());

        return values;
    }

    @Override
    protected Setting cursorToObject(Cursor cursor) {
        Setting obj = new Setting();
        obj.setId(cursor.getLong(0));
        obj.setKey(cursor.getString(1));
        obj.setValue(cursor.getInt(2));
        return obj;
    }

    public Setting getSettingByKey(String key) {
        List<Setting> result = getSelection(Todo4uContract.Setting.KEY + " = ?", new String[]{key});
        return result.isEmpty() ? new Setting(key) : result.get(0);
    }
}
