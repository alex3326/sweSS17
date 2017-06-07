package at.sw2017.todo4u;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.ViewMatchers;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SeekBar;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import at.sw2017.todo4u.database.Todo4uContract;
import at.sw2017.todo4u.database.Todo4uDbHelper;
import at.sw2017.todo4u.model.TaskCategory;

class TestHelper {

    static void clearDatabase() {
        Todo4uDbHelper dbHelper = new Todo4uDbHelper(InstrumentationRegistry.getTargetContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + Todo4uContract.Task._TABLE_NAME);
        db.execSQL("DELETE FROM " + Todo4uContract.TaskCategory._TABLE_NAME);
        db.execSQL("DELETE FROM " + Todo4uContract.Setting._TABLE_NAME);
        db.close();
    }

    static ViewAction setSeekBarProgress(final int progress) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(SeekBar.class);
            }

            @Override
            public String getDescription() {
                return "Set a progress on a SeekBar";
            }

            @Override
            public void perform(UiController uiController, View view) {
                SeekBar seekBar = (SeekBar) view;
                seekBar.setProgress(progress);
            }
        };
    }

    static Matcher<Object> withSpinnerCategoryText(final String expectedName) {
        return new BoundedMatcher<Object, TaskCategory>(TaskCategory.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("with expectedName: " + expectedName);
            }

            @Override
            protected boolean matchesSafely(TaskCategory item) {
                return item.getName().equalsIgnoreCase(expectedName);
            }
        };

    }

    static Matcher<View> hasListSize(final int size) {
        return new TypeSafeMatcher<View>() {
            int length;

            @Override
            public boolean matchesSafely(final View view) {
                length = ((AdapterView) view).getAdapter().getCount();
                return length == size;
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("ListView should have " + size + " items, the actual size is " + length);
            }
        };
    }

    static Matcher<View> hasBackgroundColor(final int color) {
        return new TypeSafeMatcher<View>() {
            int isColor = -1;

            @Override
            protected boolean matchesSafely(View view) {
                if (view.getBackground() instanceof ColorDrawable) {
                    isColor = ((ColorDrawable) view.getBackground()).getColor();
                    return color == isColor;
                }
                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("View should have color " + color + " but has color " + isColor);
            }
        };
    }
}
