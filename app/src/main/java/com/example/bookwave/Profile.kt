package com.example.bookwave

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class Profile : AppCompatActivity() {

    private lateinit var profileImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )

        profileImageView = findViewById(R.id.profile_image)  // Initialize the profile image view

        val back: ImageView = findViewById(R.id.back)
        back.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        val sharedPreferences = getSharedPreferences("ProfilePrefs", Context.MODE_PRIVATE)
        val savedAvatarResId = sharedPreferences.getInt("selected_avatar", R.drawable.user)

        Glide.with(this)
            .load(savedAvatarResId)
            .placeholder(R.drawable.user) // Optional: Add a placeholder
            .listener(object : RequestListener<Drawable> {

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }
            })
            .into(profileImageView)

        val avatarRecycleView: RecyclerView = findViewById(R.id.profilePicturesRecyclerView)
        avatarRecycleView.layoutManager = GridLayoutManager(this, 3)
        avatarRecycleView.setHasFixedSize(true)

        val data = ArrayList<AvatarViewModel>()
        data.add(AvatarViewModel(R.drawable.a1))
        data.add(AvatarViewModel(R.drawable.a2))
        data.add(AvatarViewModel(R.drawable.a3))
        data.add(AvatarViewModel(R.drawable.a4))
        data.add(AvatarViewModel(R.drawable.a5))
        data.add(AvatarViewModel(R.drawable.a6))
        data.add(AvatarViewModel(R.drawable.a7))
        data.add(AvatarViewModel(R.drawable.a8))
        data.add(AvatarViewModel(R.drawable.a9))

        val adapter = AvatarAdapter(data) { avatarViewModel ->
            val avatarResId = avatarViewModel.Image

            Glide.with(this)
                .load(avatarResId)
                .placeholder(R.drawable.user)  // Use a placeholder while loading
                .listener(object : RequestListener<Drawable> {

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }
                })
                .into(profileImageView)

            // Save the selected avatar to SharedPreferences
            sharedPreferences.edit().putInt("selected_avatar", avatarResId).apply()
        }

        avatarRecycleView.adapter = adapter
    }
}
