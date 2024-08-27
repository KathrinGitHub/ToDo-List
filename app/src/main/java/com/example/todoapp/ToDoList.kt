package com.example.todoapp

data class ToDoList(
    var list_ID: String = "",
    var name: String = "",
    var todos: MutableList<ToDo> = mutableListOf(),
    var creator_ID: String = "",

    ) {
    init {
        addList(this)
    }

    companion object {
        private val allLists: ArrayList<ToDoList> = ArrayList()

        fun addList(list: ToDoList) {
            if(!allLists.isNullOrEmpty()) {
                for (l in allLists) {
                    if (list == l) {
                        l.name = list.name
                        l.todos = list.todos
                        l.list_ID = list.list_ID
                        l.creator_ID = list.creator_ID
                        return
                    }
                }
                allLists.add(list)
            } else {
                allLists.add(list)
            }
        }

        fun addLists(lists: MutableList<ToDoList>) {
            for (list in lists) {
                addList(list)
            }
        }

        fun getLists(): ArrayList<ToDoList> {
            return allLists
        }


    }
}
