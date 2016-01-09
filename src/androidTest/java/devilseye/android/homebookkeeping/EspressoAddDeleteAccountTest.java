package devilseye.android.homebookkeeping;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static org.hamcrest.CoreMatchers.allOf;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class EspressoAddDeleteAccountTest {

    @Rule
    public final ActivityTestRule<MainActivity> account = new ActivityTestRule<MainActivity>(MainActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            Context targetContext = InstrumentationRegistry.getInstrumentation()
                    .getTargetContext();
            return new Intent(targetContext, MainActivity.class);
        }
    };

    @Test
    public void addDeleteAccountEspresso() {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText("Add account")).perform(click());
        onView(withText("Add new account")).check(matches(allOf(withText("Add new account"), isDisplayed())));
        onView(withId(R.id.name)).perform(typeText("[EspressoTest]"));
        onView(withId(R.id.description)).perform(typeText("[EspressoTest]"));
        onView(withId(R.id.balance)).perform(typeText("666.66]"));
        onView(withId(android.R.id.button1)).perform(click());
        onView(allOf(withId(R.id.deleteButton), hasSibling(withText("[EspressoTest]"))))
                .perform(click());
        onView(withText("Are you sure?")).check(matches(allOf(withText("Are you sure?"), isDisplayed())));
        onView(withId(R.id.delete)).perform(click());
    }
}
