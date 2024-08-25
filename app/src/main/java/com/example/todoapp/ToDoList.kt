package com.example.todoapp

data class ToDoList(
    val name: String,
    val todos: MutableList<ToDo>
)
