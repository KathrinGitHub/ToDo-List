package com.example.todoapp

data class ToDo(
    val item_ID: String,
    var title: String,
    var description: String = "",
    var dueDate: Long? = null,
    var priority: Int = 0,      // 0 = niedrig, 1 = mittel, 2 = hoch
    var isDone: Boolean = false
)