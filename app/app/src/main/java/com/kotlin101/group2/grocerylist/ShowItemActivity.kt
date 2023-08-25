package com.kotlin101.group2.grocerylist

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kotlin101.group2.grocerylist.adapters.AlternativeItemListAdapter
import com.kotlin101.group2.grocerylist.adapters.ItemListAdapter
import com.kotlin101.group2.grocerylist.data.api.GroceryApi
import com.kotlin101.group2.grocerylist.data.api.GroceryApiBuilder
import com.kotlin101.group2.grocerylist.data.api.models.Item
import com.kotlin101.group2.grocerylist.data.db.GroceryDb
import com.kotlin101.group2.grocerylist.data.db.LocalItem
import com.kotlin101.group2.grocerylist.data.db.PendingItemUpdate
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

        val items = db.all(pref.getUser().cartId, "alternativeItemId == $itemId and id != $itemId")
        val trans : (LocalItem) -> Item = {GroceryDb.dbToApi(it)}
        val adapter = AlternativeItemListAdapter(items.map { trans(it) }){
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

            rvAlternatives.layoutManager = LinearLayoutManager(this@ShowItemActivity, LinearLayoutManager.HORIZONTAL, false)
            rvAlternatives.adapter = adapter

            btnAddAlternative.setOnClickListener {
                startAddAlternative()
            }

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

            btnDelete.setOnClickListener {
                if (!GroceryAppHelpers.checkForInternet(this@ShowItemActivity)){
                    Toast.makeText(this@ShowItemActivity, "Can't remove item while offline.", Toast.LENGTH_LONG).show()
                }else{
                    confirmAction("Remove Item","Are you sure you want to remove the item from the list?",{
                        markAs(ITEM_ACTION.REMOVE)
                    }){

                    }
                }
            }
            btnUpdate.setOnClickListener {
                if (!GroceryAppHelpers.checkForInternet(this@ShowItemActivity)){
                    Toast.makeText(this@ShowItemActivity, "Can't update item while offline.", Toast.LENGTH_LONG).show()
                }else{
                    markAs(ITEM_ACTION.UPDATE)
                }
            }
            btnAddAlternative.setOnClickListener {
                if (!GroceryAppHelpers.checkForInternet(this@ShowItemActivity)){
                    Toast.makeText(this@ShowItemActivity, "Can't add alternative item while offline.", Toast.LENGTH_LONG).show()
                }else{
                    val i = Intent(this@ShowItemActivity, UpdateItemActivity::class.java)
                    i.putExtra(UpdateItemActivity.ALT_ITEM_ID, item.id)
                    startActivity(i)
                }
            }
        }
    }

    private fun startAddAlternative() {
        TODO("Not yet implemented")
    }

    private enum class ITEM_ACTION{
        DONE,NOT_AVAILABLE,REMOVE,UPDATE
    }

    private fun markAs(action:ITEM_ACTION) {
        val token = pref.getToken()
        GlobalScope.launch {
            when (action) {
                ITEM_ACTION.DONE -> {
                    if (!GroceryAppHelpers.checkForInternet(this@ShowItemActivity)){
                        db.upsertPending(PendingItemUpdate().apply {
                            itemId = item.id
                            status = 2
                        })
                    }else{
                        api.markItemAsDone(item.id,token!!)
                    }
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
                    if (!GroceryAppHelpers.checkForInternet(this@ShowItemActivity)){
                        db.upsertPending(PendingItemUpdate().apply {
                            itemId = item.id
                            status = 3
                        })
                    }else{
                        api.markItemAsNotAvailable(item.id,token!!)
                    }
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
                    val apiItem = GroceryDb.dbToApi(item)
                    api.deleteItem(item.id,token!!)
                    db.delete(GroceryDb.apiToDb(apiItem))
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