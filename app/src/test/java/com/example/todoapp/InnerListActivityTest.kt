package com.example.todoapp

import android.app.AlertDialog
import android.os.Build
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowToast
import org.robolectric.shadows.ShadowDialog

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class InnerListActivityTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(InnerListActivity::class.java)

    @Test
    fun testShowAddToDoDialog() {
        val scenario = activityRule.scenario
        scenario.onActivity { activity ->
            val fab: FloatingActionButton = activity.findViewById(R.id.fab)
            fab.performClick()

            // Access the latest dialog
            val dialog = ShadowDialog.getLatestDialog()
            assert(dialog is AlertDialog)

            // Check if the EditText is present in the dialog
            val input = dialog?.findViewById<EditText>(android.R.id.edit)
            assert(input != null)
            input?.setText("Test ToDo")

            // Find the positive button and perform a click
            val addButton = dialog?.findViewById<android.widget.Button>(android.R.id.button1)
            addButton?.performClick()

            // Verify the toast message
            assertEquals("ToDo hinzugefügt: Test ToDo", ShadowToast.getTextOfLatestToast())

            // Check if the ToDo item was added to the RecyclerView
            val recyclerView = activity.findViewById<RecyclerView>(R.id.recyclerView)
            val adapter = recyclerView.adapter as ToDoAdapter
            assertEquals(1, adapter.itemCount)
            assertEquals("Test ToDo", adapter.todos[0].title)
        }
    }


    @Test
    fun testAddToDoItem() {
        val scenario = activityRule.scenario
        scenario.onActivity { activity ->
            val toDoItem = ToDo("Test ToDo Item")
            activity.addToDoItem(toDoItem)

            val recyclerView = activity.findViewById<RecyclerView>(R.id.recyclerView)
            val adapter = recyclerView.adapter as ToDoAdapter
            assertEquals(1, adapter.itemCount)
            assertEquals("Test ToDo Item", adapter.todos[0].title)
        }
    }
}