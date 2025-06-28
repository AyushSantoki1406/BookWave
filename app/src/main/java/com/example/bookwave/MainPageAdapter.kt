package com.example.bookwave

import MainPageClass
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MainPageAdapter(private val mList: List<MainPageClass>) : RecyclerView.Adapter<MainPageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.main_page_book_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemViewModel = mList[position]
        holder.name.text = itemViewModel.name
        holder.authorName.text = itemViewModel.authorName
//        holder.category.text = itemViewModel.category
//        holder.description.text = itemViewModel.description

        Glide.with(holder.itemView.context)
            .load(itemViewModel.imgUrl)
            .into(holder.imgUrl)

        holder.itemView.setOnClickListener {
            val content = holder.itemView.context
            val intent = Intent(content,BookPage::class.java)
            intent.putExtra("book_name",itemViewModel.name)
            content.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return if (mList.size > 10) 10 else mList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.bookName)
        val authorName: TextView = itemView.findViewById(R.id.authorName)
        //        val category: TextView = itemView.findViewById(R.id.category)
//        val description: TextView = itemView.findViewById(R.id.description)
        val imgUrl: ImageView = itemView.findViewById(R.id.img)
    }
}
