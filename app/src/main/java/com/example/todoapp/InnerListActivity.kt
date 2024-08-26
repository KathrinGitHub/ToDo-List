package com.example.todoapp
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Calendar


class InnerListActivity : AppCompatActivity() {

    private lateinit var toDoAdapter: ToDoAdapter
    private var selectedToDoList: ToDoList? = null
    private val todos = mutableListOf<ToDo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inner_list)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        createNotificationChannel()

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
        addSampleToDo()
    }

    private fun addSampleToDo() {
        val sampleTodo = ToDo(
            title = "Sample ToDo",
            description = "This is a sample todo item.",
            dueDate = System.currentTimeMillis(), // Aktuelles Datum
            priority = 1, // Medium
            isDone = false
        )
        toDoAdapter.addToDo(sampleTodo)
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
        return if (name != null) {
            null
        } else {
            ToDoList(name ?: "Default List", mutableListOf())
        }
    }

    private fun showAddToDoDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add ToDo")

        val view = layoutInflater.inflate(R.layout.dialog_add_todo, null)
        builder.setView(view)

        val editTextTitle: EditText = view.findViewById(R.id.editTextTitle)
        val editTextDescription: EditText = view.findViewById(R.id.editTextDescription)
        val buttonSelectDate: Button = view.findViewById(R.id.buttonSelectDate)
        val spinnerPriority: Spinner = view.findViewById(R.id.spinnerPriority)

        var selectedDate: Long? = null

        buttonSelectDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                calendar.set(selectedYear, selectedMonth, selectedDayOfMonth)
                selectedDate = calendar.timeInMillis
                buttonSelectDate.text = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
            }, year, month, dayOfMonth)
            datePicker.show()
        }

        builder.setPositiveButton("Add") { dialog, _ ->
            val title = editTextTitle.text.toString()
            val description = editTextDescription.text.toString()
            val priority = spinnerPriority.selectedItemPosition

            if (title.isNotEmpty()) {
                val todo = ToDo(
                    title = title,
                    description = description,
                    dueDate = selectedDate,
                    priority = priority
                )
                selectedToDoList?.todos?.add(todo)
                toDoAdapter.notifyDataSetChanged()
                //toDoAdapter.addToDo(todo)

                Log.d("MainActivity", "ToDo hinzugefügt:")
                Log.d("MainActivity", "Title: ${todo.title}")
                Log.d("MainActivity", "Description: ${todo.description}")
                Log.d("MainActivity", "Due Date: ${todo.dueDate}")
                Log.d("MainActivity", "Priority: ${todo.priority}")

                if (selectedDate != null) {
                //TODO
                // scheduleReminder(todo)
                }
            }
            dialog.dismiss()
        }
        builder.setNeutralButton("Cancel") { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    private fun createNotificationChannel() {
        val name = "ToDo Reminders"
        val descriptionText = "Channel for ToDo Reminders"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel("todo_channel", name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }



}