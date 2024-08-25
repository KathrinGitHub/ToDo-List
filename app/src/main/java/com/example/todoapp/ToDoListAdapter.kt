import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.ToDoList

class ToDoListAdapter(private val toDoLists: List<ToDoList>, private val onClick: (ToDoList) -> Unit) : RecyclerView.Adapter<ToDoListAdapter.ToDoListViewHolder>() {

    class ToDoListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val listNameTextView: TextView = itemView.findViewById(R.id.textViewListName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_todolist, parent, false)
        return ToDoListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ToDoListViewHolder, position: Int) {
        val toDoList = toDoLists[position]
        holder.listNameTextView.text = toDoList.name
        holder.itemView.setOnClickListener { onClick(toDoList) }
    }

    override fun getItemCount() = toDoLists.size
}
