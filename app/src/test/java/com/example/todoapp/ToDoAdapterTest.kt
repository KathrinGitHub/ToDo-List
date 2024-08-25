package com.example.todoapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.common.base.Verify.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.Mockito.times
import org.mockito.Mockito.anyInt

class ToDoAdapterTest {

    private lateinit var adapter: ToDoAdapter
    private val todos = mutableListOf<ToDo>()

    @Before
    fun setUp() {
        adapter = ToDoAdapter(todos)
    }
/*
    @Test
    fun testAddToDo() {
        // Given
        val newToDo = ToDo("New ToDo Item")

        // When
        adapter.addToDo(newToDo)

        // Then
        assertEquals(1, adapter.itemCount)
        assertEquals("New ToDo Item", todos[0].title)
    }

 */
/*
    @Test
    fun testOnBindViewHolder() {
        val viewHolder = mock(ToDoAdapter.ToDoViewHolder::class.java)
        val todo = ToDo("Test ToDo")
        todo.isDone = true
        todos.add(todo)
        adapter.notifyDataSetChanged()

        // Wenn ein ViewHolder gemockt wird, stelle sicher, dass du die tatsächlichen Methoden des ViewHolder mockst
        `when`(viewHolder.titleTextView.text).thenReturn("Test ToDo")
        `when`(viewHolder.checkBox.isChecked).thenReturn(true)

        adapter.onBindViewHolder(viewHolder, 0)

        // Überprüfe, ob die Methoden auf dem Mock-Objekt aufgerufen wurden
        verify(viewHolder.titleTextView).text = "Test ToDo"
        verify(viewHolder.checkBox).isChecked = true
    }

 */
}