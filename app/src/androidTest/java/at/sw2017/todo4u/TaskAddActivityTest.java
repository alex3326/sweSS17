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

public class TaskAddActivityTest {

    @Rule
    public final ActivityTestRule<TaskAddActivity> mActivityRule = new ActivityTestRule<>(TaskAddActivity.class);
    private Context context;
    private TaskCategoriesDataSource tcDs;
    private TasksDataSource tDs;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getTargetContext();
        tcDs = new TaskCategoriesDataSource(context);
        tDs = new TasksDataSource(context);

        TestHelper.clearDatabase();

        tcDs.open();
        TaskCategory tc1 = new TaskCategory("Private");
        TaskCategory tc2 = new TaskCategory("University");
        tcDs.insertOrUpdate(tc1);
        tcDs.insertOrUpdate(tc2);
        tcDs.close();

    }

    @After
    public void tearDown() {
        TestHelper.clearDatabase();
    }

    @Test
    public void addSimpleTask() {
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
    }


}