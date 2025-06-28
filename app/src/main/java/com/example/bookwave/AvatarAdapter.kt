package com.example.bookwave

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AvatarAdapter(private val mList: List<AvatarViewModel> , private val onItemClick: (AvatarViewModel) -> Unit) : RecyclerView.Adapter<AvatarAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.avatar_preview, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemViewModel = mList[position]
        Glide.with(holder.itemView.context)
            .load(itemViewModel.Image)
            .into(holder.imageView)

        holder.itemView.setOnClickListener{
            onItemClick(itemViewModel)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.AvatarImg)
    }
}
