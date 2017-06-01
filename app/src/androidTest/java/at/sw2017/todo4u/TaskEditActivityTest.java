package at.sw2017.todo4u;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.PickerActions;
import android.support.test.rule.ActivityTestRule;
import android.widget.DatePicker;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Calendar;

import at.sw2017.todo4u.database.TaskCategoriesDataSource;
import at.sw2017.todo4u.database.TasksDataSource;
import at.sw2017.todo4u.database.Todo4uContract;
import at.sw2017.todo4u.database.Todo4uDbHelper;
import at.sw2017.todo4u.model.Task;
import at.sw2017.todo4u.model.TaskCategory;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import static org.junit.Assert.*;

public class TaskEditActivityTest {

    @Rule
    public ActivityTestRule<TaskAddActivity> mActivityRule = new ActivityTestRule<>(TaskAddActivity.class, true, false);
    private Context context;
    private TaskCategoriesDataSource tcDs;
    private TasksDataSource tDs;
    private TaskCategory category;


    @Before
    public void setUp() {
        context = InstrumentationRegistry.getTargetContext();
        tcDs = new TaskCategoriesDataSource(context);
        tDs = new TasksDataSource(context);

        clearDatabase();

        category = new TaskCategory("My Category");
        tcDs.open();
        tcDs.insertOrUpdate(category);
        tcDs.close();

    }

    @After
    public void tearDown() {
        clearDatabase();
    }

    private void clearDatabase() {
        Todo4uDbHelper dbHelper = new Todo4uDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM " + Todo4uContract.Task._TABLE_NAME);
        db.execSQL("DELETE FROM " + Todo4uContract.TaskCategory._TABLE_NAME);
        db.close();
    }


    @Test
    public void editSimpleTask() {

        Task t1 = new Task("MyEditTask");
        t1.setCategory(category);
        tDs.open();
        tDs.insertOrUpdate(t1);
        tDs.close();

        Intent intent = new Intent();
        intent.putExtra("task", t1.getId());
        mActivityRule.launchActivity(intent);

        onView(withId(R.id.task_add_btSave)).perform(click());
        onView(withText(R.string.task_add_error_required_fields))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));

    }


    @Test
    public void editTaskWithDate() {

        Task t1 = new Task("My Task with Dates");
        t1.setCategory(category);
        Calendar dueDate = Calendar.getInstance();
        dueDate.add(Calendar.DAY_OF_MONTH, 14);
        Calendar remindDate = Calendar.getInstance();
        remindDate.add(Calendar.DAY_OF_MONTH, 10);
        t1.setDueDate(dueDate.getTimeInMillis());
        t1.setReminderDate(remindDate.getTimeInMillis());

        tDs.open();
        tDs.insertOrUpdate(t1);
        tDs.close();

        Intent intent = new Intent();
        intent.putExtra("task", t1.getId());
        mActivityRule.launchActivity(intent);

        onView(withId(R.id.task_add_btSave)).perform(click());
        onView(withText(R.string.task_add_error_required_fields))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));

        onView(withId(R.id.task_add_tfDescription)).perform(typeText("Here you can describe your task"), closeSoftKeyboard());
        onView(withId(R.id.task_add_btDueDate)).perform(click());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 18);
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH) + 1,
                        cal.get(Calendar.DAY_OF_MONTH)
                ));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.task_add_seekProgress)).perform(TestHelper.setSeekBarProgress(100));
        onView(withId(R.id.task_add_btSave)).perform(click());

        tDs.openReadonly();
        t1 = tDs.getById(t1.getId());
        tDs.close();

        assertTrue(t1.isFinished());
    }

}