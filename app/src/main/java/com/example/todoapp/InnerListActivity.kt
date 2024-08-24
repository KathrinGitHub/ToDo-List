package com.example.todoapp
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class InnerListActivity : BasicActivity() {

    private lateinit var toDoAdapter: ToDoAdapter
    private val todos = mutableListOf<ToDo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inner_list)
        //println("Hello, World!")

        toDoAdapter = ToDoAdapter(todos)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.adapter = toDoAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            showAddToDoDialog()
        }
    }

    private fun showAddToDoDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add ToDo")

        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("Add") { dialog, _ ->
            val title = input.text.toString()
            if (title.isNotEmpty()) {
                val todo = ToDo(title)
                toDoAdapter.addToDo(todo)
                Log.d("MainActivity", "ToDo hinzugefügt: $title")
                Toast.makeText(this, "ToDo hinzugefügt: $title", Toast.LENGTH_SHORT).show()

            }
            dialog.dismiss()
        }
        builder.setNeutralButton("Cancel") { dialog, _ -> dialog.cancel()}

        builder.show()
    }
     fun addToDoItem(todo: ToDo) {
         todos.add(todo)
         toDoAdapter.notifyItemInserted(todos.size -1)
     }
}
