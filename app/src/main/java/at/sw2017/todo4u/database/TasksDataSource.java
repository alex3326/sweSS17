package at.sw2017.todo4u.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.List;

import at.sw2017.todo4u.model.Task;
import at.sw2017.todo4u.model.TaskCategory;

public class TasksDataSource extends AbstractDataSource<Task> {
    public TasksDataSource(Context context) {
        super(context,
                new String[]{
                        Todo4uContract.Task._ID, Todo4uContract.Task.TITLE,
                        Todo4uContract.Task.DESCRIPTION, Todo4uContract.Task.DUE_DATE,
                        Todo4uContract.Task.CREATION_DATE, Todo4uContract.Task.REMINDER_DATE,
                        Todo4uContract.Task.CATEGORY_ID, Todo4uContract.Task.PROGRESS
                },
                Todo4uContract.Task._TABLE_NAME
        );
    }

    @Override
    protected ContentValues getContentValues(Task task) {
        ContentValues values = new ContentValues();

        values.put(Todo4uContract.Task.TITLE, task.getTitle());

        values.put(Todo4uContract.Task.DESCRIPTION, task.getDescription());

        if (task.getDueDate() == null) {
            values.putNull(Todo4uContract.Task.DUE_DATE);
        } else {
            values.put(Todo4uContract.Task.DUE_DATE, task.getDueDateAsNumber());
        }

        if (task.getCreationDate() == null) {
            values.putNull(Todo4uContract.Task.CREATION_DATE);
        } else {
            values.put(Todo4uContract.Task.CREATION_DATE, task.getCreationDateAsNumber());
        }

        if (task.getReminderDate() == null) {
            values.putNull(Todo4uContract.Task.REMINDER_DATE);
        } else {
            values.put(Todo4uContract.Task.REMINDER_DATE, task.getReminderDateAsNumber());
        }

        if (task.getCategory() != null) {
            values.put(Todo4uContract.Task.CATEGORY_ID, task.getCategory().getId());
        } else {
            values.putNull(Todo4uContract.Task.CATEGORY_ID);
        }
        values.put(Todo4uContract.Task.PROGRESS, task.getProgress());

        return values;
    }

    @Override
    protected Task cursorToObject(Cursor cursor) {
        Task task = new Task();
        task.setId(cursor.getLong(0));
        task.setTitle(cursor.getString(1));
        if (!cursor.isNull(2)) {
            task.setDescription(cursor.getString(2));
        }
        if (!cursor.isNull(3)) {
            task.setDueDate(cursor.getLong(3));
        }
        if (!cursor.isNull(4)) {
            task.setCreationDate(cursor.getLong(4));
        }
        if (!cursor.isNull(5)) {
            task.setReminderDate(cursor.getLong(5));
        }
        if (!cursor.isNull(6)) {
            TaskCategoriesDataSource ds = new TaskCategoriesDataSource(getContext());
            boolean isOpen = ds.isDatabaseOpen();
            if (!isOpen) {
                ds.openReadonly();
            }
            TaskCategory category = ds.getById(cursor.getLong(6));
            task.setCategory(category);
            if (!isOpen) {
                ds.close();
            }
        }
        if (!cursor.isNull(7)) {
            task.setProgress(cursor.getInt(7));
        }
        return task;
    }

    public List<Task> getTasksInCategory(TaskCategory category) {
        if (category == null) return getAll();
        return getTasksInCategory(category.getId());
    }

    public List<Task> getNotFinishedTasksInCategory(TaskCategory category) {
        String selection = Todo4uContract.Task.PROGRESS + " != 100";
        String[] selectionArgs = null;
        if (category != null) {
            selection += " AND " + Todo4uContract.Task.CATEGORY_ID + " = ?";
            selectionArgs = new String[]{Long.toString(category.getId())};
        }

        return getSelection(selection, selectionArgs);
    }

    public List<Task> getTasksInCategoryWithTitle(long categoryId, String title) {
        return getSelection(
                Todo4uContract.Task.CATEGORY_ID + " = ? AND " +
                        Todo4uContract.Task.TITLE + " LIKE ?",
                new String[]{Long.toString(categoryId), "%" + title + "%"}
        );
    }

    public List<Task> getTasksInCategory(long categoryId) {
        return getSelection(
                Todo4uContract.Task.CATEGORY_ID + " = ?",
                new String[]{Long.toString(categoryId)}
        );
    }
}
