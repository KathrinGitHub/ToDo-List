package com.example.todoapp

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.*
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
class ToDoListAdapterTest {

    private lateinit var toDoListAdapter: ToDoListAdapter
    private lateinit var toDoLists: MutableList<ToDoList>
    private lateinit var mockOnClick: (ToDoList) -> Unit

    @Before
    fun setUp() {
        Log.d("ToDoListAdapterTest", "Setting up the test environment")

        toDoLists = mutableListOf(
            ToDoList("1", "Test List 1"),
            ToDoList("2", "Test List 2")
        )
        mockOnClick = mock()
        toDoListAdapter = ToDoListAdapter(toDoLists).apply {
            onClick = mockOnClick
        }
        Log.d("ToDoListAdapterTest", "toDoListAdapter initialized with ${toDoLists.size} items")

    }

    @Ignore("Datenbank noch nicht verfügbar, Test wird vorübergehend deaktiviert")
    @Test
    fun testOnClickHandlerIsCalled() {
        Log.d("ToDoListAdapterTest", "Running testOnClickHandlerIsCalled")

        val context = ApplicationProvider.getApplicationContext<Context>()
        val parent = LayoutInflater.from(context)
            .inflate(R.layout.item_todolist, null) as ViewGroup
        val viewHolder = toDoListAdapter.onCreateViewHolder(parent, 0)
        Log.d("ToDoListAdapterTest", "ViewHolder created")

        toDoListAdapter.onBindViewHolder(viewHolder, 0)
        Log.d("ToDoListAdapterTest", "ViewHolder bound to item at position 0")

        viewHolder.itemView.findViewById<TextView>(R.id.textViewListName).performClick()
        verify(mockOnClick).invoke(toDoLists[0])
        Log.d("ToDoListAdapterTest", "onClick handler verified")

    }

    @Ignore("Datenbank noch nicht verfügbar, Test wird vorübergehend deaktiviert")
    @Test
    fun testGetItemCount() {
        assertEquals(2, toDoListAdapter.itemCount)
    }

    @Ignore("Datenbank noch nicht verfügbar, Test wird vorübergehend deaktiviert")
    @Test
    fun testAddToDoList() {
        Log.d("ToDoListAdapterTest", "Running testAddToDoList")

        assertNotNull(toDoListAdapter)
        assertNotNull(toDoLists)
        val newToDoList = ToDoList("3", "New ToDo List")
        toDoListAdapter.addToDoList(newToDoList)
        assertEquals(3, toDoListAdapter.itemCount)
        assertEquals(newToDoList, toDoLists.last())
        Log.d("ToDoListAdapterTest", "Item added successfully, new item count: ${toDoListAdapter.itemCount}")

    }
}
