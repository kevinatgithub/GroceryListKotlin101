package com.kotlin101.group2.grocerylist

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kotlin101.group2.grocerylist.data.api.GroceryApi
import com.kotlin101.group2.grocerylist.data.api.GroceryApiBuilder
import com.kotlin101.group2.grocerylist.data.db.GroceryDb
import com.kotlin101.group2.grocerylist.data.sharedpreference.GroceryAppSharedPreference
import com.kotlin101.group2.grocerylist.databinding.ActivityMenuBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MenuActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMenuBinding
    private lateinit var api: GroceryApi
    private lateinit var db: GroceryDb
    private lateinit var pref: GroceryAppSharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        api = GroceryApiBuilder.getInstance()
        db = GroceryDb(this)
        pref = GroceryAppSharedPreference.getInstance(this)

        with(binding){

            layoutHeader.ivBack.setOnClickListener {
                startActivity(Intent(this@MenuActivity, MainActivity::class.java))
                finish()
            }

            layoutHeader.tvTitle.text = "Grocery List"

            ivClear.setOnClickListener {
                if (GroceryAppHelpers.checkForInternet(this@MenuActivity)) {
                    confirmClear()
                }else{
                    Toast.makeText(this@MenuActivity,"Unable to clear list while offline", Toast.LENGTH_LONG).show()
                }
            }
            tvClear.setOnClickListener {
                if (GroceryAppHelpers.checkForInternet(this@MenuActivity)) {
                    confirmClear()
                }else{
                    Toast.makeText(this@MenuActivity,"Unable to clear list while offline", Toast.LENGTH_LONG).show()
                }
            }
            ivContacts.setOnClickListener {
                if (GroceryAppHelpers.checkForInternet(this@MenuActivity)){
                    startActivity(Intent(this@MenuActivity, ContactsActivity::class.java))
                    finish()
                }else{
                    Toast.makeText(this@MenuActivity,"Unable to view contacts while offline", Toast.LENGTH_LONG).show()
                }
            }
            tvContacts.setOnClickListener {
                if (GroceryAppHelpers.checkForInternet(this@MenuActivity)){
                    startActivity(Intent(this@MenuActivity, ContactsActivity::class.java))
                    finish()
                }else{
                    Toast.makeText(this@MenuActivity,"Unable to view contacts while offline", Toast.LENGTH_LONG).show()
                }
            }
            ivSignout.setOnClickListener {
                confirmSignOut()
            }
            tvSignout.setOnClickListener {
                confirmSignOut()
            }
        }
    }



    private fun confirmClear() {
        AlertDialog.Builder(this).apply {
            title = "Clear List"
            setMessage("Are you sure you wan't to clear all items in the list?")
            setPositiveButton(android.R.string.yes){ dialog, which ->
                GlobalScope.launch {
                    api.deleteCart(pref.getToken().toString())
                    db.clearCart(pref.getUser().cartId)
                    withContext(Dispatchers.Main){
                        startActivity(Intent(this@MenuActivity, MainActivity::class.java))
                        finish()
                    }
                }
            }
            setNegativeButton(android.R.string.no){ dialog, which ->
                dialog.dismiss()
            }
        }.show()
    }

    private fun confirmSignOut() {
        AlertDialog.Builder(this).apply {
            title = "Sign out"
            setMessage("Are you sure you wan't to sign out? Signing back in to the app will require internet connection.")
            setPositiveButton(android.R.string.yes){ dialog, which ->
                pref.setToken("")
                startActivity(Intent(this@MenuActivity, SignInActivity::class.java))
                finish()
            }
            setNegativeButton(android.R.string.no){ dialog, which ->
                dialog.dismiss()
            }
        }.show()
    }
}