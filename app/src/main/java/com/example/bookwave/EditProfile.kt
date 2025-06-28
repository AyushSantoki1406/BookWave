package com.example.bookwave

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore

class EditProfile : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_profile)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val welcome: TextView = findViewById(R.id.welcome)

        val welcomeText = """
                            <font>Change Name</font><br>
                            """.trimIndent()

        welcome.text = Html.fromHtml(welcomeText, Html.FROM_HTML_MODE_LEGACY)

        val sp = getSharedPreferences("MyApp", Context.MODE_PRIVATE)
        val email = sp.getString("username", "")
        Log.d("email", "${email}")

        val submitBtn : Button = findViewById(R.id.submitBtn)

        submitBtn.setOnClickListener{

            val fname : TextView = findViewById(R.id.firstname)
            val lname : TextView = findViewById(R.id.lastname)

            val FirstName = fname.text.toString()
            val LastName = lname.text.toString()

            val db = FirebaseFirestore.getInstance()
            db.collection("user")
                .whereEqualTo("Email",email)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents){
                        val docRef = document.reference

                        docRef.update("First Name",FirstName,"Last Name",LastName )
                            .addOnSuccessListener {
                                Toast.makeText(this, "name added", Toast.LENGTH_SHORT)
                                startActivity(Intent(this,MainActivity::class.java))
                            }
                            .addOnFailureListener{
                                Toast.makeText(this, "name not added", Toast.LENGTH_SHORT)
                            }
                    }
                }

        }

    }
}