package com.example.bookwave

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.bookwave.databinding.FragmentSettingsBinding
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView
import org.w3c.dom.Text

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var profileImageView: CircleImageView
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout and initialize binding
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        // Initialize SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences("ProfilePrefs", Context.MODE_PRIVATE)

        // Initialize views
        profileImageView = binding.profileImageIcon

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set click listener for profile image
        profileImageView.setOnClickListener {
            val intent = Intent(requireContext(), Profile::class.java)
            startActivity(intent)
        }

        val logout : TextView = binding.logoutBtn
        logout.setOnClickListener{
            val sharedPref = requireActivity().getSharedPreferences("MyApp", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putBoolean("isLoggedIn", false)
            editor.apply()
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(requireContext(), Login::class.java))
            requireActivity().finish()
        }

        val change_password:TextView = binding.changePassword
        change_password.setOnClickListener{
            startActivity(Intent(requireContext(),ForgotPassword::class.java))
        }

        val editProfile : TextView = binding.editProfile
        editProfile.setOnClickListener{
            startActivity(Intent(requireContext(),EditProfile::class.java))
        }

        val privacy : TextView = binding.privacy
        privacy.setOnClickListener{
            startActivity(Intent(requireContext(),Privacy::class.java))
        }

        val contactUs : TextView = binding.contact
        contactUs.setOnClickListener{
            startActivity(Intent(requireContext(),ContactUs::class.java))
        }

        val aboutUs : TextView = binding.about
        aboutUs.setOnClickListener{
            startActivity(Intent(requireContext(),AboutUs::class.java))
        }

        val rateUs : TextView = binding.rateus
        rateUs.setOnClickListener{
            startActivity(Intent(requireContext(),RateUs::class.java))
        }

        val switchBtn: SwitchMaterial = binding.switchBtn

        if (sharedPreferences.contains("night_mode")) {
            val isNightMode = sharedPreferences.getBoolean("night_mode", false)
            switchBtn.isChecked = isNightMode
            AppCompatDelegate.setDefaultNightMode(
                if (isNightMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
        } else {
            // Follow system default on the first launch
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }

        // Set the initial text based on the saved or system mode
        switchBtn.text = if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            "Night Mode"
        } else {
            "Light Mode"
        }

        // Set up listener for switch toggle
        switchBtn.setOnCheckedChangeListener { buttonView, isChecked ->
            sharedPreferences.edit().putBoolean("night_mode", isChecked).apply()

            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        // Get the saved avatar resource ID
        val savedAvatarResId = sharedPreferences.getInt("selected_avatar", R.drawable.user)

        // Load the saved avatar using Glide
        Glide.with(this)
            .load(savedAvatarResId)
            .placeholder(R.drawable.user)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    e?.logRootCauses("Glide")
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
    }
}
