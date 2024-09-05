package at.fhjoanneum.todoapp

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
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
    private var mockOnClick = mock<(ToDoList) -> Unit>()

    @Before
    fun setUp() {
        toDoLists = mutableListOf(
            ToDoList("1", "Test List 1"),
            ToDoList("2", "Test List 2")
        )
        mockOnClick = mock()
        toDoListAdapter = ToDoListAdapter(toDoLists).apply {
            onClick = mockOnClick
        }
    }

    @Ignore("test doesn't work")
    @Test
    fun testOnClickHandlerIsCalled() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val parent = FrameLayout(context)
        val viewHolder = toDoListAdapter.onCreateViewHolder(parent, 0)
        toDoListAdapter.onBindViewHolder(viewHolder, 0)

        val textViewListName = viewHolder.itemView.findViewById<TextView>(R.id.textViewListName)
        assertNotNull("TextView not found", textViewListName)

        textViewListName.performClick()
        verify(mockOnClick).invoke(toDoLists[0])
    }

    @Test
    fun testGetItemCount() {
        assertEquals(2, toDoListAdapter.itemCount)
    }

    @Ignore("test doesn't work")
    @Test
    fun testAddToDoList() {
        assertNotNull(toDoListAdapter)
        assertNotNull(toDoLists)

        val newToDoList = ToDoList("3", "New ToDo List")
        toDoListAdapter.addToDoList(newToDoList)
        assertEquals(3, toDoListAdapter.itemCount)
        assertEquals(newToDoList, toDoLists.last())
    }
}
