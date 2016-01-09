package devilseye.android.homebookkeeping;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class EspressoAddDeleteOperationTest {

    @Rule
    public final ActivityTestRule<OperationActivity> operation = new ActivityTestRule<OperationActivity>(OperationActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            Context targetContext = InstrumentationRegistry.getInstrumentation()
                    .getTargetContext();
            Intent result = new Intent(targetContext, OperationActivity.class);
            result.putExtra("acc_id", (long)2);
            return result;
        }
    };

    @Test
    public void addDeleteOperationEspresso() {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext());
        onView(withText("Add operation")).perform(click());
        onView(withText("Add operation")).check(matches(allOf(withText("Add operation"), isDisplayed())));
        onView(withId(R.id.op_value)).perform(typeText("666.66"));
        onView(withId(R.id.op_desc)).perform(typeText("[EspressoTest]"));
        onView(withId(R.id.toLabel)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
        onView(allOf(withId(R.id.deleteButton), hasSibling(withText("[EspressoTest]"))))
                .perform(click());
        onView(withText("Are you sure?")).check(matches(allOf(withText("Are you sure?"), isDisplayed())));
        onView(withId(android.R.id.button1)).perform(click());
    }
}
