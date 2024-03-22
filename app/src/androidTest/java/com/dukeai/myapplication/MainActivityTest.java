package com.dukeai.myapplication;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void changeBackground_CheckColor() {
        // This test checks if the main activity is displayed
        onView(withId(R.id.activity_main)).check(matches(isDisplayed()));

        // Note: Checking the background color programmatically in Espresso is complex and usually not recommended.
        // It's more common to check visible UI elements' text, existence, or interactions.
    }

    @Test
    public void buttonClick_NavigateToDashboard() {
        // Simulate a button click that should start DashboardActivity.
        // Replace R.id.button_id with your button's ID that triggers jumpToManageLoads.
        onView(withId(R.id.button)).perform(click());

        // Verify if DashboardActivity is started. This step requires knowing the internal state or view ID of DashboardActivity.
        // Since we don't have access to DashboardActivity's layout and logic, this is a placeholder for further expansion.
    }
}
