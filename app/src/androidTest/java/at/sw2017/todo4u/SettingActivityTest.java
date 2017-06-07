package at.sw2017.todo4u;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import at.sw2017.todo4u.database.SettingsDataSource;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class SettingActivityTest {

    @Rule
    public final ActivityTestRule<SettingActivity> mActivityRule = new ActivityTestRule<>(SettingActivity.class);

    private Context context;
    private SettingsDataSource sDs;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getTargetContext();
        sDs = new SettingsDataSource(context);

        TestHelper.clearDatabase();
    }

    @After
    public void tearDown() {
        TestHelper.clearDatabase();
    }

    @Test
    public void checkDefault() {
        onView(withText("None")).check(matches(isDisplayed()));

    }

    @Test
    public void selectColorful() {
        onView(withId(R.id.settings_spinnerCategoryColor)).perform(click());
        onView(withText("Colorful")).perform(click());
        onView(withText("Colorful")).check(matches(isDisplayed()));
    }

}
