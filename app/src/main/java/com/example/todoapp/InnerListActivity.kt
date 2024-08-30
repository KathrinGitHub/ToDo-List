package com.example.todoapp
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Calendar
import java.util.UUID
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper


class InnerListActivity : AppCompatActivity() {

    private lateinit var toDoAdapter: ToDoAdapter
    private var selectedToDoList: ToDoList? = null
    private lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inner_list)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        createNotificationChannel()

        val listName = intent.getStringExtra("list_name")
        val titleTextView: TextView = findViewById(R.id.items_title)
        titleTextView.text = listName


        selectedToDoList = getToDoListByName(listName)

        recyclerView = findViewById(R.id.recyclerView)
        toDoAdapter = ToDoAdapter(selectedToDoList?.todos ?: mutableListOf())
        recyclerView.adapter = toDoAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            showAddToDoDialog()
        }

        if (toDoAdapter.itemCount == 0) {
            addSampleToDo()
        }
        setupSwipeToDelete(this)
        setupActionBar()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_inner_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_share -> {
                shareList()
                true
            }
            R.id.action_delete -> {
                deleteList()
                true
            }
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun shareList() {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Sharing the list: ${selectedToDoList?.name}")
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

    private fun deleteList() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete List")
        builder.setMessage("Are you sure you want to delete this list?")
        builder.setPositiveButton("Yes") { _, _ ->
            if (selectedToDoList != null) {
                CloudFirestore().deleteListFromCloudFirestore(this, selectedToDoList!!)
                finish()
            } else {
                Toast.makeText(this, "No list selected", Toast.LENGTH_SHORT).show()
            }

                // Zurück zur Übersicht Activity navigieren
                val intent = Intent(this, ToDoListOverviewActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun addSampleToDo() {
        val sampleTodo = ToDo(
            item_ID = "1A",
            title = "Sample ToDo",
            description = "<- Swipe to delete",
            dueDate = System.currentTimeMillis(),
            priority = 1, // Medium
            isDone = false
        )
        toDoAdapter.addToDo(sampleTodo)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, ToDoListOverviewActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    private fun getToDoListByName(name: String?): ToDoList? {
        return if (name != null) {
            ToDoList.findByName(name) ?: ToDoList(
                list_ID = UUID.randomUUID().toString(),
                name = name,
                todos = mutableListOf<ToDo>(),
                creator_ID = "default_creator"
            )
        } else {
            ToDoList(
                list_ID = UUID.randomUUID().toString(),
                name = "Unnamed List",
                todos = mutableListOf<ToDo>(),
                creator_ID = "default_creator"
            )
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
        val checkBoxReminder: CheckBox = view.findViewById(R.id.checkBoxReminder)
        val spinnerReminderTime: Spinner = view.findViewById(R.id.spinnerReminderTime)


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

        checkBoxReminder.setOnCheckedChangeListener { _, isChecked ->
            spinnerReminderTime.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

        builder.setPositiveButton("Add") { dialog, _ ->
            val title = editTextTitle.text.toString()
            val description = editTextDescription.text.toString()
            val priority = spinnerPriority.selectedItemPosition

            if (title.isNotEmpty()) {
                val todo = ToDo(
                    item_ID = UUID.randomUUID().toString(),
                    title = title,
                    description = description,
                    dueDate = selectedDate,
                    priority = priority
                )
                toDoAdapter.addToDo(todo)
                CloudFirestore().updateToDoItems(this, selectedToDoList!!, true)

                if (checkBoxReminder.isChecked) {
                    val reminderTime = spinnerReminderTime.selectedItemPosition
                    scheduleReminder(todo, reminderTime)
                }
            }
            dialog.dismiss()
        }
        builder.setNeutralButton("Cancel") { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    private fun scheduleReminder(todo: ToDo, reminderTime: Int) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val reminderIntent = Intent(this, ReminderReceiver::class.java).apply {
            putExtra("title", todo.title)
            putExtra("todo_id", todo.item_ID)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            todo.item_ID.hashCode(),
            reminderIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )

        val triggerTime = todo.dueDate
            ?: System.currentTimeMillis() // Fallback auf jetzt, falls kein Datum angegeben ist

        val reminderMillis = when (reminderTime) {
            0 -> 15 * 60 * 1000 // 15 Minuten
            1 -> 30 * 60 * 1000 // 30 Minuten
            2 -> 60 * 60 * 1000 // 1 Stunde
            3 -> 2 * 60 * 60 * 1000 // 2 Stunden
            4 -> 24 * 60 * 60 * 1000 // 1 Tag
            else -> 0 // Keine Erinnerung
        }

        val reminderTriggerTime = triggerTime - reminderMillis
        if (reminderTriggerTime > System.currentTimeMillis()) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (alarmManager.canScheduleExactAlarms()) {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
                    } else {
                        Toast.makeText(
                            this,
                            "Permission required to schedule exact alarms. Please enable it in settings.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
                }
            } catch (e: SecurityException) {
                Toast.makeText(this, "Failed to schedule alarm: ${e.message}", Toast.LENGTH_LONG)
                    .show()
            }
        }
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

    private fun setupActionBar() {
        val toolbarRegistration: Toolbar = findViewById(R.id.toolbar_items_activity)
        setSupportActionBar(toolbarRegistration)

        val actionBar = supportActionBar
        if(actionBar != null) {
            // actionbar element is clickable + add following icon "<" (default: left)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        }

        toolbarRegistration.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupSwipeToDelete(context: Context) {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                (recyclerView.adapter as ToDoAdapter).removeToDoAt(position)
                CloudFirestore().updateToDoItems(context, selectedToDoList!!, false)
            }
        }
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}
