package com.kotlin101.group2.grocerylist

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.kotlin101.group2.grocerylist.data.api.GroceryApi
import com.kotlin101.group2.grocerylist.data.api.GroceryApiBuilder
import com.kotlin101.group2.grocerylist.data.api.models.CreateItemRequest
import com.kotlin101.group2.grocerylist.data.api.models.TextResponse
import com.kotlin101.group2.grocerylist.data.api.models.UpdateItemRequest
import com.kotlin101.group2.grocerylist.data.db.GroceryDb
import com.kotlin101.group2.grocerylist.data.db.LocalItem
import com.kotlin101.group2.grocerylist.data.sharedpreference.GroceryAppSharedPreference
import com.kotlin101.group2.grocerylist.databinding.ActivityUpdateItemBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher


class UpdateItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateItemBinding
    private lateinit var api : GroceryApi
    private lateinit var db : GroceryDb
    private lateinit var pref : GroceryAppSharedPreference
    private var item : LocalItem? = null
    private var imgUrl : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        api = GroceryApiBuilder.getInstance()
        db = GroceryDb(this)
        pref = GroceryAppSharedPreference.getInstance(this)

        loadItemFromExtra()

        with(binding) {
            layoutAddUpdateItem.ivBack.setOnClickListener {
                startActivity(Intent(this@UpdateItemActivity, MainActivity::class.java))
                finish()
            }

            if (item != null){
                etItemName.setText(item!!.name)
                etItemDetails.setText(item!!.description)
                etItemPrice.setText(item!!.pricePerUnit.toString())
                etQuantity.setText(item!!.quantity)
                photoBox.setOnClickListener {
                    val searchIntent = Intent(this@UpdateItemActivity, ImageSearchActivity::class.java)
                    searchIntent.putExtra(ImageSearchActivity.KEYWORD, item!!.name)
                    startImageSearch.launch(searchIntent)
                }
                if (item!!.imgUrl != null){
                    Picasso.get().load(item!!.imgUrl).into(photoBox)
                }else if (item!!.img != null){
                    val base64Image: String = item!!.img!!.split(",").get(1)
                    val imageAsBytes: ByteArray = Base64.decode(base64Image.toByteArray(), Base64.DEFAULT)
                    photoBox.setImageBitmap(
                        BitmapFactory.decodeByteArray(
                            imageAsBytes,
                            0,
                            imageAsBytes.size
                        )
                    )
                }
            }else{
                photoBox.setOnClickListener {
                    newImageSearch()
                }
            }

            fabSave.setOnClickListener {
                attemptItemSave()
            }
        }
    }

    private fun newImageSearch() {
        if (!binding.etItemName.text!!.isEmpty()){
            val searchIntent = Intent(this,ImageSearchActivity::class.java)
            searchIntent.putExtra(ImageSearchActivity.KEYWORD, binding.etItemName.text!!.toString())
            startImageSearch.launch(searchIntent)
        }
    }

    private fun attemptItemSave() {
        with(binding){
            var errors : Int = 0
            if (etItemName.text!!.isEmpty()){

            }

            if (errors == 0){
                var pricePerUnit : Double = 0.0
                if (!etItemPrice.text!!.isEmpty()){
                    pricePerUnit = etItemPrice.text.toString().toDouble()
                }
                var quantity : Int = 0
                if (!etQuantity.text!!.isEmpty()){
                    quantity = etQuantity.text.toString().toInt()
                }
                GlobalScope.launch {
                    if (item != null){
                        item!!.apply {
                            name = etItemName.text.toString()
                            description = etItemDetails.text.toString()
                            pricePerUnit = pricePerUnit
                            quantity = quantity
                            imgUrl = this@UpdateItemActivity.imgUrl
                        }
                        db.update(item!!)
                        val updateItemRequest = api.updateItem(item!!.id, UpdateItemRequest(item!!.description, "", "", item!!.name, item!!.pricePerUnit, item!!.quantity), pref.getToken().toString())
                        if (updateItemRequest.isSuccessful){
                            withContext(Dispatchers.Main){
                                Toast.makeText(this@UpdateItemActivity,"Item has been updated", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@UpdateItemActivity,MainActivity::class.java))
                                finish()
                            }
                        }
                    } else{
                        val lItem = LocalItem().apply {
                            name = etItemName.text.toString()
                            description = etItemDetails.text.toString()
                            pricePerUnit = pricePerUnit
                            quantity = quantity
                            imgUrl = this@UpdateItemActivity.imgUrl
                        }
                        db.add(lItem)
                        val addItemRequest = api.addItem(CreateItemRequest(0,lItem.description,"",imgUrl!!,lItem.name,lItem.pricePerUnit,lItem.quantity),pref.getToken().toString())
                        if (addItemRequest.isSuccessful){
                            withContext(Dispatchers.Main){
                                Toast.makeText(this@UpdateItemActivity,"Item added to list", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this@UpdateItemActivity,MainActivity::class.java))
                                finish()
                            }
                        }else{
                            val errorResponse = Gson().fromJson(addItemRequest.errorBody()!!.charStream(), TextResponse::class.java)
                            withContext(Dispatchers.Main){
                                Toast.makeText(this@UpdateItemActivity, errorResponse.text,Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun loadItemFromExtra() {
        val itemId = intent.extras?.getInt("ITEM_ID", 0)
        if (itemId != 0) {
            val items = db.all("id == $itemId")
            if (items.size > 0) {
                item = items[0]
            }
        }else{
            item = null
        }
    }

    private val startImageSearch = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            it.data?.let { intent ->
                val url = intent.getStringExtra(ImageSearchActivity.IMAGE_URL)
                imgUrl = url
                Picasso.get().load(url).into(binding.photoBox)
            }
        }
    }
}
