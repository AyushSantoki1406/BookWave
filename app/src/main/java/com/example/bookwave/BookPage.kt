package com.example.bookwave

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class BookPage : AppCompatActivity() {

    private lateinit var mainAdapter: BookAdapter
    private lateinit var heartIcon: ImageView
    private var isBookSaved = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_book_page)

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )

        val sp = getSharedPreferences("MyApp", MODE_PRIVATE)
        val id = sp.getString("username", "")
        if (id != null) {
            Log.d("123123132132",id)
        }

        heartIcon = findViewById(R.id.heartIcon)

        val name = intent.getStringExtra("book_name")
        val db = FirebaseFirestore.getInstance()

        val rcv: RecyclerView = findViewById(R.id.rv)
        val bookData = mutableListOf<BookDataClass>()
        mainAdapter = BookAdapter(bookData)
        rcv.layoutManager = LinearLayoutManager(this)
        rcv.adapter = mainAdapter

        rcv.isNestedScrollingEnabled = false

        val readButton: Button = findViewById(R.id.read_btn)
        var bookUrl: String? = null

        val backButton = findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }


        val query = db.collection("user").whereEqualTo("Email", id)
        query.get().addOnSuccessListener { querySnapshot ->
            if (!querySnapshot.isEmpty) {
                for (document in querySnapshot.documents) {
                    val savedBooks = document.get("savedBooks") as? ArrayList<String> ?: ArrayList()

                    if (savedBooks.contains(name)) {
                        // Book is already saved
                        heartIcon.setImageResource(R.drawable.heart_filled)
                        isBookSaved = true
                    } else {
                        // Book is not saved
                        heartIcon.setImageResource(R.drawable.heart_outline)
                        isBookSaved = false
                    }
                }
            } else {
                heartIcon.setImageResource(R.drawable.heart_outline)
                isBookSaved = false
            }
        }.addOnFailureListener {
            Toast.makeText(this@BookPage, "Error checking saved books", Toast.LENGTH_SHORT).show()
        }

        heartIcon.setOnClickListener {
            if (isBookSaved) {
                removeBookFromSaved(query, name)
            } else {
                addBookToSaved(query, name)
            }
        }

        db.collection("books")
            .whereEqualTo("Name", name)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val bookName = document.getString("Name")
                    val author = document.getString("Author")
                    val img = document.getString("Img_url")
                    val category = document.getString("Category")
                    val description = document.getString("Description")
                    val language = document.getString("Language")
                    val rating = document.getDouble("Rating")
                    bookUrl = document.getString("BookUrl")

                    if (bookName != null && author != null && img != null && category != null && description != null && language != null && rating != null) {
                        bookData.add(BookDataClass(bookName, author, img, category, description,language,rating))
                    }
                }
                mainAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Book not found", Toast.LENGTH_SHORT).show()
            }

        readButton.setOnClickListener {
            bookUrl?.let { url ->
                val intent = Intent(this@BookPage, ShowPdf::class.java)
                intent.putExtra("PDF_URL", url)
                startActivity(intent)
            } ?: run {
                Toast.makeText(this, "No book URL found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addBookToSaved(query: Query, name: String?) {
        heartIcon.setImageResource(R.drawable.heart_filled)
        animateHeartFill()

        query.get().addOnSuccessListener { querySnapshot ->
            if (!querySnapshot.isEmpty) {
                val document = querySnapshot.documents[0]
                val docRef = document.reference

                val savedBooks = document.get("savedBooks") as? ArrayList<String> ?: ArrayList()
                name?.let { savedBooks.add(it) }

                docRef.update("savedBooks", savedBooks)
                    .addOnSuccessListener {
                        Toast.makeText(this@BookPage, "Book Added", Toast.LENGTH_SHORT).show()
                        isBookSaved = true
                    }
                    .addOnFailureListener {
                        Toast.makeText(this@BookPage, "Book not Added", Toast.LENGTH_SHORT).show()
                    }
            } else {
                val sp = getSharedPreferences("MyAppPreferences", MODE_PRIVATE)
                val id = sp.getString("Id", "")
                val newBookAdded = arrayListOf(name)
                val newDocRef = FirebaseFirestore.getInstance().collection("user").document(id.toString())

                newDocRef.set(mapOf("savedBooks" to newBookAdded))
                    .addOnSuccessListener {
                        Toast.makeText(this@BookPage, "Book Added", Toast.LENGTH_SHORT).show()
                        isBookSaved = true
                    }
                    .addOnFailureListener {
                        Toast.makeText(this@BookPage, "Book not Added", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun removeBookFromSaved(query: Query, name: String?) {
        heartIcon.setImageResource(R.drawable.heart_outline)
        animateHeartFill() // Optional: you can animate here as well
        isBookSaved = false

        query.get().addOnSuccessListener { querySnapshot ->
            if (!querySnapshot.isEmpty) {
                for (document in querySnapshot.documents) {
                    val docRef = document.reference

                    val updates = hashMapOf<String, Any>(
                        "savedBooks" to FieldValue.arrayRemove(name)
                    )

                    docRef.update(updates).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this@BookPage, "Book Removed", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@BookPage, "Failed to Remove Book", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this@BookPage, "No books found for this user", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this@BookPage, "Failed to query books", Toast.LENGTH_SHORT).show()
        }
    }

    private fun animateHeartFill() {
        heartIcon.animate()
            .scaleX(1.2f)
            .scaleY(1.2f)
            .setDuration(150)
            .withEndAction {
                heartIcon.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .duration = 100
            }
    }
}
