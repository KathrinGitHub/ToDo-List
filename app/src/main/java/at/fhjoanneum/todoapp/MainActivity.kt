package at.fhjoanneum.todoapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic)

        val action: String? = intent?.action
        val data: Uri? = intent?.data

        var intent = Intent(this, LoginActivity::class.java)
        var sharedId = data?.getQueryParameter("id")
        if(sharedId != null) {
            intent.putExtra("shared_list", sharedId)
        }
        startActivity(intent)
    }
}