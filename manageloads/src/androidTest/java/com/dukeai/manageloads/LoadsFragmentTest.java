package com.dukeai.manageloads;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.Espresso;
//import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import com.dukeai.manageloads.ui.fragments.LoadsFragment;

@RunWith(AndroidJUnit4.class)
public class LoadsFragmentTest {


//    @Rule
//    public ActivityScenarioRule<LoadsFragment> activityRule = new ActivityScenarioRule<>(LoadsFragment.class);

    @Test
    public void testFragmentLaunch() {
        // Launches the LoadsFragment
        FragmentScenario.launchInContainer(LoadsFragment.class, null);

        // Checks if the RecyclerView is displayed
        onView(withId(R.id.loads_recycler_view)).check(matches(isDisplayed()));
    }

    @Test
    public void testRecyclerViewInteraction() {
        // Assuming you have a RecyclerView with id loads_recycler_view and it's populated
        FragmentScenario.launchInContainer(LoadsFragment.class, null);

        // Scrolls to a specific position in the RecyclerView and clicks on an item
//        onView(withId(R.id.loads_recycler_view))
//                .perform(RecyclerViewActions.scrollToPosition(0))
//                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Add checks or assertions here depending on the expected outcome of the click action
    }

    @Test
    public void testAddRecipientButton() {
        // Launches the LoadsFragment
        FragmentScenario.launchInContainer(LoadsFragment.class, null);

        // Checks if the "Add Recipient" button is displayed and clicks it
        onView(withId(R.id.add_recipient_text)).check(matches(isDisplayed())).perform(click());

        // Depending on your implementation, you might navigate to a new Fragment or Dialog.
        // Add assertions here to verify the UI after clicking "Add Recipient".
    }

    // Add more tests as necessary to cover different functionalities of your LoadsFragment
}

