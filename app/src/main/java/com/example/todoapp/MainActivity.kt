package com.example.todoapp

import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var toDoAdapter: ToDoAdapter
    private val todos = mutableListOf<ToDo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
            }
            dialog.dismiss()
        }
        builder.setNeutralButton("Cancel") { dialog, _ -> dialog.cancel()}

        builder.show()
    }
     private fun addToDoItem(todo: ToDo) {
         todos.add(todo)
         toDoAdapter.notifyItemInserted(todos.size -1)
     }
}
