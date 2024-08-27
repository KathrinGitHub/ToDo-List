package com.example.todoapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.UUID

class ToDoListOverviewActivity : BasicActivity() {

    private lateinit var toDoListAdapter: ToDoListAdapter
    private var toDoLists = mutableListOf<ToDoList>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todolist_overview)

        // TODO: comment in as soon as dummy data is in the database
        toDoLists = CloudFirestore().getUserLists(this)

        toDoListAdapter = ToDoListAdapter(toDoLists) { selectedList ->
            val intent = Intent(this, InnerListActivity::class.java).apply {
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
                val loggedInUserId = getSharedPreferences(Constants.TODOAPP_PREFERENCES, MODE_PRIVATE)
                    .getString(Constants.UID_OF_LOGGED_USER, "")
                val toDoList = ToDoList(
                    UUID.randomUUID().toString(),
                    listName,
                    mutableListOf(),
                    loggedInUserId.toString()
                )
                CloudFirestore().saveListOnCloudFirestore(this, toDoList)
                toDoListAdapter.addToDoList(toDoList)
            }
            dialog.dismiss()
        }
        builder.setNeutralButton("Cancel") { dialog, _ -> dialog.cancel() }

        builder.show()
    }
}
