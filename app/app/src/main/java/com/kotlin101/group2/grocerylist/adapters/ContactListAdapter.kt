package com.kotlin101.group2.grocerylist.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.kotlin101.group2.grocerylist.R
import com.kotlin101.group2.grocerylist.ViewImageActivity
import com.kotlin101.group2.grocerylist.data.api.models.User
import com.squareup.picasso.Picasso

class ContactListAdapter(private val context: Context, private val contacts: List<User>) : RecyclerView.Adapter<ContactView>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactView {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.contact_row, parent, false)

        return ContactView(view)
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    override fun onBindViewHolder(holder: ContactView, position: Int) {
        val contact: User = contacts.get(position)
        with(holder){
            tvContactName.text = contact.name
            tvContactEmail.text = contact.email
            if (contact.avatar != null && !contact.avatar.isEmpty()){
                Picasso.get().load(contact.avatar).into(ivPicture)
                ivPicture.setOnClickListener {
                    val i = Intent(context,ViewImageActivity::class.java)
                    i.putExtra(ViewImageActivity.SHOW_BUTTON,false)
                    i.putExtra(ViewImageActivity.URL,contact.avatar)
                    context.startActivity(i)
                }
            }
        }
    }
}

class ContactView(contactRow: View) : ViewHolder(contactRow) {
    val tvContactName: TextView = contactRow.findViewById(R.id.tvContactName)
    val tvContactEmail: TextView = contactRow.findViewById(R.id.tvContactEmail)
    val ivPicture: ImageView = contactRow.findViewById(R.id.ivPicture)
}
