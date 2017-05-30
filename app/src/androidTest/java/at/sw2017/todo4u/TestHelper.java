package at.sw2017.todo4u;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.ViewMatchers;
import android.view.View;
import android.widget.SeekBar;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import at.sw2017.todo4u.model.TaskCategory;

class TestHelper {
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
}
