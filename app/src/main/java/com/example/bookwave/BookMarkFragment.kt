package com.example.bookwave

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class BookMarkFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var bookAdapter: BookMarkAdapter
    private lateinit var sharedPreferences: SharedPreferences

    private val bookList = mutableListOf<BookMarkClass>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_book_mark, container, false)
        recyclerView = view.findViewById(R.id.rcv)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize the adapter with an empty list
        bookAdapter = BookMarkAdapter(bookList)
        recyclerView.adapter = bookAdapter

        // Fetch user email from SharedPreferences

        return view
    }

    override fun onResume() {
        super.onResume()
        sharedPreferences = requireActivity().getSharedPreferences("MyApp", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("username", null)
        Log.d("132132132132132132",email.toString())
        if (email != null) {
            // Fetch saved books from Firestore
            fetchSavedBooks(email)
        } else {
            Log.d("BookMarkFragment", "Email is null")
        }

    }

    private fun fetchSavedBooks(email: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("user")  // Update the collection name to "user"
            .whereEqualTo("Email", email)
            .get()
            .addOnSuccessListener { querySnapshot ->
                Log.d("BookMarkFragment", "Query snapshot: ${querySnapshot.documents}")
                if (querySnapshot != null && !querySnapshot.isEmpty) {
                    // Assuming there is only one document per email
                    val document = querySnapshot.documents.firstOrNull()
                    val savedBooks = document?.get("savedBooks") as? List<String>
                    savedBooks?.let {
                        fetchBookDetails(it)
                    }
                } else {
                    Log.d("BookMarkFragment", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("BookMarkFragment", "Get failed with ", exception)
            }
    }



    private fun fetchBookDetails(bookNames: List<String>) {
        val db = FirebaseFirestore.getInstance()

        // Clear the bookList before adding new items
        bookList.clear()

        bookNames.forEach { bookName ->
            db.collection("books").whereEqualTo("Name", bookName)
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val name = document.getString("Name")
                        val authorName = document.getString("Author")
                        val category = document.getString("Category")
                        val rating = document.getDouble("Rating")
                        val imgUrl = document.getString("Img_url")

                        Log.d("bookMark", "Name: $name")

                        if (name != null && authorName != null && category != null && rating != null && imgUrl != null) {
                            bookList.add(BookMarkClass(name, authorName, category, rating, imgUrl))
                        }
                    }

                    // Notify adapter after adding all items
                    bookAdapter.notifyDataSetChanged()
                }
                .addOnFailureListener { exception ->
                    Log.d("BookMarkFragment", "Error getting documents: ", exception)
                }
        }
    }


}
