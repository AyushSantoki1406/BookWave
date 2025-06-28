package com.example.bookwave

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private  var loadingDialog : Dialog?= null

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )

        val auth : FirebaseAuth = FirebaseAuth.getInstance()

        val username = findViewById<TextInputEditText>(R.id.username)
        val password = findViewById<TextInputEditText>(R.id.password)

        val loginbtn = findViewById<Button>(R.id.login_btn)

        val signupBtn = findViewById<TextView>(R.id.signinBtn)

        val resetBtn:TextView = findViewById(R.id.forgotPassword)

        val welcome:TextView = findViewById(R.id.welcome)

        val yellowColor = resources.getColor(R.color.yellow, theme)
        val welcomeText = """
                            <font>Welcome Back!</font><br>
                             <font>to</font>
                            <font color="${String.format("#%06X", 0xFFFFFF and yellowColor)}">Book Wave</font>
                            """.trimIndent()

        welcome.text = Html.fromHtml(welcomeText, Html.FROM_HTML_MODE_LEGACY)


        resetBtn.setOnClickListener{
            startActivity(Intent(this,ForgotPassword::class.java))
        }

        signupBtn.setOnClickListener {
            startActivity(Intent(this, Signup::class.java))
        }

        loginbtn.setOnClickListener {
            val id = username.text.toString().trim()
            val pass = password.text.toString().trim()

            if (id.isEmpty()) {
                Toast.makeText(this, "Please enter your username", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (pass.isEmpty()) {
                Toast.makeText(this, "Please enter your password", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (!(pass.length >= 6)) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (id == "admin" && pass == "admin12") {
                startActivity(Intent(this, Admin::class.java))
            } else {
                // Show loading animation and blur effect
                showLoading()
                CommonUtils.applyBlurToLayout(this, 80f)

                // Firebase authentication
                auth.signInWithEmailAndPassword(id, pass)
                    .addOnCompleteListener(this) { task ->
                        // Dismiss loading animation and blur effect when login completes
                        CommonUtils.dismissLoading()
                        CommonUtils.removeBlurFromLayout(this)

                        if (task.isSuccessful) {
                            val sp = getSharedPreferences("MyApp", Context.MODE_PRIVATE)
                            val editor = sp.edit()
                            editor.putBoolean("isLoggedIn", true)
                            editor.putString("username", id)
                            editor.apply()

                            startActivity(Intent(this, MainActivity::class.java))
                        } else {
                            CommonUtils.dismissLoading()
                            CommonUtils.removeBlurFromLayout(this)
                            Toast.makeText(this, "Authentication failed", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }


    }

    fun signUp(view: View) {
        startActivity(Intent(this, Signup::class.java))

    }


    private fun hideLoading() {
        loadingDialog?.let {
            if (it.isShowing) {
                it.dismiss()
                loadingDialog = null
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.S)
    private fun showLoading() {
        loadingDialog = CommonUtils.showLoading(this)
    }

}

