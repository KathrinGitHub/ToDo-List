package com.example.todoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar

open class BasicActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic)
    }

    fun showCustomSnackbar(message:String, errorMessage:Boolean){
        val snackbar:Snackbar = Snackbar.make(findViewById(android.R.id.content),message, Snackbar.LENGTH_LONG)

        if(errorMessage)
            snackbar.view.setBackgroundColor(getColor(R.color.error_red))
        else
            snackbar.view.setBackgroundColor(getColor(R.color.success_green))
        snackbar.show()
    }
}