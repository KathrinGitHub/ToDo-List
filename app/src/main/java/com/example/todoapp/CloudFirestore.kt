package com.example.todoapp
import android.content.Context
import android.content.Entity
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.toObject
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
                onFailure(registrationActivity, exp)
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
                    Constants.TODOAPP_PREFERENCES, Context.MODE_PRIVATE)
                with(sharedPrefences.edit()){
                    putString(Constants.USERNAME_OF_LOGGED_USER, "${user.firstName}")
                    putString(Constants.UID_OF_LOGGED_USER, "${user.id}")
                    apply()
                }

                loginActivity.userLoggedInSuccess(user)
            }
            .addOnFailureListener { exp ->
                onFailure(loginActivity, exp)
            }
    }

    private fun getCurrentUserID() : String{
        val currentUser = FirebaseAuth.getInstance().currentUser
        if(currentUser != null)
            return currentUser.uid

        return ""
    }

    fun getUserLists(toDoListOverviewActivity: ToDoListOverviewActivity, callback: (ArrayList<ToDoList>) -> Unit){

        var lists = ArrayList<ToDoList>()
        val loggedInUserId = toDoListOverviewActivity.getSharedPreferences(Constants.TODOAPP_PREFERENCES, MODE_PRIVATE)
            .getString(Constants.UID_OF_LOGGED_USER, "")

        firestoreInstance
            .collection(Constants.TABLENAME_LISTACCESS)
            .whereEqualTo("user_ID", loggedInUserId)
            .get()
            .addOnSuccessListener { accessSnapshot ->
                if (!accessSnapshot.isEmpty) {
                    val listIds = accessSnapshot.documents.mapNotNull { it.getString("list_ID") }

                    fetchListsByIds(toDoListOverviewActivity, listIds) { callbackLists ->
                        lists = callbackLists
                        lists.sortBy { l -> l.name }
                        callback(lists)
                    }
                }
            }
            .addOnFailureListener { exp ->
                onFailure(toDoListOverviewActivity, exp)
                callback(lists)
            }
    }

    private fun fetchListsByIds(toDoListOverviewActivity: ToDoListOverviewActivity, listIds: List<String>, callback: (ArrayList<ToDoList>) -> Unit) {

        var lists = ArrayList<ToDoList>()

        if (listIds.isNotEmpty()) {
            // Query the lists collection for documents matching the retrieved listIds
            listIds.forEach { listId ->
                firestoreInstance
                    .collection(Constants.TABLENAME_LIST)
                    .whereEqualTo("list_ID", listId).get()
                    .addOnSuccessListener { listsSnapshot ->
                        lists.add(listsSnapshot.first().toObject(ToDoList::class.java))
                        callback(lists)
                    }
                    .addOnFailureListener { exp ->
                        onFailure(toDoListOverviewActivity, exp)
                        callback(lists)
                    }
            }
        }
    }

    fun saveListOnCloudFirestore(toDoListOverviewActivity: ToDoListOverviewActivity, toDoList: ToDoList) {

        val loggedInUserId = toDoListOverviewActivity.getSharedPreferences(Constants.TODOAPP_PREFERENCES, MODE_PRIVATE)
            .getString(Constants.UID_OF_LOGGED_USER, "")

        firestoreInstance
            .collection(Constants.TABLENAME_LIST)
            .document(toDoList.list_ID)
            .set(toDoList, SetOptions.merge())
            .addOnSuccessListener {
                val combinedID = toDoList.list_ID + loggedInUserId
                val accessData: Access = Access(toDoList.list_ID, loggedInUserId.toString())
                firestoreInstance
                    .collection(Constants.TABLENAME_LISTACCESS)
                    .document(combinedID)
                    .set(accessData, SetOptions.merge())
                    .addOnSuccessListener {
                        // TODO:
                        //toDoListOverviewActivity.saveSuccess()
                    }.addOnFailureListener { exception ->
                        onFailure(toDoListOverviewActivity, exception)
                    }
            }
            .addOnFailureListener { exp->
                onFailure(toDoListOverviewActivity, exp)
            }


    }

    private fun onFailure(activity: BasicActivity, exp: Exception) {
        activity.showCustomSnackbar("an error occured :( \nPlease try again later", true)
        Log.e(activity.javaClass.name, "error occured", exp)
    }

    fun deleteListFromCloudFirestore(context: Context, toDoList: ToDoList) {

        val loggedInUserId = context.getSharedPreferences(Constants.TODOAPP_PREFERENCES, MODE_PRIVATE)
            .getString(Constants.UID_OF_LOGGED_USER, "")
        val combinedID = toDoList.list_ID + loggedInUserId

        firestoreInstance
            .collection(Constants.TABLENAME_LIST)
            .document(toDoList.list_ID)
            .delete()
            .addOnSuccessListener {
                firestoreInstance
                    .collection(Constants.TABLENAME_LISTACCESS)
                    .document(combinedID)
                    .delete()
                    .addOnSuccessListener {
                        Log.d("CloudFirestore", "List successfully deleted")
                        Toast.makeText(context, "List deleted successfully", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Log.e("CloudFirestore", "Error deleting access entry", e)
                    }
            }
            .addOnFailureListener { e ->
                Log.e("CloudFirestore", "Error deleting list", e)
                Toast.makeText(context, "Failed to delete list", Toast.LENGTH_SHORT).show()
            }
    }

    fun updateToDoItems(context: Context, toDoList: ToDoList, isAdd: Boolean) {

        firestoreInstance
            .collection(Constants.TABLENAME_LIST)
            .document(toDoList.list_ID)
            .update("todos", toDoList.todos)
            .addOnSuccessListener {
                if(isAdd) {
                    Log.d("CloudFirestore", "Item added successfully")
                    Toast.makeText(context, "Item added successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Log.d("CloudFirestore", "Item deleted successfully")
                    Toast.makeText(context, "Item deleted successfully", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                if(isAdd) {
                    Log.e("CloudFirestore", "Error adding item", e)
                    Toast.makeText(context, "Error adding item", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("CloudFirestore", "Error deleting item", e)
                    Toast.makeText(context, "Error deleting item", Toast.LENGTH_SHORT).show()
                }
            }

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