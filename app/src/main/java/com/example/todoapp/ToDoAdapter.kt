package com.example.todoapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.widget.ImageView


class ToDoAdapter(
    private val todos: MutableList<ToDo>
) : RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {

    inner class ToDoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.textViewTitle)
        val descriptionTextView: TextView = itemView.findViewById(R.id.textViewDescription)
        val dueDateTextView: TextView = itemView.findViewById(R.id.textViewDueDate)
        val priorityImageView: ImageView = itemView.findViewById(R.id.imageViewPriority)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todo, parent, false)
        return ToDoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        val todo = todos[position]
        holder.titleTextView.text = todo.title
        holder.descriptionTextView.text = todo.description
        holder.dueDateTextView.text = todo.dueDate?.let { formatDate(it) } ?: ""

        val priorityImageResId = when (todo.priority) {
            2 -> R.drawable.baseline_keyboard_arrow_up_24   // Hohe Priorität
            1 -> R.drawable.baseline_horizontal_rule_24 // Mittlere Priorität
            else -> R.drawable.baseline_keyboard_arrow_down_24  // Niedrige Priorität
        }
        holder.priorityImageView.setImageResource(priorityImageResId)

        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.isChecked = todo.isDone
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            todo.isDone = isChecked
        }
    }

    private fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }

    override fun getItemCount() = todos.size

    fun addToDo(todo: ToDo) {
        todos.add(todo)
        notifyItemInserted(todos.size -1)
        Log.d("ToDoAdapter", "ToDo hinzugefügt: $todo")
    }
}