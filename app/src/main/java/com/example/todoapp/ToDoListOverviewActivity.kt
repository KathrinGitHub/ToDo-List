package com.example.todoapp

import ToDoListAdapter
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ToDoListOverviewActivity : AppCompatActivity() {

    private lateinit var toDoListAdapter: ToDoListAdapter
    private val toDoLists = mutableListOf<ToDoList>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todolist_overview)

        toDoListAdapter = ToDoListAdapter(toDoLists) { selectedList ->
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("list_name", selectedList.name)
            }
            startActivity(intent)
        }

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewToDoLists)
        recyclerView.adapter = toDoListAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val fab: FloatingActionButton = findViewById(R.id.fabAddList)
        fab.setOnClickListener {
            showAddToDoListDialog()
        }
    }

    private fun showAddToDoListDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add ToDo List")

        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("Add") { dialog, _ ->
            val listName = input.text.toString()
            if (listName.isNotEmpty()) {
                val toDoList = ToDoList(listName, mutableListOf())
                toDoLists.add(toDoList)
                toDoListAdapter.notifyItemInserted(toDoLists.size - 1)
            }
            dialog.dismiss()
        }
        builder.setNeutralButton("Cancel") { dialog, _ -> dialog.cancel() }

        builder.show()
    }
}
