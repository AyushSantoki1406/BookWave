package com.example.bookwave

import ImageSliderAdapter
import MainPageClass
import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.init
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Timer
import java.util.TimerTask
import kotlin.math.abs

class HomeFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var profileImageView: ImageView
    private lateinit var nameTextView: TextView

    private lateinit var viewPager2: ViewPager2
    private lateinit var imageList: ArrayList<Int>
    private lateinit var handler: Handler
    private lateinit var adapter: ImageSliderAdapter

    private lateinit var progressBar: View


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize views from the inflated layout
        profileImageView = view.findViewById(R.id.profile_image)
        nameTextView = view.findViewById(R.id.name)
        viewPager2 = view.findViewById(R.id.vp2) // Initialize viewPager2 here

        // Initialize other components
        init(view) // Pass view to init
        setUpTransformer()

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable,2000)
            }
        })

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        val db = FirebaseFirestore.getInstance()

        recyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        val bookList = mutableListOf<MainPageClass>()
        val adapter = MainPageAdapter(bookList)
        recyclerView.adapter = adapter

        progressBar = view.findViewById(R.id.progressBar)

        progressBar.visibility = View.VISIBLE

        db.collection("books")
            .whereGreaterThan("Rating", 4.5)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val name = document.getString("Name")
                    val authorName = document.getString("Author")
                    val category = document.getString("Category")
                    val description = document.getString("Description")
                    val imgUrl = document.getString("Img_url")

                    Log.d("Home_page_fragment", "Name: $name")

                    if (name != null && authorName != null && category != null && description != null && imgUrl != null) {
                        bookList.add(MainPageClass(name, authorName, category, description, imgUrl))
                    }
                }
                adapter.notifyDataSetChanged() // Notify ad
                progressBar.visibility = View.GONE// apter about data changes
            }
            .addOnFailureListener {
                Log.d("Home_page_fragment", "Error fetching documents")
                progressBar.visibility = View.GONE
            }

        val recyclerView2: RecyclerView = view.findViewById(R.id.recyclerView2)
        recyclerView2.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val romanceBookList = mutableListOf<MainPageClass>()
        val romanceAdapter = MainPageAdapter(romanceBookList)
        recyclerView2.adapter = romanceAdapter

        progressBar.visibility = View.VISIBLE

        db.collection("books")
            .whereEqualTo("Category", "Romance")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val name = document.getString("Name")
                    val authorName = document.getString("Author")
                    val category = document.getString("Category")
                    val description = document.getString("Description")
                    val imgUrl = document.getString("Img_url")

                    Log.d("Home_page_fragment", "Name: $name")

                    if (name != null && authorName != null && category != null && description != null && imgUrl != null) {
                        romanceBookList.add(MainPageClass(name, authorName, category, description, imgUrl))
                    }
                }
                romanceAdapter.notifyDataSetChanged() // Notify a
                progressBar.visibility = View.GONE// dapter about data changes
            }
            .addOnFailureListener {
                Log.d("Home_page_fragment", "Error fetching documents")
                progressBar.visibility = View.GONE
            }


        return view
    }

    private val runnable = Runnable{
        viewPager2.currentItem = viewPager2.currentItem + 1
    }

    private fun setUpTransformer() {
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r + 0.14f
        }

        viewPager2.setPageTransformer(transformer)
    }

    private fun init(view: View) {
        handler = Handler(Looper.myLooper()!!)
        imageList = ArrayList()

        imageList.add(R.drawable.img1)
        imageList.add(R.drawable.img2)
        imageList.add(R.drawable.img3)

        adapter = ImageSliderAdapter(imageList, viewPager2)

        viewPager2.adapter = adapter
        viewPager2.offscreenPageLimit = 3
        viewPager2.clipToPadding = false
        viewPager2.clipChildren = false
        viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

    override fun onResume() {
        super.onResume()

        handler.postDelayed(runnable, 2000)

        // Initialize SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences("ProfilePrefs", Context.MODE_PRIVATE)

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

        // Initialize Firebase Firestore
        val db = FirebaseFirestore.getInstance()

        // Get email from SharedPreferences
        sharedPreferences = requireActivity().getSharedPreferences("MyApp", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("username", "")

        // Fetch user details from Firestore
        if (email != null) {
            Log.d("email", email)
        }

        db.collection("user")
            .whereEqualTo("Email", email)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val firstName = document.getString("First Name")
                    Log.d("Name", firstName.toString())
                    nameTextView.text = firstName
                }
            }
    }
}
