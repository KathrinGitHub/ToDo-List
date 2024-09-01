package at.fhjoanneum.todoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ToDoListAdapter(
    private val toDoLists: MutableList<ToDoList>
) : RecyclerView.Adapter<ToDoListAdapter.ToDoListViewHolder>() {

    var onClick: ((ToDoList) -> Unit)? = null

    inner class ToDoListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var listNameTextView: TextView = itemView.findViewById(R.id.textViewListName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todolist, parent, false)
        return ToDoListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ToDoListViewHolder, position: Int) {
        val toDoList = toDoLists[position]
        holder.listNameTextView.text = toDoList.name
        holder.listNameTextView.setOnClickListener { onClick?.invoke(toDoList) }
    }

    override fun getItemCount() = toDoLists.size

    fun addToDoList(toDoList: ToDoList) {
        toDoLists.add(toDoList)
        notifyItemInserted(toDoLists.size - 1)
    }

}


