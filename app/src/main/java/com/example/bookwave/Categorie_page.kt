package com.example.bookwave

import MainPageClass
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class Categorie_page : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_categorie_page)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        val db = FirebaseFirestore.getInstance()

        recyclerView.layoutManager = GridLayoutManager(this, 3)
        val bookList = mutableListOf<MainPageClass>()
        val adapter = MainPageAdapter(bookList)
        recyclerView.adapter = adapter

        val category: String? = intent.getStringExtra("item_data")

        if (category == null) {
            Log.e("Categorie_page", "No valid category data received")
            return
        }

        db.collection("books")
            .whereEqualTo("Category", category)
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
                adapter.notifyDataSetChanged() // Notify adapter about data changes
            }
            .addOnFailureListener {
                Log.d("Home_page_fragment", "Error fetching documents")
            }

    }
}