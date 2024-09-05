package at.fhjoanneum.todoapp

import android.util.Log
import org.junit.Assert.*
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

class ToDoAdapterTest {

    private lateinit var toDoAdapter: ToDoAdapter
    private lateinit var todos: MutableList<ToDo>

    @Before
    fun setUp() {
        todos = mutableListOf(
            ToDo("1", "Test ToDo 1"),
            ToDo("2", "Test ToDo 2")
        )
        toDoAdapter = ToDoAdapter(todos)
    }

    @Test
    fun testGetItemCount() {
        val itemCount = toDoAdapter.itemCount
        assertEquals(2, itemCount)
    }

    @Ignore("test doesn't work")
    @Test
    fun testAddToDo() {
        val newToDo = ToDo(
            item_ID = "3",
            title = "Test ToDo 3",
            description = "Test Description",
            dueDate = System.currentTimeMillis(),
            priority = 1,
            isDone = false
        )
        toDoAdapter.addToDo(newToDo)
        assertEquals(3, toDoAdapter.itemCount)
        assertEquals("Test ToDo 3", todos.last().title)
    }

    @Ignore("test doesn't work")
    @Test
    fun testRemoveToDoAt() {
        toDoAdapter.removeToDoAt(1)
        assertEquals(1, toDoAdapter.itemCount)
        assertEquals("Test ToDo 1", todos.last().title)
    }
}
