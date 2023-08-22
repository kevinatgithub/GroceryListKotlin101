package com.kotlin101.group2.grocerylist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.kotlin101.group2.grocerylist.R
import com.squareup.picasso.Picasso

class ImageSearchAdapter(private val result: List<String>, private val callback: (url: String) -> Unit) : RecyclerView.Adapter<ImageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.image_card, parent, false)

        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return result.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val url = result.get(position)
        if (url.isEmpty()){
            return
        }
        holder.apply {
            Picasso.get().load(url).into(ivImg)
            ivImg.setOnClickListener {
                callback(url)
            }
        }
    }
}

class ImageViewHolder(imageView: View) : RecyclerView.ViewHolder(imageView){
    val ivImg: ImageView = imageView.findViewById(R.id.ivImage)
}