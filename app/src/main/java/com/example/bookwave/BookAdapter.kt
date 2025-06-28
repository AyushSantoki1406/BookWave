package com.example.bookwave

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class BookAdapter(private val bookData: MutableList<BookDataClass>) : RecyclerView.Adapter<BookAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookName: TextView = itemView.findViewById(R.id.bookName)
        val author: TextView = itemView.findViewById(R.id.authorName)
        val img: ImageView = itemView.findViewById(R.id.book_img)
        val description: TextView = itemView.findViewById(R.id.description)
        val category: TextView = itemView.findViewById(R.id.category)
        val language:TextView = itemView.findViewById(R.id.language)
        val rate:TextView = itemView.findViewById(R.id.rating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.read_book_display, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = bookData[position]
        holder.bookName.text = book.bookName
        holder.author.text = book.author
        holder.description.text = book.description
        holder.category.text = book.category
        holder.language.text = book.language
        holder.rate.text = book.rating.toString()

        Glide.with(holder.itemView.context)
            .load(book.img)
            .into(holder.img)

    }

    override fun getItemCount(): Int {
        return if (bookData.size > 10) 10 else bookData.size
    }
}

