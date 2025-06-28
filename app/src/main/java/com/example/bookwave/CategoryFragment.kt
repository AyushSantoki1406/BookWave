package com.example.bookwave

import BookCategory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class CategoryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Firebase Firestore
        val db = FirebaseFirestore.getInstance()

        // Initialize RecyclerView
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(),2)

        val imageList = mutableListOf<BookCategory>()
        val adapter = CategotyAdapter(imageList)
        recyclerView.adapter = adapter

        // Fetch data from Firestore and update the adapter
        db.collection("categories")
            .get()
            .addOnSuccessListener { documents ->
                Log.d("Firestore", "Successfully fetched ${documents.size()} documents.")
                if (documents.isEmpty) {
                    Log.d("Firestore", "No documents found.")
                } else {
                    for (document in documents) {
                        val imageName = document.getString("type")
                        val imageUrl = document.getString("img_url")
                        Log.d("Firestore", "Fetched document: name=$imageName, img_url=$imageUrl")

                        if (imageName != null && imageUrl != null) {
                            imageList.add(BookCategory(imageName, imageUrl))
                        } else {
                            Log.d("Firestore", "Skipping document with missing fields: name=$imageName, img_url=$imageUrl")
                        }
                    }

                    adapter.notifyDataSetChanged()
                }
            }
            .addOnFailureListener { exception ->
                Log.d("Firestore", "Error getting documents: ", exception)
            }
    }



}