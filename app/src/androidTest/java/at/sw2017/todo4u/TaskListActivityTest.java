package at.sw2017.todo4u;

import android.content.Context;
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

import static android.support.test.espresso.Espresso.onData;
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
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;

public class TaskListActivityTest {
    @Rule
    public final ActivityTestRule<CategoryListActivity> mActivityRule = new ActivityTestRule<>(CategoryListActivity.class);
    private Context context;
    private TaskCategoriesDataSource tcDs;
    private TasksDataSource tDs;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getTargetContext();
        tcDs = new TaskCategoriesDataSource(context);
        tDs = new TasksDataSource(context);

        clearDatabase();
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

    private void callOnResumeWorkaround() {
        onView(withId(R.id.bt_add_category)).perform(click());
        onView(withId(R.id.category_add_btCancel)).perform(click());
    }


    @Test
    public void openTasklistOfCategory() {
        TaskCategory cat = new TaskCategory("MyTaskCategory");
        tcDs.open();
        tcDs.insertOrUpdate(cat);
        tcDs.close();
        Task t1 = new Task("TestTask 1");
        t1.setState(Task.State.IN_PROGRESS);
        t1.setCategory(cat);
        Calendar cal = Calendar.getInstance();
        cal.set(2017, 7, 2, 10, 30);
        t1.setDueDate(cal.getTime());
        Task t2 = new Task("My second Task");
        t2.setState(Task.State.OPEN);
        t2.setCategory(cat);

        tDs.open();
        tDs.insertOrUpdate(t1);
        tDs.insertOrUpdate(t2);
        tDs.close();


        callOnResumeWorkaround();

        onData(anything()).inAdapterView(withId(R.id.category_list_view)).atPosition(0).perform(click());
        onView(withId(R.id.task_list_view)).check(matches(isDisplayed()));
        onData(anything()).inAdapterView(withId(R.id.task_list_view)).atPosition(0)
                .onChildView(withText("TestTask 1")).check(matches(isDisplayed()));
        onData(anything()).inAdapterView(withId(R.id.task_list_view)).atPosition(1)
                .onChildView(withText("My second Task")).check(matches(isDisplayed())).perform(click());

        onView(withText(allOf(startsWith("Selected task "), endsWith("My second Task"))))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));

    }

    @Test
    public void testSearch() {
        TaskCategory cat = new TaskCategory("MyTaskCategory");
        tcDs.open();
        tcDs.insertOrUpdate(cat);
        tcDs.close();
        Task t1 = new Task("test");
        t1.setCategory(cat);
        Task t2 = new Task("tes");
        t2.setCategory(cat);
        Task t3 = new Task("te");
        t3.setCategory(cat);
        Task t4 = new Task("t");
        t4.setCategory(cat);

        tDs.open();
        tDs.insertOrUpdate(t1);
        tDs.insertOrUpdate(t2);
        tDs.insertOrUpdate(t3);
        tDs.insertOrUpdate(t4);
        tDs.close();


        callOnResumeWorkaround();

        onData(anything()).inAdapterView(withId(R.id.category_list_view)).atPosition(0).perform(click());
        onView(withId(R.id.task_list_view)).check(matches(isDisplayed()));


        onView(withId(R.id.bt_search_task)).perform(click());
        onView(withId(android.support.design.R.id.search_src_text))
                .perform(typeText("t"), closeSoftKeyboard());
        onData(anything()).inAdapterView(withId(R.id.task_list_view)).atPosition(0).onChildView(withText("test")).check(matches(isDisplayed()));
        onData(anything()).inAdapterView(withId(R.id.task_list_view)).atPosition(1).onChildView(withText("tes")).check(matches(isDisplayed()));
        onData(anything()).inAdapterView(withId(R.id.task_list_view)).atPosition(2).onChildView(withText("te")).check(matches(isDisplayed()));
        onData(anything()).inAdapterView(withId(R.id.task_list_view)).atPosition(3).onChildView(withText("t")).check(matches(isDisplayed()));

        onView(withId(android.support.design.R.id.search_src_text))
                .perform(typeText("e"), closeSoftKeyboard());
        onData(anything()).inAdapterView(withId(R.id.task_list_view)).atPosition(0).onChildView(withText("test")).check(matches(isDisplayed()));
        onData(anything()).inAdapterView(withId(R.id.task_list_view)).atPosition(1).onChildView(withText("tes")).check(matches(isDisplayed()));
        onData(anything()).inAdapterView(withId(R.id.task_list_view)).atPosition(2).onChildView(withText("te")).check(matches(isDisplayed()));

        onView(withId(android.support.design.R.id.search_src_text))
                .perform(typeText("s\n"), closeSoftKeyboard());
        onData(anything()).inAdapterView(withId(R.id.task_list_view)).atPosition(0).onChildView(withText("test")).check(matches(isDisplayed()));
        onData(anything()).inAdapterView(withId(R.id.task_list_view)).atPosition(1).onChildView(withText("tes")).check(matches(isDisplayed()));

        onView(withId(android.support.design.R.id.search_src_text))
                .perform(typeText("t"), closeSoftKeyboard());
        onData(anything()).inAdapterView(withId(R.id.task_list_view)).atPosition(0).onChildView(withText("test")).check(matches(isDisplayed()));
    }

    @Test
    public void insertTasks() {
        TaskCategory cat = new TaskCategory("MyTaskCategory");
        tcDs.open();
        tcDs.insertOrUpdate(cat);
        tcDs.close();

        callOnResumeWorkaround();

        onData(anything()).inAdapterView(withId(R.id.category_list_view)).atPosition(0).perform(click());
        onView(withId(R.id.task_list_view)).check(matches(isDisplayed()));

        insertTask1();
        insertTask2();

    }

    private void insertTask1() {
        onView(withId(R.id.bt_add_task)).perform(click());

        onView(withId(R.id.task_add_btSave)).perform(click());
        onView(withText(R.string.task_add_error_required_fields))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
        onView(withId(R.id.task_add_tfTitle)).perform(typeText("My first task"), closeSoftKeyboard());
        onView(withId(R.id.task_add_btSave)).perform(click());
        onView(withText(R.string.task_add_error_required_fields))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
        onView(withId(R.id.task_add_tfDescription)).perform(typeText("My first task description"), closeSoftKeyboard());
        onView(withId(R.id.task_add_btDueDate)).perform(click());
        Calendar cal = Calendar.getInstance();
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)
                ));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.task_add_btSave)).perform(click());

        onData(anything()).inAdapterView(withId(R.id.task_list_view)).atPosition(0)
                .onChildView(withText("My first task")).check(matches(isDisplayed()));
    }

    private void insertTask2() {
        onView(withId(R.id.bt_add_task)).perform(click());

        onView(withId(R.id.task_add_tfTitle)).perform(typeText("My second task"), closeSoftKeyboard());
        onView(withId(R.id.task_add_tfDescription)).perform(typeText("My second task description"), closeSoftKeyboard());
        onView(withId(R.id.task_add_btReminderDate)).perform(click());
        Calendar cal = Calendar.getInstance();
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)
                ));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.task_add_btSave)).perform(click());
        onView(withText(R.string.task_add_error_required_fields))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
        onView(withId(R.id.task_add_btDueDate)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)
                ));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.task_add_btSave)).perform(click());

        onData(anything()).inAdapterView(withId(R.id.task_list_view)).atPosition(1)
                .onChildView(withText("My second task")).check(matches(isDisplayed()));
    }

}