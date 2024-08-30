package com.example.todoapp

import org.junit.Assert.assertEquals
import org.junit.Test

class ToDoTest {

    @Test
    fun testToDoCreation() {
        val todo = ToDo(
            item_ID = "1",
            title = "Test ToDo",
            description = "This is a test todo"
        )

        assertEquals("Test ToDo", todo.title)
        assertEquals("This is a test todo", todo.description)
    }
}
