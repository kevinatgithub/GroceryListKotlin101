package com.kotlin101.group2.grocerylist

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kotlin101.group2.grocerylist.adapters.ItemListAdapter
import com.kotlin101.group2.grocerylist.data.api.GroceryApi
import com.kotlin101.group2.grocerylist.data.api.GroceryApiBuilder
import com.kotlin101.group2.grocerylist.data.api.models.Item
import com.kotlin101.group2.grocerylist.data.db.GroceryDb
import com.kotlin101.group2.grocerylist.data.db.LocalItem
import com.kotlin101.group2.grocerylist.data.sharedpreference.GroceryAppSharedPreference
import com.kotlin101.group2.grocerylist.databinding.ActivityShowItemBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShowItemActivity : AppCompatActivity() {
    private lateinit var binding : ActivityShowItemBinding
    private lateinit var api : GroceryApi
    private lateinit var db : GroceryDb
    private lateinit var pref : GroceryAppSharedPreference
    private lateinit var item : LocalItem

    companion object{
        val ITEM_ID : String = "ITEM_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        api = GroceryApiBuilder.getInstance()
        db = GroceryDb(this)
        pref = GroceryAppSharedPreference.getInstance(this)

        val itemId = intent.getIntExtra(ITEM_ID, 0)
        item = db.all(pref.getUser().cartId, "id == $itemId")[0]

        val items = db.all(pref.getUser().cartId)
        val trans : (LocalItem) -> Item = {GroceryDb.dbToApi(it)}
        val adapter = ItemListAdapter(items.map { trans(it) }){
            showAlternative(it)
        }
        with(binding){
            with(pageHeader){
                tvPageTitle.text = "Show Item"
                ivBack.setOnClickListener {
                    startActivity(Intent(this@ShowItemActivity,MainActivity::class.java))
                    finish()
                }
            }
            rvAlternatives.layoutManager = LinearLayoutManager(this@ShowItemActivity)
            rvAlternatives.adapter = adapter

            if (!item.imgUrl!!.isEmpty()){
                Picasso.get().load(item.imgUrl).into(ivItemImage)
                ivItemImage.setOnClickListener {
                    val intent = Intent(this@ShowItemActivity, ViewImageActivity::class.java)
                    intent.putExtra(ViewImageActivity.URL,item.imgUrl)
                    intent.putExtra(ViewImageActivity.SHOW_BUTTON,false)
                    startActivity(intent)
                }
            }

            tvItemName.text = item.name
            tvItemDescription1.text = item.description
            tvItemDescription2.text = "P ${GroceryAppHelpers.applyText(item.totalPrice)} | P ${
                GroceryAppHelpers.applyText(
                    item.pricePerUnit
                )
            } per unit x ${GroceryAppHelpers.applyText(item.quantity)} pcs"
            btnPositive.setOnClickListener {
                confirmAction("Mark as Done", "Do you want to mark this item as done?", {
                    markAs(ITEM_ACTION.DONE)
                }){

                }
            }
            btnNegative.setOnClickListener {
                confirmAction("Mark as Not Available","Is this item not available?",{
                    markAs(ITEM_ACTION.NOT_AVAILABLE)
                }){

                }
            }
            btnAlternative.setOnClickListener {

            }
            btnDelete.setOnClickListener {
                confirmAction("Remove Item","Are you sure you want to remove the item from the list?",{
                    markAs(ITEM_ACTION.REMOVE)
                }){

                }
            }
            btnUpdate.setOnClickListener {
                markAs(ITEM_ACTION.UPDATE)
            }
        }
    }

    private enum class ITEM_ACTION{
        DONE,NOT_AVAILABLE,REMOVE,UPDATE
    }

    private fun markAs(action:ITEM_ACTION) {
        val token = pref.getToken()
        GlobalScope.launch {
            when (action) {
                ITEM_ACTION.DONE -> {
                    api.markItemAsDone(item.id,token!!)
                    val apiItem = GroceryDb.dbToApi(item)
                    apiItem.status = 2
                    db.update(GroceryDb.apiToDb(apiItem))
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@ShowItemActivity,"Item marked as done!",Toast.LENGTH_LONG).show()
                        startActivity(Intent(this@ShowItemActivity, MainActivity::class.java))
                        finish()
                    }
                }
                ITEM_ACTION.NOT_AVAILABLE -> {
                    api.markItemAsNotAvailable(item.id,token!!)
                    val apiItem = GroceryDb.dbToApi(item)
                    apiItem.status = 3
                    db.update(GroceryDb.apiToDb(apiItem))
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@ShowItemActivity,"Item marked as not available!",Toast.LENGTH_LONG).show()
                        startActivity(Intent(this@ShowItemActivity, MainActivity::class.java))
                        finish()
                    }
                }
                ITEM_ACTION.UPDATE -> {
                    withContext(Dispatchers.Main){
                        val i = Intent(this@ShowItemActivity, UpdateItemActivity::class.java)
                        i.putExtra(UpdateItemActivity.ITEM_ID, item.id)
                        startActivity(i)
                        finish()
                    }
                }
                ITEM_ACTION.REMOVE -> {
                    api.deleteItem(item.id,token!!)
                    db.delete(item)
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@ShowItemActivity,"Item has been removed from the list!",Toast.LENGTH_LONG).show()
                        startActivity(Intent(this@ShowItemActivity, MainActivity::class.java))
                        finish()
                    }
                }
            }
        }

    }

    private fun showAlternative(itemId: Int) {

    }

    private fun confirmAction(_title:String,_msg:String, positive:() -> Unit, negative:() -> Unit){
        AlertDialog.Builder(this).apply {
            setTitle(_title)
            setMessage(_msg)
            setPositiveButton(android.R.string.yes){ dialog, which ->
                positive()
            }
            setNegativeButton(android.R.string.no){ dialog, which ->
                negative()
            }
        }.show()
    }
}