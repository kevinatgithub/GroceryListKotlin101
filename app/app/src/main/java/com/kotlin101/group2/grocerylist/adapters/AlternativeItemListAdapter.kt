package com.kotlin101.group2.grocerylist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.kotlin101.group2.grocerylist.R
import com.kotlin101.group2.grocerylist.data.api.models.Item

class AlternativeItemListAdapter(private val items: List<Item>,
                                 private val callback: (Int) -> Unit
) : ItemListAdapter(items, callback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.alternative_item_card, parent, false)

        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = items[position]
        holder.apply {
            ivStatus.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class AlternativeItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val card: MaterialCardView = itemView.findViewById(R.id.card)
    val ivStatus: ImageView = itemView.findViewById(R.id.ivStatus)
    val ivImg: ImageView = itemView.findViewById(R.id.iv_item_image)
    val tvName: TextView = itemView.findViewById(R.id.tv_item_name)
    val tvDesc1: TextView = itemView.findViewById(R.id.tv_item_description_1)
    val tvDesc2: TextView = itemView.findViewById(R.id.tv_item_description_2)
}