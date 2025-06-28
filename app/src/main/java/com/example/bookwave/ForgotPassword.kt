package com.example.bookwave

import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import jp.wasabeef.blurry.Blurry

class ForgotPassword : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private  var loadingDialog : Dialog?= null

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )

        setContentView(R.layout.activity_forgot_password)

        val resetBtn: Button = findViewById(R.id.resetBtn)
        val emailInput: TextInputEditText = findViewById(R.id.username)

        mAuth = FirebaseAuth.getInstance()

        resetBtn.setOnClickListener {
            val email = emailInput.text.toString().trim()
            if (email.isNotEmpty()) {
                showLoading()
                CommonUtils2.applyBlurToLayout(findViewById(R.id.main), 80f)
                CommonUtils2.showLoading(this)
                mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            hideLoading()
                            CommonUtils2.removeBlurFromLayout(findViewById(R.id.main))
                            CommonUtils2.dismissLoading()
                            Toast.makeText(this, "Check your email", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this,Login::class.java))
                        } else {
                            hideLoading()
                            CommonUtils2.removeBlurFromLayout(findViewById(R.id.main))
                            CommonUtils2.dismissLoading()
                            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Email field cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun hideLoading() {
        loadingDialog?.let { if (it.isShowing) it.dismiss() }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun showLoading() {
        loadingDialog = CommonUtils2.showLoading(this)
    }
}
