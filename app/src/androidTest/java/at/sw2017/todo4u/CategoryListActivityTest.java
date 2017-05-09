package at.sw2017.todo4u;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import at.sw2017.todo4u.database.TaskCategoriesDataSource;
import at.sw2017.todo4u.model.TaskCategory;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class CategoryListActivityTest {
    @Rule
    public final ActivityTestRule<CategoryListActivity> mActivityRule = new ActivityTestRule<>(CategoryListActivity.class);
    private TaskCategoriesDataSource tcDs;

    @Before
    public void setUp() {
        Context context = InstrumentationRegistry.getTargetContext();
        tcDs = new TaskCategoriesDataSource(context);

        removeAllCategories();
    }

    @After
    public void tearDown() {
        removeAllCategories();
    }

    private void removeAllCategories() {
        tcDs.open();
        List<TaskCategory> categories = tcDs.getAll();
        for (TaskCategory taskCategory : categories) {
            tcDs.delete(taskCategory);
        }
        tcDs.close();
    }

    private void callOnResumeWorkaround() {
        onView(withId(R.id.bt_add_category)).perform(click());
        onView(withId(R.id.category_add_btCancel)).perform(click());
    }


    @Test
    public void testOnCreate() {
        TaskCategory testCategory1 = new TaskCategory("test");
        TaskCategory testCategory2 = new TaskCategory("awesome test");

        tcDs.open();
        tcDs.insertOrUpdate(testCategory1);
        tcDs.insertOrUpdate(testCategory2);
        tcDs.close();

        callOnResumeWorkaround();

        onData(anything()).inAdapterView(withId(R.id.category_list_view)).atPosition(0).onChildView(withText("test")).check(matches(isDisplayed()));
        onData(anything()).inAdapterView(withId(R.id.category_list_view)).atPosition(1).onChildView(withText("awesome test")).check(matches(isDisplayed()));
    }

    @Test
    public void testInsertTaskCategory() {
        onView(withId(R.id.bt_add_category)).perform(click());
        onView(withId(R.id.category_pop_up_view)).check(matches(isDisplayed()));
        onView(withId(R.id.tx_new_category)).perform(typeText("test"), closeSoftKeyboard());
        onView(withText("test")).check(matches(isDisplayed()));
        onView(withId(R.id.category_add_btSave)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.category_list_view)).atPosition(0).onChildView(withText("test")).check(matches(isDisplayed()));
    }

    @Test
    public void testInsertTaskCategoryAlreadyExist() {
        onView(withId(R.id.bt_add_category)).perform(click());
        onView(withId(R.id.tx_new_category)).perform(typeText("test"), closeSoftKeyboard());
        onView(withId(R.id.category_add_btSave)).perform(click());
        onView(withId(R.id.bt_add_category)).perform(click());
        onView(withId(R.id.tx_new_category)).perform(typeText("test"), closeSoftKeyboard());
        onView(withId(R.id.category_add_btSave)).perform(click());
        onView(withId(R.id.bt_add_category)).perform(click());
        onView(withId(R.id.tx_new_category)).perform(typeText("test2"), closeSoftKeyboard());
        onView(withId(R.id.category_add_btSave)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.category_list_view)).atPosition(0).onChildView(withText("test")).check(matches(isDisplayed()));
        onData(anything()).inAdapterView(withId(R.id.category_list_view)).atPosition(1).onChildView(withText("test2")).check(matches(isDisplayed()));
    }

    @Test
    public void testSearch() {
        TaskCategory testCategory1 = new TaskCategory("test");
        TaskCategory testCategory2 = new TaskCategory("tes");
        TaskCategory testCategory3 = new TaskCategory("te");
        TaskCategory testCategory4 = new TaskCategory("t");

        tcDs.open();
        tcDs.insertOrUpdate(testCategory1);
        tcDs.insertOrUpdate(testCategory2);
        tcDs.insertOrUpdate(testCategory3);
        tcDs.insertOrUpdate(testCategory4);
        tcDs.close();


        onView(withId(R.id.bt_search_category)).perform(click());
        onView(withId(android.support.design.R.id.search_src_text))
                .perform(typeText("t"), closeSoftKeyboard());
        onData(anything()).inAdapterView(withId(R.id.category_list_view)).atPosition(0).onChildView(withText("test")).check(matches(isDisplayed()));
        onData(anything()).inAdapterView(withId(R.id.category_list_view)).atPosition(1).onChildView(withText("tes")).check(matches(isDisplayed()));
        onData(anything()).inAdapterView(withId(R.id.category_list_view)).atPosition(2).onChildView(withText("te")).check(matches(isDisplayed()));
        onData(anything()).inAdapterView(withId(R.id.category_list_view)).atPosition(3).onChildView(withText("t")).check(matches(isDisplayed()));

        onView(withId(android.support.design.R.id.search_src_text))
                .perform(typeText("e"), closeSoftKeyboard());
        onData(anything()).inAdapterView(withId(R.id.category_list_view)).atPosition(0).onChildView(withText("test")).check(matches(isDisplayed()));
        onData(anything()).inAdapterView(withId(R.id.category_list_view)).atPosition(1).onChildView(withText("tes")).check(matches(isDisplayed()));
        onData(anything()).inAdapterView(withId(R.id.category_list_view)).atPosition(2).onChildView(withText("te")).check(matches(isDisplayed()));

        onView(withId(android.support.design.R.id.search_src_text))
                .perform(typeText("s\n"), closeSoftKeyboard());
        onData(anything()).inAdapterView(withId(R.id.category_list_view)).atPosition(0).onChildView(withText("test")).check(matches(isDisplayed()));
        onData(anything()).inAdapterView(withId(R.id.category_list_view)).atPosition(1).onChildView(withText("tes")).check(matches(isDisplayed()));

        onView(withId(android.support.design.R.id.search_src_text))
                .perform(typeText("t"), closeSoftKeyboard());
        onData(anything()).inAdapterView(withId(R.id.category_list_view)).atPosition(0).onChildView(withText("test")).check(matches(isDisplayed()));
    }

    @Test
    public void addEmpty() {
        onView(withId(R.id.bt_add_category)).perform(click());
        onView(withId(R.id.category_add_btSave)).perform(click());
        onView(withText(R.string.category_add_error_name_empty))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

}