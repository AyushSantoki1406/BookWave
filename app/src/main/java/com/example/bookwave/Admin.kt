package com.example.bookwave

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore

class Admin : AppCompatActivity() {

    private lateinit var authorEditText: EditText
    private lateinit var bookUrlEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var imgUrlEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var ratingEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var categoryEditText: TextInputEditText
    private lateinit var languageEditText: TextInputEditText

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        // Initialize UI elements
        authorEditText = findViewById(R.id.authorEditText)
        bookUrlEditText = findViewById(R.id.bookUrlEditText)
        descriptionEditText = findViewById(R.id.descriptionEditText)
        imgUrlEditText = findViewById(R.id.imgUrlEditText)
        nameEditText = findViewById(R.id.nameEditText)
        ratingEditText = findViewById(R.id.ratingEditText)
        submitButton = findViewById(R.id.submitButton)
        categoryEditText = findViewById(R.id.categoryEditText)
        languageEditText = findViewById(R.id.languageEditText)

        // Set up the PopupMenu for Category
        categoryEditText.setOnClickListener {
            showPopupMenu(it, R.menu.category_menu) { item ->
                categoryEditText.setText(item.title)
            }
        }

        // Set up the PopupMenu for Language
        languageEditText.setOnClickListener {
            showPopupMenu(it, R.menu.language_menu) { item ->
                languageEditText.setText(item.title)
            }
        }

        submitButton.setOnClickListener {
            submitData()
        }
    }

    private fun showPopupMenu(view: View, menuRes: Int, onMenuItemClick: (MenuItem) -> Unit) {
        val popup = PopupMenu(this, view)
        popup.menuInflater.inflate(menuRes, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            onMenuItemClick(item)
            true
        }
        popup.show()
    }

    private fun submitData() {
        val author = authorEditText.text.toString().trim()
        val bookUrl = bookUrlEditText.text.toString().trim()
        val category = categoryEditText.text.toString().trim()
        val description = descriptionEditText.text.toString().trim()
        val imgUrl = imgUrlEditText.text.toString().trim()
        val language = languageEditText.text.toString().trim()
        val name = nameEditText.text.toString().trim()
        val rating = ratingEditText.text.toString().trim().toDoubleOrNull() ?: 0.0

        // Validate fields are not empty
        if (author.isEmpty() || bookUrl.isEmpty() || category.isEmpty() || description.isEmpty()
            || imgUrl.isEmpty() || language.isEmpty() || name.isEmpty() || ratingEditText.text.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a map for the data to submit
        val bookData = hashMapOf(
            "Author" to author,
            "BookUrl" to bookUrl,
            "Category" to category,
            "Description" to description,
            "Img_url" to imgUrl,
            "Language" to language,
            "Name" to name,
            "Rating" to rating
        )

        // Submit to Firestore
        db.collection("books")
            .add(bookData)
            .addOnSuccessListener {
                Toast.makeText(this, "Book added successfully!", Toast.LENGTH_SHORT).show()
                clearFields()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to add book: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun clearFields() {
        authorEditText.text.clear()
        bookUrlEditText.text.clear()
        categoryEditText.text?.clear()
        descriptionEditText.text.clear()
        imgUrlEditText.text.clear()
        languageEditText.text?.clear()
        nameEditText.text.clear()
        ratingEditText.text.clear()
    }
}
