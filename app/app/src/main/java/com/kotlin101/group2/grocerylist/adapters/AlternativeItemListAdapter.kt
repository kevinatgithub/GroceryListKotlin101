package com.kotlin101.group2.grocerylist.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.kotlin101.group2.grocerylist.GroceryAppHelpers
import com.kotlin101.group2.grocerylist.R
import com.kotlin101.group2.grocerylist.data.api.models.Item
import com.kotlin101.group2.grocerylist.data.db.LocalItem
import com.squareup.picasso.Picasso

open class AlternativeItemListAdapter(private val items: List<Item>, private val callback: (itemId:Int) -> Unit) : RecyclerView.Adapter<AlternativeItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlternativeItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.alternative_item_card, parent, false)

        return AlternativeItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: AlternativeItemViewHolder, position: Int) {
        val item = items[position]
        holder.apply {
            card.setOnClickListener {
                callback(item.id)
            }
            btnPriority.setOnClickListener {
                callback(item.id)
            }
            if (item.imgUrl != null && !item.imgUrl.toString().isEmpty()){
                Picasso.get().load(item.imgUrl!!.toString()).into(ivImg)
            }
            tvName.text = GroceryAppHelpers.applyText(item.name)
            tvDesc1.text = GroceryAppHelpers.applyText(item.description)
            tvDesc2.text = "P ${GroceryAppHelpers.applyText(item.totalPrice)} | P ${
                GroceryAppHelpers.applyText(
                    item.pricePerUnit
                )
            } per unit x ${GroceryAppHelpers.applyText(item.quantity)} pcs"
            ivStatus.visibility = View.VISIBLE
            val p = Picasso.get()
            when(item.status){
                2->{
                    p.load(R.drawable.ic_check).into(ivStatus)
                }
                3->{
                    p.load(android.R.drawable.ic_menu_close_clear_cancel).into(ivStatus)
                }
                1->{
                    ivStatus.visibility = View.GONE
                }
                else->{
                    ivStatus.visibility = View.GONE
                }
            }
        }
    }
}

open class AlternativeItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val card: MaterialCardView = itemView.findViewById(R.id.card)
    val ivStatus: ImageView = itemView.findViewById(R.id.ivStatus)
    val ivImg: ImageView = itemView.findViewById(R.id.iv_item_image)
    val tvName: TextView = itemView.findViewById(R.id.tv_item_name)
    val tvDesc1: TextView = itemView.findViewById(R.id.tv_item_description_1)
    val tvDesc2: TextView = itemView.findViewById(R.id.tv_item_description_2)
    val btnPriority: Button = itemView.findViewById(R.id.btnPriority)
}