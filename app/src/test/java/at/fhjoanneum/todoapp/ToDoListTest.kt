package at.fhjoanneum.todoapp

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ToDoListTest {

    @Before
    fun setUp() {
        ToDoList.getLists().clear()
    }

    @Test
    fun testAddList() {
        val toDoList = ToDoList("1", "Test List")
        ToDoList.addList(toDoList)

        assertEquals(1, ToDoList.getLists().size)
        assertEquals("Test List", ToDoList.getLists()[0].name)
    }

    @Test
    fun testAddLists() {
        val lists = mutableListOf(
            ToDoList("1", "List 1"),
            ToDoList("2", "List 2")
        )
        ToDoList.addLists(lists)

        assertEquals(2, ToDoList.getLists().size)
    }

    @Test
    fun testFindByName() {
        val toDoList = ToDoList("1", "Test List")
        ToDoList.addList(toDoList)

        val foundList = ToDoList.findByName("Test List")
        assertNotNull(foundList)
        assertEquals("Test List", foundList?.name)
    }
}
