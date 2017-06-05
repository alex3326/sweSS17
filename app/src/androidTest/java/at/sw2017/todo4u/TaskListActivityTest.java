package at.sw2017.todo4u;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;
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
import static android.support.test.espresso.Espresso.openContextualActionModeOverflowMenu;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

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
        t1.setProgress(30);
        t1.setCategory(cat);
        Calendar cal = Calendar.getInstance();
        cal.set(2017, 7, 2, 10, 30);
        t1.setDueDate(cal.getTime());
        Task t2 = new Task("My second Task");
        t2.setProgress(0);
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

        onView(withId(R.id.task_add_tfTitle)).check(matches(withText("My second Task")));

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


        try {
            onView(withId(R.id.bt_search_task)).perform(click());
        } catch (NoMatchingViewException ex) {
            openContextualActionModeOverflowMenu();
            onView(withText(R.string.task_list_search)).perform(click());
        }
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
        TaskCategory cat1 = new TaskCategory("MyTaskCategory");
        TaskCategory cat2 = new TaskCategory("Second Category");
        tcDs.open();
        tcDs.insertOrUpdate(cat1);
        tcDs.insertOrUpdate(cat2);
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
                        cal.get(Calendar.MONTH) + 1,
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
        onView(withId(R.id.task_add_seekProgress)).perform(TestHelper.setSeekBarProgress(67));
        onView(withId(R.id.task_add_spinnerCategory)).perform(click());
        onData(TestHelper.withSpinnerCategoryText("Second Category")).perform(click());
        onView(withId(R.id.task_add_spinnerCategory)).perform(click());
        onData(TestHelper.withSpinnerCategoryText("MyTaskCategory")).perform(click());
        onView(withId(R.id.task_add_btReminderDate)).perform(click());
        Calendar calRemind1 = Calendar.getInstance();
        calRemind1.add(Calendar.MONTH, 1);
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(
                        calRemind1.get(Calendar.YEAR),
                        calRemind1.get(Calendar.MONTH) + 1,
                        calRemind1.get(Calendar.DAY_OF_MONTH)
                ));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.task_add_btSave)).perform(click());
        onView(withText(R.string.task_add_error_required_fields))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
        onView(withId(R.id.task_add_btDueDate)).perform(click());
        Calendar calDue = Calendar.getInstance();
        calDue.add(Calendar.DAY_OF_MONTH, 8);
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(
                        calDue.get(Calendar.YEAR),
                        calDue.get(Calendar.MONTH) + 1,
                        calDue.get(Calendar.DAY_OF_MONTH)
                ));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.task_add_btSave)).perform(click());
        onView(withText(R.string.task_add_error_dates_reminderAfterDue))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
        onView(withId(R.id.task_add_btReminderDate)).perform(click());
        Calendar calRemind2 = Calendar.getInstance();
        calRemind2.add(Calendar.DAY_OF_MONTH, 3);
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(
                        calRemind2.get(Calendar.YEAR),
                        calRemind2.get(Calendar.MONTH) + 1,
                        calRemind2.get(Calendar.DAY_OF_MONTH)
                ));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.task_add_btSave)).perform(click());

        onData(anything()).inAdapterView(withId(R.id.task_list_view)).atPosition(1)
                .onChildView(withText("My second task")).check(matches(isDisplayed()));
    }

    @Test
    public void listFinishedTasks() {
        TaskCategory cat = new TaskCategory("MyTaskCategory");
        tcDs.open();
        tcDs.insertOrUpdate(cat);
        tcDs.close();
        Task t1 = new Task("Task 1 not finished");
        t1.setProgress(30);
        t1.setCategory(cat);
        Task t2 = new Task("Task 2");
        t2.setProgress(0);
        t2.setCategory(cat);

        tDs.open();
        tDs.insertOrUpdate(t1);
        tDs.insertOrUpdate(t2);
        tDs.close();


        callOnResumeWorkaround();

        onData(anything()).inAdapterView(withId(R.id.category_list_view)).atPosition(0).perform(click());
        onView(withId(R.id.task_list_view)).check(matches(isDisplayed()));

        onData(anything()).inAdapterView(withId(R.id.task_list_view)).atPosition(0)
                .onChildView(withText(t1.getTitle())).check(matches(isDisplayed()));
        onData(anything()).inAdapterView(withId(R.id.task_list_view)).atPosition(1)
                .onChildView(withText(t2.getTitle())).check(matches(isDisplayed()));

        t2.setFinished(true);
        tDs.open();
        tDs.insertOrUpdate(t2);
        tDs.close();

        try {
            onView(withId(R.id.bt_search_task)).perform(click());
        } catch (NoMatchingViewException ex) {
            openContextualActionModeOverflowMenu();
            onView(withText(R.string.task_list_search)).perform(click());
        }
        onView(withId(android.support.design.R.id.search_src_text))
                .perform(typeText("t"), clearText(), closeSoftKeyboard());
        pressBack();

        onData(anything()).inAdapterView(withId(R.id.task_list_view)).atPosition(0)
                .onChildView(withText(t1.getTitle())).check(matches(isDisplayed()));
        onView(withId(R.id.task_list_view)).check(matches(TestHelper.hasListSize(1)));

        try {
            onView(withId(R.id.bt_finished)).perform(click());
        } catch (NoMatchingViewException ex) {
            openContextualActionModeOverflowMenu();
            onView(withText(R.string.task_list_finished)).perform(click());
        }

        onData(anything()).inAdapterView(withId(R.id.task_list_view)).atPosition(0)
                .onChildView(withText(t2.getTitle())).check(matches(isDisplayed()));
        onView(withId(R.id.task_list_view)).check(matches(TestHelper.hasListSize(1)));

        pressBack();
        pressBack();

        onData(anything()).inAdapterView(withId(R.id.task_list_view)).atPosition(0)
                .onChildView(withText(t1.getTitle())).check(matches(isDisplayed()));

        pressBack();

        onView(withId(R.id.category_list_view)).check(matches(isDisplayed()));

    }

    @Test
    public void testSortByDueDate() {
        TaskCategory taskCategory = new TaskCategory("testCat");

        Task task1 = new Task("task1");
        Calendar dueCal = Calendar.getInstance();
        dueCal.add(Calendar.DAY_OF_MONTH, 40);
        task1.setDueDate(dueCal.getTimeInMillis());
        task1.setCategory(taskCategory);
        Task task2 = new Task("task2");
        dueCal.add(Calendar.DAY_OF_MONTH, -16);
        task2.setDueDate(dueCal.getTimeInMillis());
        task2.setCategory(taskCategory);
        Task task3 = new Task("task3");
        dueCal.add(Calendar.DAY_OF_MONTH, 5);
        task3.setDueDate(dueCal.getTimeInMillis());
        task3.setCategory(taskCategory);

        tcDs.open();
        tcDs.insertOrUpdate(taskCategory);
        tcDs.close();

        tDs.open();
        tDs.insertOrUpdate(task1);
        tDs.insertOrUpdate(task2);
        tDs.insertOrUpdate(task3);
        tDs.close();

        callOnResumeWorkaround();

        onData(anything()).inAdapterView(withId(R.id.category_list_view)).atPosition(0).perform(click());

        onData(anything()).inAdapterView(withId(R.id.task_list_view)).atPosition(0).onChildView(withText("task2")).check(matches(isDisplayed()));
        onData(anything()).inAdapterView(withId(R.id.task_list_view)).atPosition(1).onChildView(withText("task3")).check(matches(isDisplayed()));
        onData(anything()).inAdapterView(withId(R.id.task_list_view)).atPosition(2).onChildView(withText("task1")).check(matches(isDisplayed()));

    }

    @Test
    public void testSortByProgress() {
        TaskCategory taskCategory = new TaskCategory("testCat");

        Task task1 = new Task("task1");
        Calendar dueCal = Calendar.getInstance();
        dueCal.add(Calendar.DAY_OF_MONTH, 40);
        task1.setDueDate(dueCal.getTimeInMillis());
        task1.setProgress(60);
        task1.setCategory(taskCategory);
        Task task2 = new Task("task2");
        dueCal.add(Calendar.DAY_OF_MONTH, -16);
        task2.setDueDate(dueCal.getTimeInMillis());
        task2.setProgress(20);
        task2.setCategory(taskCategory);
        Task task3 = new Task("task3");
        dueCal.add(Calendar.DAY_OF_MONTH, 5);
        task3.setDueDate(dueCal.getTimeInMillis());
        task3.setProgress(80);
        task3.setCategory(taskCategory);

        tcDs.open();
        tcDs.insertOrUpdate(taskCategory);
        tcDs.close();

        tDs.open();
        tDs.insertOrUpdate(task1);
        tDs.insertOrUpdate(task2);
        tDs.insertOrUpdate(task3);
        tDs.close();

        callOnResumeWorkaround();

        onData(anything()).inAdapterView(withId(R.id.category_list_view)).atPosition(0).perform(click());

        onView(withId(R.id.bt_sort)).perform(click());

        onData(anything()).inAdapterView(withId(R.id.task_list_view)).atPosition(0).onChildView(withText("task2")).check(matches(isDisplayed()));
        onData(anything()).inAdapterView(withId(R.id.task_list_view)).atPosition(1).onChildView(withText("task1")).check(matches(isDisplayed()));
        onData(anything()).inAdapterView(withId(R.id.task_list_view)).atPosition(2).onChildView(withText("task3")).check(matches(isDisplayed()));

        onView(withId(R.id.bt_sort)).perform(click());

        onData(anything()).inAdapterView(withId(R.id.task_list_view)).atPosition(0).onChildView(withText("task2")).check(matches(isDisplayed()));
        onData(anything()).inAdapterView(withId(R.id.task_list_view)).atPosition(1).onChildView(withText("task3")).check(matches(isDisplayed()));
        onData(anything()).inAdapterView(withId(R.id.task_list_view)).atPosition(2).onChildView(withText("task1")).check(matches(isDisplayed()));
    }
}