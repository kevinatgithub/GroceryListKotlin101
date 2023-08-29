package com.kotlin101.group2.grocerylist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.kotlin101.group2.grocerylist.adapters.ItemListAdapter
import com.kotlin101.group2.grocerylist.data.api.GroceryApi
import com.kotlin101.group2.grocerylist.data.api.GroceryApiBuilder
import com.kotlin101.group2.grocerylist.data.api.models.Item
import com.kotlin101.group2.grocerylist.data.db.GroceryDb
import com.kotlin101.group2.grocerylist.data.db.LocalItem
import com.kotlin101.group2.grocerylist.data.db.PendingItemUpdateHandler
import com.kotlin101.group2.grocerylist.data.sharedpreference.GroceryAppSharedPreference
import com.kotlin101.group2.grocerylist.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var api: GroceryApi
    private lateinit var pref: GroceryAppSharedPreference
    private lateinit var db: GroceryDb
    private lateinit var listItems: List<LocalItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        api = GroceryApiBuilder.getInstance()
        pref = GroceryAppSharedPreference.getInstance(this)
        db = GroceryDb(this)

        if (pref.getToken() == null){
            startActivity(Intent(this,SignInActivity::class.java))
            finish()
        }

        val user = pref.getUser()

        with(binding){

            layoutHeader.tvTitle.text = "Item List"
            layoutHeader.ivBack.setImageDrawable(getDrawable(R.drawable.ic_menu))
            layoutHeader.ivBack.setOnClickListener {
                // TODO: Change to open drawer
                startActivity(Intent(this@MainActivity, ContactsActivity::class.java))
                finish()
            }

            tvHelperText.visibility = View.GONE
            pbLoading.visibility = View.VISIBLE
            srlRefresh.visibility = View.GONE
            rvItems.visibility = View.GONE
            rvItems.layoutManager = LinearLayoutManager(this@MainActivity)

            if (user.avatar != null){
                Picasso.get().load(user.avatar).into(ivAvatar)
            }

            ivAvatar.setOnClickListener {
                startActivity(Intent(this@MainActivity, UpdateProfileActivity::class.java))
                finish()
            }

            fabAddItem.setOnClickListener {
                if (!GroceryAppHelpers.checkForInternet(this@MainActivity)){
                    Toast.makeText(this@MainActivity, "Can't connect to the internet, unable to add new items to list while offline.", Toast.LENGTH_LONG).show()
                }else{
                    startActivity(Intent(this@MainActivity, UpdateItemActivity::class.java))
                    finish()
                }
            }

            etSearch.addTextChangedListener {
                performSearch()
            }

            srlRefresh.setOnRefreshListener {
                srlRefresh.isRefreshing = false
                processPending()
                loadItemsList()
            }
        }

        processPending()
        loadItemsList()
    }

    private fun processPending() {
        if (GroceryAppHelpers.checkForInternet(this@MainActivity)) {
            val pending = PendingItemUpdateHandler(this)
            pending.processPending()
        }
    }

    private fun performSearch() {
        val items = listItems.filter{
            it.name.contains(binding.etSearch.text.toString(),true)
        }
        val transform: (LocalItem) -> Item = {GroceryDb.dbToApi(it)}
        val adapter = ItemListAdapter(items.map { transform(it) }){
            gotoShowItem(it)
        }
        binding.rvItems.adapter = adapter
        binding.rvItems.adapter!!.notifyDataSetChanged()
    }

    private fun gotoShowItem(itemId: Int) {
        val i = Intent(this, ShowItemActivity::class.java)
        i.putExtra(ShowItemActivity.ITEM_ID, itemId)
        startActivity(i)
        finish()
    }

    private fun loadItemsList() {
        switchItemListViewState(1)

        binding.srlRefresh.isRefreshing = true

        if (GroceryAppHelpers.checkForInternet(this)){
            GlobalScope.launch {
                val itemsRequest = api.getCartItems(pref.getToken().toString())
                if (itemsRequest.isSuccessful){
                    val items = itemsRequest.body()
                    val transform: (Item) -> LocalItem = {GroceryDb.apiToDb(it)}
                    val primeItems = items?.filter { it.isPrimary == true }
                    listItems = primeItems!!.map { transform(it) }
                    db.clearCart(pref.getUser().cartId)
                    items?.map{ transform(it) }?.forEach { db.add(it) }
                    withContext(Dispatchers.Main){
                        loadItemsToTheList()
                    }
                }
            }
        }else{
            Toast.makeText(this,"Can't connect to the internet, showing offline items.",Toast.LENGTH_LONG).show()
            listItems = db.all(pref.getUser().cartId).sortedBy {
                it.name
            }.filter { it.isPrimary == true }
            loadItemsToTheList()
        }
    }

    private fun loadItemsToTheList() {
        if (listItems.size > 0) {
            switchItemListViewState(2)
            val transform: (LocalItem) -> Item = { GroceryDb.dbToApi(it) }
            val listItems = listItems.map { transform(it) }
            val adapter = ItemListAdapter(listItems) {
                gotoShowItem(it)
            }
            binding.rvItems.adapter = adapter
        } else {
            switchItemListViewState(3)
        }
    }

    private fun switchItemListViewState(state: Int) {
        with (binding){
            when(state){
                1->{
                    srlRefresh.visibility = View.VISIBLE
                    srlRefresh.isRefreshing = true
                    rvItems.visibility = View.GONE
                    pbLoading.visibility = View.GONE
                    tvHelperText.visibility = View.GONE
                }
                2->{
                    srlRefresh.visibility = View.VISIBLE
                    rvItems.visibility = View.VISIBLE
                    srlRefresh.isRefreshing = false
                    pbLoading.visibility = View.GONE
                    tvHelperText.visibility = View.GONE
                }
                3->{
                    srlRefresh.visibility = View.GONE
                    srlRefresh.isRefreshing = false
                    rvItems.visibility = View.GONE
                    pbLoading.visibility = View.GONE
                    tvHelperText.visibility = View.VISIBLE
                }
            }
        }
    }
}