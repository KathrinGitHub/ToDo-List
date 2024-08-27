package com.example.todoapp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.util.Log;
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val tvRegister: TextView = findViewById(R.id.tv_login_register)
        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }

        val tvForgotPassword: TextView = findViewById(R.id.tv_login_forgotpassword)
        tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        val btnLogin : Button = findViewById(R.id.btn_login_login)
        btnLogin.setOnClickListener {
            logInUser()
        }
    }

    fun logInUser(){
        val emailID : String = (findViewById(R.id.et_login_email)as EditText).text.toString().trim{ it <= ' '}
        val password : String = (findViewById(R.id.et_login_password)as EditText).text.toString().trim{ it <= ' '}

        if(validateLoginInformation(emailID, password)){
            FirebaseAuth
                .getInstance()
                .signInWithEmailAndPassword(emailID, password)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        CloudFirestore().getUserDetails(this)
                    } else {
                        showCustomSnackbar(task.exception!!.message.toString(), true);
                    }
                }
        }
    }

    private fun validateLoginInformation(emailID:String, password:String) : Boolean{
        return when {
            TextUtils.isEmpty(emailID) -> {
                (findViewById(R.id.til_login_email) as TextInputLayout).error = getString(R.string.errormessage_login_email)
                false
            }

            TextUtils.isEmpty(password) -> {
                (findViewById(R.id.til_login_password) as TextInputLayout).error = getString(R.string.errormessage_login_password)
                false
            }

            else -> true
        }
    }

    fun userLoggedInSuccess(user: User){
        showCustomSnackbar(getString(R.string.logged_in), false)
        val intent = Intent(this, ToDoListOverviewActivity::class.java)
        Log.d("todo-list", "start inner list")
        startActivity(intent)
        finish()
    }
}