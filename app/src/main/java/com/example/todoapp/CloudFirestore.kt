package com.example.todoapp
import android.content.Context
import android.content.Entity
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await

class CloudFirestore {

    private val firestoreInstance = FirebaseFirestore.getInstance()

    fun saveUserInfoOnCloudFirestore(registrationActivity:RegistrationActivity, currentUser: User){
        firestoreInstance
            .collection(Constants.TABLENAME_USER)
            .document(currentUser.id.toString())
            .set(currentUser, SetOptions.merge())
            .addOnSuccessListener {
                registrationActivity.userRegistrationSuccess()
            }
            .addOnFailureListener { exp->
                Log.e(registrationActivity.javaClass.name, "error occured", exp)
            }
    }

    fun getUserDetails(loginActivity: LoginActivity){
        firestoreInstance
            .collection(Constants.TABLENAME_USER)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener {document ->
                val user:User = document.toObject(User::class.java)!!

                //save in the local storage who is currently logged in
                val sharedPrefences = loginActivity.getSharedPreferences(
                    Constants.FHJSTUDENTAPP_PREFERENCES, Context.MODE_PRIVATE)
                with(sharedPrefences.edit()){
                    putString(Constants.USERNAME_OF_LOGGED_USER, "${user.firstName}")
                    putString(Constants.UID_OF_LOGGED_USER, "${user.id}")
                    apply()
                }

                loginActivity.userLoggedInSuccess(user)
            }
            .addOnFailureListener { exp ->
                loginActivity.showCustomSnackbar("an error occured :( \nPlease try again later", true)
                Log.e(loginActivity.javaClass.name, "error occured", exp)
            }
    }

    private fun getCurrentUserID() : String{
        val currentUser = FirebaseAuth.getInstance().currentUser
        if(currentUser != null)
            return currentUser.uid

        return ""
    }

    /*
    fun initAllLists(mainActivity: MainActivity) {
        firestoreInstance
            .collection(Constants.TABLENAME_PLANT)
            .whereEqualTo("userId", getCurrentUserID())
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val userPlant: UserPlant = document.toObject(UserPlant::class.java)
                    UserPlant.addUserPlant(userPlant)
                }
            }
            .addOnFailureListener{ exp ->
                Log.e(mainActivity.javaClass.name, "error occurred", exp)
            }
    }

    fun deleteList(plant: UserPlant, activity: AppCompatActivity) {
        firestoreInstance
            .collection(Constants.TABLENAME_PLANT)
            .document(plant.firebaseId.toString())
            .delete()
            .addOnSuccessListener {
                Toast.makeText(activity, "Plant was deleted successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exp ->
                Log.e(activity.javaClass.name, "error occurred", exp)
            }
    }

     */
}