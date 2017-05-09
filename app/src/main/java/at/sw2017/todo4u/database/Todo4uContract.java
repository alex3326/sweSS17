package at.sw2017.todo4u.database;

import android.provider.BaseColumns;

public final class Todo4uContract {
    private Todo4uContract() {
    }

    public static class Task implements BaseColumns {
        public static final String _TABLE_NAME = "task";
        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String DUE_DATE = "dueDate";
        public static final String CREATION_DATE = "creationDate";
        public static final String REMINDER_DATE = "reminderDate";
        public static final String CATEGORY_ID = "categoryId";
        public static final String STATE = "state";
    }

    public static class TaskCategory implements BaseColumns {
        public static final String _TABLE_NAME = "taskcategory";
        public static final String NAME = "name";
    }

}
