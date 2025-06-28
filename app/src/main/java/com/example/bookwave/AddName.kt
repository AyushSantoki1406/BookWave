package com.example.bookwave

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import java.util.HashMap

class AddName : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_name)

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )

        val welcome: TextView = findViewById(R.id.welcome)

        val yellowColor = resources.getColor(R.color.yellow, theme)
        val welcomeText = """
                            <font>Welcome!</font><br>
                            <font color="${String.format("#%06X", 0xFFFFFF and yellowColor)}">Book Wave</font>
                            """.trimIndent()

        welcome.text = Html.fromHtml(welcomeText, Html.FROM_HTML_MODE_LEGACY)

        sharedPreferences = getSharedPreferences("MyApp", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "")

        val submitBtn:Button = findViewById(R.id.submitBtn)
        submitBtn.setOnClickListener {

            val db = FirebaseFirestore.getInstance()

            val firstName:TextInputEditText = findViewById(R.id.firstname)
            val lastName:TextInputEditText = findViewById(R.id.lastname)

            val firstNameText = firstName.text.toString()
            val lastNameText = lastName.text.toString()

            db.collection("user")
                .whereEqualTo("Email", username)
                .get()
                .addOnSuccessListener {documents ->
                    for (document in documents){
                        val data = hashMapOf(
                            "First Name" to firstNameText,
                            "Last Name" to lastNameText
                        )
                        db.collection("user").document(document.id).update(data as Map<String,Any>)
                            .addOnCompleteListener{
                                Toast.makeText(this, "name added", Toast.LENGTH_SHORT)
                                    .show()
                                startActivity(Intent(this,MainActivity::class.java))
                            }
                            .addOnFailureListener{
                                Toast.makeText(
                                    this,
                                    "Something went wrong!!!!!!!!!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                }
        }

    }
}