package com.example.todoapp

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import org.junit.Ignore
/*
@RunWith(AndroidJUnit4::class)
class InnerListActivityTest {

    @Rule @JvmField
    var activityScenarioRule = ActivityScenarioRule(InnerListActivity::class.java)

    @Ignore("Datenbank noch nicht verfügbar, Test wird vorübergehend deaktiviert")
    @Test
    fun testListTitleIsDisplayed() {
        onView(withId(R.id.items_title)).check(matches(isDisplayed()))
    }

    @Ignore("Datenbank noch nicht verfügbar, Test wird vorübergehend deaktiviert")
    @Test
    fun testRecyclerViewIsDisplayed() {
        onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
    }
}
*/