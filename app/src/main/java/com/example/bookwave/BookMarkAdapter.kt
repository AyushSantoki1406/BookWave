package com.example.bookwave

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class BookMarkAdapter(private val bookList: List<BookMarkClass>) : RecyclerView.Adapter<BookMarkAdapter.BookViewHolder>() {

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookImg: ImageView = itemView.findViewById(R.id.book_img)
        val bookName: TextView = itemView.findViewById(R.id.bookName)
        val authorName: TextView = itemView.findViewById(R.id.authorName)
        val category: TextView = itemView.findViewById(R.id.category)
        val rating: TextView = itemView.findViewById(R.id.rating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bookmark_page_layout, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = bookList[position]
        holder.bookName.text = book.bookName
        holder.authorName.text = book.authorName
        holder.category.text = book.category
        holder.rating.text = book.rating.toString()

        // Load image using Glide if imageURL is provided
        if (book.imageURL.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(book.imageURL)
                .into(holder.bookImg)
        } else {
            holder.bookImg.setImageResource(R.drawable.logo)
        }

        holder.itemView.setOnClickListener {
            val content = holder.itemView.context
            val intent = Intent(content,BookPage::class.java)
            intent.putExtra("book_name",book.bookName)
            content.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return bookList.size
    }
}
