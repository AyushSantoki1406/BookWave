package com.example.bookwave

import BookCategory
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookwave.R

class CategotyAdapter(private val imageList: List<BookCategory>) :
    RecyclerView.Adapter<CategotyAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageNameTextView: TextView = itemView.findViewById(R.id.imageNameTextView)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.categorie_item_img, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageData = imageList[position]

        holder.imageNameTextView.text = imageData.name

        holder.imageView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, Categorie_page::class.java)
            intent.putExtra("item_data", imageData.name)
            context.startActivity(intent)
        }

        // Load the image using Glide
        Glide.with(holder.itemView.context)
            .load(imageData.imageUrl)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int = imageList.size
}
