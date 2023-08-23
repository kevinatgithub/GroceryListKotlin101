package com.kotlin101.group2.grocerylist

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kotlin101.group2.grocerylist.adapters.ContactListAdapter
import com.kotlin101.group2.grocerylist.data.api.GroceryApi
import com.kotlin101.group2.grocerylist.data.api.GroceryApiBuilder
import com.kotlin101.group2.grocerylist.data.sharedpreference.GroceryAppSharedPreference
import com.kotlin101.group2.grocerylist.databinding.ActivityContactsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContactsActivity: AppCompatActivity() {
    private lateinit var binding:ActivityContactsBinding
    private lateinit var api:GroceryApi
    private lateinit var pref:GroceryAppSharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        api = GroceryApiBuilder.getInstance()
        pref = GroceryAppSharedPreference.getInstance(this)

        with(binding){
            rvContacts.layoutManager = LinearLayoutManager(this@ContactsActivity)
            layoutHeader.tvTitle.text = "Contacts"
            layoutHeader.ivBack.setOnClickListener {
                startActivity(Intent(this@ContactsActivity, MainActivity::class.java))
                finish()
            }
            fabAddContact.setOnClickListener {
                startActivity(Intent(this@ContactsActivity, ContactSignUpActivity::class.java))
            }
        }

        GlobalScope.launch {
            var users = api.getCartUsers(pref.getToken().toString())
            users.remove(users.first {
                it.email == pref.getUser().email
            })
            withContext(Dispatchers.Main){
                if (users.size == 0){
                    binding.tvNoContacts.visibility = View.VISIBLE
                }else{
                    binding.tvNoContacts.visibility = View.GONE
                    val adapter = ContactListAdapter(users)
                    binding.rvContacts.adapter = adapter
                }
            }
        }
    }
}