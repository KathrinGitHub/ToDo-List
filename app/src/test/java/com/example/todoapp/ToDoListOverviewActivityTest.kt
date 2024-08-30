package com.example.todoapp

import android.util.Log
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
class ToDoListOverviewActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(ToDoListOverviewActivity::class.java)

    @Ignore("Datenbank noch nicht verfügbar, Test wird vorübergehend deaktiviert")
    @Test
    fun testRecyclerViewIsDisplayed() {
        Log.d("ToDoListOverviewActivityTest", "Running testRecyclerViewIsDisplayed")

        onView(withId(R.id.recyclerViewToDoLists)).check(matches(isDisplayed()))
        Log.d("ToDoListOverviewActivityTest", "RecyclerView is displayed")

    }

    @Ignore("Datenbank noch nicht verfügbar, Test wird vorübergehend deaktiviert")
    @Test
    fun testFabButtonIsDisplayed() {
        Log.d("ToDoListOverviewActivityTest", "Running testFabButtonIsDisplayed")

        onView(withId(R.id.fabAddList)).check(matches(isDisplayed()))
        Log.d("ToDoListOverviewActivityTest", "FAB button is displayed")

    }
}
*/