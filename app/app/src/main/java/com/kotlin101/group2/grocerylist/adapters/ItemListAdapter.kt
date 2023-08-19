package com.kotlin101.group2.grocerylist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kotlin101.group2.grocerylist.R
import com.kotlin101.group2.grocerylist.data.api.models.Item
import com.squareup.picasso.Picasso

class ItemListAdapter(private val items: List<Item>) : RecyclerView.Adapter<ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card, parent, false)

        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.apply {
            if (item.imgUrl != null){
                Picasso.get().load(item.imgUrl!!.toString()).into(ivImg)
            }else{
                // base64 to image
            }
            tvName.text = item.name
            tvDesc1.text = item.description
            tvDesc2.text = "P ${item.totalPrice} | P ${item.pricePerUnit} per unit x ${item.quantity} pcs"
        }
    }
}

class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val ivImg: ImageView = itemView.findViewById(R.id.iv_item_image)
    val tvName: TextView = itemView.findViewById(R.id.tv_item_name)
    val tvDesc1: TextView = itemView.findViewById(R.id.tv_item_description_1)
    val tvDesc2: TextView = itemView.findViewById(R.id.tv_item_description_2)
}