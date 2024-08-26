package com.example.todoapp

import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth

class RegistrationActivity : BasicActivity() {
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

    fun registerNewUser() {
        if(validateUserInformation()){
            val firstName:String = (findViewById(R.id.et_register_firstname) as EditText).text.toString().trim { it <= ' '}
            val lastName:String = (findViewById(R.id.et_register_lastname) as EditText).text.toString().trim { it <= ' '}
            val email:String = (findViewById(R.id.et_register_email) as EditText).text.toString().trim { it <= ' '}
            val password:String = (findViewById(R.id.et_register_password) as EditText).text.toString().trim { it <= ' '}
            val confirmPassword:String = (findViewById(R.id.et_register_confirmpassword) as EditText).text.toString().trim { it <= ' '}

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        val firebaseUser = task.result!!.user!!
                        showCustomSnackbar("A new user is created with Firebase-ID ${firebaseUser.uid}", false)

                        //TODO: Save the rest in CloudFirestore
                        val user = User(firebaseUser.uid, firstName, lastName, email)
                        CloudFirestore().saveUserInfoOnCloudFirestore(this, user)

                    } else {
                        showCustomSnackbar(task.exception!!.toString(), true)
                    }
                }
        }
    }

    fun userRegistrationSuccess(){
        Toast.makeText(this, "You are registered successfully.", Toast.LENGTH_SHORT).show()
    }
}