package com.example.bookwave

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore

class Signup : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private  var loadingDialog : Dialog ?= null

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val welcome:TextView = findViewById(R.id.welcome)

        val yellowColor = resources.getColor(R.color.yellow, theme)
        val welcomeText = """
                            <font>Welcome!</font><br>
                            <font>to</font>
                            <font color="${String.format("#%06X", 0xFFFFFF and yellowColor)}">Book Wave</font>
                            """.trimIndent()

        welcome.text = Html.fromHtml(welcomeText, Html.FROM_HTML_MODE_LEGACY)

        val signUp: Button = findViewById(R.id.signinBtn)

        signUp.setOnClickListener {

            val userName: TextInputEditText = findViewById(R.id.username)
            val passWord: TextInputEditText = findViewById(R.id.password)
            val confirmPass: TextInputEditText = findViewById(R.id.confirmPassword)

            val username = userName.text.toString()
            val password = passWord.text.toString()
            val confirmPassword = confirmPass.text.toString()

            Log.d("usernamr", username)
            Log.d("password", password)
            Log.d("confirmPassword", confirmPassword)

            if (confirmPassword.equals(password)) {
                if (username.isNotEmpty() && password.isNotEmpty()) {
                    if (password.length >= 6) {
                        CommonUtils.applyBlurToLayout(this, 80f)
                        val dialog = CommonUtils.showLoading(this)
                        auth.createUserWithEmailAndPassword(username, password)
                            .addOnCompleteListener(this) { task ->
                                if (task.isSuccessful) {

                                    val sp = getSharedPreferences("MyApp", Context.MODE_PRIVATE)
                                    val editor = sp.edit()
                                    editor.putBoolean("isLoggedIn", true)
                                    editor.putString("username", username)
                                    editor.apply()

                                    val user = auth.currentUser

                                    user?.let {
                                        val email = it.email

                                        val newUser = hashMapOf(
                                            "Email" to email
                                        )

                                        db.collection("user")
                                            .add(newUser)
                                            .addOnSuccessListener {
                                                Toast.makeText(this, "User Added", Toast.LENGTH_SHORT).show()
                                            }
                                            .addOnFailureListener {
                                                Toast.makeText(this, "User Not Added", Toast.LENGTH_SHORT).show()
                                            }
                                    }
                                    CommonUtils.dismissLoading()
                                    CommonUtils.removeBlurFromLayout(this)
                                    startActivity(Intent(this, AddName::class.java))
                                }
                                else {
                                    CommonUtils.dismissLoading()
                                    CommonUtils.removeBlurFromLayout(this)
                                    Toast.makeText(this, "Authentication Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun login(view: View) {
        startActivity(Intent(this, Login::class.java))
    }


    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.dismiss() }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun showLoading() {
        loadingDialog = CommonUtils.showLoading(this)
    }

}
