package com.example.todoapp
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var toDoAdapter: ToDoAdapter
    private var selectedToDoList: ToDoList? = null
    private val todos = mutableListOf<ToDo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //println("Hello, World!")

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val listName = intent.getStringExtra("list_name")
        selectedToDoList = getToDoListByName(listName)

        toDoAdapter = ToDoAdapter(selectedToDoList?.todos ?: mutableListOf())
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.adapter = toDoAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            showAddToDoDialog()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, ToDoListOverviewActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    private fun getToDoListByName(name: String?): ToDoList? {

        return null // Platzhalter TODO
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
                //Log.d("MainActivity", "ToDo hinzugefügt: $title")
                Toast.makeText(this, "ToDo hinzugefügt: $title", Toast.LENGTH_SHORT).show()

            }
            dialog.dismiss()
        }
        builder.setNeutralButton("Cancel") { dialog, _ -> dialog.cancel()}

        builder.show()
    }
}