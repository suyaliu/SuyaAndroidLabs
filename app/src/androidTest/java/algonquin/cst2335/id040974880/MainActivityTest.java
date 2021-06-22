package algonquin.cst2335.id040974880;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    /**
     * Write a test case to test password only have numbers
     */
    @Test
    public void mainActivityTest() {
        //find the view with id R.id.editText;
        ViewInteraction appCompatEditText = onView(withId(R.id.editText));
        //perform typing "12345" into that view, then close the keyboard.
        appCompatEditText.perform(replaceText("12345"),closeSoftKeyboard());
        //find the button
        ViewInteraction materialButton = onView(withId(R.id.button));
        //perform clicking that button
        materialButton.perform(click());
        //find the texView
        ViewInteraction textView = onView(withId(R.id.textView));
        //check that its text matches "You shall not pass!"
        textView.check(matches(withText("You shall not pass")));//assertion(pass/fail)

    }

    /**
     * Write a test case to find missing uppperCase
     */
    @Test
    public void testFindMissingUpperCase(){
        //finds the EditText
        ViewInteraction appCompatEditText = onView(withId(R.id.editText));
        //perform: type in "password123#$*"
        appCompatEditText.perform(replaceText("password123#$*"),closeSoftKeyboard());
        //find the button
        ViewInteraction materialButton = onView(withId(R.id.button));
        //perform: click the button
        materialButton.perform(click());
        //find the TextView
        ViewInteraction textView = onView(withId(R.id.textView));
        //check that its text matches "You shall not pass!"
        textView.check(matches(withText("You shall not pass")));
    }
    /**
     * Write a test case to find missing LowerCase
     */
    @Test
    public void testFindMissingLowerCase(){
        //finds the EditText
        ViewInteraction appCompatEditText = onView(withId(R.id.editText));
        //perform: type in "PW123#$*"
        appCompatEditText.perform(replaceText("PW123#$*"),closeSoftKeyboard());
        //find the button
        ViewInteraction materialButton = onView(withId(R.id.button));
        //perform: click the button
        materialButton.perform(click());
        //find the TextView
        ViewInteraction textView = onView(withId(R.id.textView));
        //check that its text matches "You shall not pass!"
        textView.check(matches(withText("You shall not pass")));
    }
    /**
     * Write a test case to find missing digitCase
     */
    @Test
    public void testFindMissingDigitCase(){
        //finds the EditText
        ViewInteraction appCompatEditText = onView(withId(R.id.editText));
        //perform: type in "PWord#$*"
        appCompatEditText.perform(replaceText("PWord#$*"),closeSoftKeyboard());
        //find the button
        ViewInteraction materialButton = onView(withId(R.id.button));
        //perform: click the button
        materialButton.perform(click());
        //find the TextView
        ViewInteraction textView = onView(withId(R.id.textView));
        //check that its text matches "You shall not pass!"
        textView.check(matches(withText("You shall not pass")));
    }
    /**
     * Write a test case to find missing SpecialCase
     */
    @Test
    public void testFindMissingSpecialCase(){
        //finds the EditText
        ViewInteraction appCompatEditText = onView(withId(R.id.editText));
        //perform: type in "PWord123"
        appCompatEditText.perform(replaceText("PWord123"),closeSoftKeyboard());
        //find the button
        ViewInteraction materialButton = onView(withId(R.id.button));
        //perform: click the button
        materialButton.perform(click());
        //find the TextView
        ViewInteraction textView = onView(withId(R.id.textView));
        //check that its text matches "You shall not pass!"
        textView.check(matches(withText("You shall not pass")));
    }

    /**
     * Write a test case to show Your password is complex enough
     */
    @Test
    public void testFindMeetCase(){
        //finds the EditText
        ViewInteraction appCompatEditText = onView(withId(R.id.editText));
        //perform: type in "PWord123#$*"
        appCompatEditText.perform(replaceText("PWord123#$*"),closeSoftKeyboard());
        //find the button
        ViewInteraction materialButton = onView(withId(R.id.button));
        //perform: click the button
        materialButton.perform(click());
        //find the TextView
        ViewInteraction textView = onView(withId(R.id.textView));
        //check that its text matches "Your password is complex enough!"
        textView.check(matches(withText("Your password is complex enough")));
    }


    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
