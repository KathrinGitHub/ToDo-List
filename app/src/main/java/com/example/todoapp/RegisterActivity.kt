package com.example.todoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.Toolbar

class RegisterActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val tvLogin: TextView = findViewById(R.id.tv_register_login)
        tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        //setupActionBar()
        validateUserInformation()
    }

    private fun setupActionBar() {
        val toolbarRegistration: Toolbar = findViewById(R.id.toolbar_register_activity)
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

    fun validateUserInformation(): Boolean {
        val etFirstName: EditText = findViewById(R.id.et_register_firstname)
        val etLastName: EditText = findViewById(R.id.et_register_lastname)
        val etemail: EditText = findViewById(R.id.et_register_email)
        val etPassword: EditText = findViewById(R.id.et_register_password)
        val etConfirmPassword: EditText = findViewById(R.id.et_register_confirmpassword)
        val cbTermsAndCondition: CheckBox = findViewById(R.id.cb_register_termsandcondition)

        val firstName: String = etFirstName.text.toString().trim{it <= ' '}
        val lastName: String = etLastName.text.toString().trim{it <= ' '}
        val email: String = etemail.text.toString().trim{it <= ' '}
        val password: String = etPassword.text.toString().trim{it <= ' '}
        val confirmPassword: String = etConfirmPassword.text.toString().trim{it <= ' '}

        val returnVal = when {
            firstName.isEmpty() -> {
                showCustomSnackbar("Please enter first name", true)
                false
            }

            lastName.isEmpty() -> {
                showCustomSnackbar("Please enter last name", true)
                false
            }

            email.isEmpty() -> {
                showCustomSnackbar("Please enter email", true)
                false
            }

            password.isEmpty() -> {
                showCustomSnackbar("Please enter password", true)
                false
            }

            confirmPassword.isEmpty() -> {
                showCustomSnackbar("Please enter confirm password name", true)
                false
            }

            password != confirmPassword -> {
                showCustomSnackbar("Password and confirm password does not match", true)
                false
            }

            !cbTermsAndCondition.isChecked -> {
                showCustomSnackbar("Please agree to the terms and condition", true)
                false
            }


            else -> true
        }


        return returnVal

    }

    fun registerUser() {
        if(validateUserInformation()) {
            val firstName: String = (findViewById<EditText>(R.id.et_register_firstname)).text.toString().trim{it <= ' '}
            val lastName: String = (findViewById<EditText>(R.id.et_register_lastname)).text.toString().trim{it <= ' '}
            val email: String = (findViewById<EditText>(R.id.et_register_email)).text.toString().trim{it <= ' '}
            val password: String = (findViewById<EditText>(R.id.et_register_password)).text.toString().trim{it <= ' '}
            val confirmPassword: String = (findViewById<EditText>(R.id.et_register_confirmpassword)).text.toString().trim{it <= ' '}
        }
    }
}