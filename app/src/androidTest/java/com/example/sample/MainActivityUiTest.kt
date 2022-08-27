package com.example.sample

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.sample.ui.main.MainActivity

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Rule

/**
 * Super basic espresso demonstration
 */
@RunWith(AndroidJUnit4::class)
class MainActivityUiTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test fun testViewShowsLoading() {
        onView(withId(R.id.list_progress_bar)).check(matches(isDisplayed()))
    }
}