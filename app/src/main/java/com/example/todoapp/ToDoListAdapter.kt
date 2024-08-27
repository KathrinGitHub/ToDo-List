package com.example.todoapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ToDoListAdapter(
    private val toDoLists: MutableList<ToDoList>
) : RecyclerView.Adapter<ToDoListAdapter.ToDoListViewHolder>() {

    lateinit var onClick: (ToDoList) -> Unit
    lateinit var onDeleteClick: (ToDoList) -> Unit

    inner class ToDoListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val deleteIcon: ImageView = itemView.findViewById(R.id.imageViewDelete)
        val listNameTextView: TextView = itemView.findViewById(R.id.textViewListName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todolist, parent, false)
        return ToDoListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ToDoListViewHolder, position: Int) {
        val toDoList = toDoLists[position]
        holder.listNameTextView.text = toDoList.name
        holder.listNameTextView.setOnClickListener { onClick(toDoList) }
        holder.deleteIcon.setOnClickListener { onDeleteClick(toDoList) }
    }

    override fun getItemCount() = toDoLists.size

    fun addToDoList(toDoList: ToDoList) {
        toDoLists.add(toDoList)
        notifyItemInserted(toDoLists.size - 1)
    }

    fun removeToDoList(toDoList: ToDoList) {
        val position = toDoLists.indexOf(toDoList)
        if (position != -1) {
            toDoLists.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}


