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
        Log.d("ToDoAdapterTest", "Setting up the test environment")

        // Initialisierung der Liste
        todos = mutableListOf(
            ToDo("1", "Test ToDo 1"),
            ToDo("2", "Test ToDo 2")
        )
        Log.d("ToDoAdapterTest", "ToDo list initialized with ${todos.size} items: $todos")

        // Initialisierung des Adapters
        toDoAdapter = ToDoAdapter(todos)
        Log.d("ToDoAdapterTest", "ToDoAdapter initialized")
    }

    @Ignore("Datenbank noch nicht verfügbar, Test wird vorübergehend deaktiviert")
    @Test
    fun testGetItemCount() {
        Log.d("ToDoAdapterTest", "Running testGetItemCount")

        // Überprüfen der Anzahl der Items
        val itemCount = toDoAdapter.itemCount
        Log.d("ToDoAdapterTest", "Item count retrieved: $itemCount")

        assertEquals(2, itemCount)
        Log.d("ToDoAdapterTest", "Item count is correct")
    }

    @Ignore("Datenbank noch nicht verfügbar, Test wird vorübergehend deaktiviert")
    @Test
    fun testAddToDo() {
        Log.d("ToDoAdapterTest", "Running testAddToDo")

        val newToDo = ToDo("3", "New ToDo")
        toDoAdapter.addToDo(newToDo)

        assertEquals(3, toDoAdapter.itemCount)
        assertEquals("New ToDo", todos.last().title)
        Log.d("ToDoAdapterTest", "ToDo added successfully, new item count: ${toDoAdapter.itemCount}")

    }

    @Ignore("Datenbank noch nicht verfügbar, Test wird vorübergehend deaktiviert")
    @Test
    fun testRemoveToDoAt() {
        Log.d("ToDoAdapterTest", "Running testRemoveToDoAt")

        toDoAdapter.removeToDoAt(0)

        assertEquals(1, toDoAdapter.itemCount)
        assertEquals("Test ToDo 2", todos.first().title)
        Log.d("ToDoAdapterTest", "ToDo removed successfully, new item count: ${toDoAdapter.itemCount}")

    }
}
