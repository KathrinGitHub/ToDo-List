package com.example.todoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar

class ForgotPasswordActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        setupActionBar()
    }

    private fun setupActionBar() {
        val toolbarRegistration: Toolbar = findViewById(R.id.toolbar_forgotpassword_toolbar)
        setSupportActionBar(toolbarRegistration)

        val actionBar = supportActionBar
        if(actionBar != null) {
            // actionbar element is clickable + add followning icon "<" (default: left)
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        }

        toolbarRegistration.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}