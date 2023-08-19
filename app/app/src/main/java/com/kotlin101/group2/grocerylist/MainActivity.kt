package com.kotlin101.group2.grocerylist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.kotlin101.group2.grocerylist.adapters.ItemListAdapter
import com.kotlin101.group2.grocerylist.data.api.GroceryApi
import com.kotlin101.group2.grocerylist.data.api.GroceryApiBuilder
import com.kotlin101.group2.grocerylist.data.api.models.Item
import com.kotlin101.group2.grocerylist.data.db.GroceryDb
import com.kotlin101.group2.grocerylist.data.db.LocalItem
import com.kotlin101.group2.grocerylist.data.sharedpreference.GroceryAppSharedPreference
import com.kotlin101.group2.grocerylist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var api: GroceryApi
    private lateinit var pref: GroceryAppSharedPreference
    private lateinit var db: GroceryDb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        api = GroceryApiBuilder.GetInstance()
        pref = GroceryAppSharedPreference.getInstance(this)
        db = GroceryDb(this)

        with(binding){
            tvHelperText.visibility = View.GONE
            pbLoading.visibility = View.VISIBLE
            rvItems.visibility = View.GONE
            rvItems.layoutManager = LinearLayoutManager(this@MainActivity)
        }

        loadItemsList()
    }

    private fun loadItemsList() {
        switchItemListViewState(1)
        var items = db.all().sortedBy {
            it.name
        }

        if (items.size > 0){
            switchItemListViewState(2)
            val transform: (LocalItem) -> Item = {GroceryDb.dbToApi(it)}
            val listItems = items.map {transform(it)}
            val adapter = ItemListAdapter(listItems)
            binding.rvItems.adapter = adapter
        }else{
            switchItemListViewState(3)
        }
    }

    private fun switchItemListViewState(state: Int) {
        with (binding){
            when(state){
                1->{
                    rvItems.visibility = View.GONE
                    pbLoading.visibility = View.VISIBLE
                    tvHelperText.visibility = View.GONE
                }
                2->{
                    rvItems.visibility = View.VISIBLE
                    pbLoading.visibility = View.GONE
                    tvHelperText.visibility = View.GONE
                }
                3->{
                    rvItems.visibility = View.GONE
                    pbLoading.visibility = View.GONE
                    tvHelperText.visibility = View.VISIBLE
                }
            }
        }
    }
}