package com.kotlin101.group2.grocerylist.data.db

import android.app.Activity
import com.kotlin101.group2.grocerylist.data.api.GroceryApi
import com.kotlin101.group2.grocerylist.data.api.GroceryApiBuilder
import com.kotlin101.group2.grocerylist.data.api.models.UpdateItemRequest
import com.kotlin101.group2.grocerylist.data.sharedpreference.GroceryAppSharedPreference
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PendingItemUpdateHandler {
    private val activity: Activity
    private val api : GroceryApi
    private val db : GroceryDb
    private val pref : GroceryAppSharedPreference

    constructor(_activity: Activity){
        activity = _activity
        api = GroceryApiBuilder.getInstance()
        db = GroceryDb(_activity)
        pref = GroceryAppSharedPreference(_activity)
    }

    fun processPending(){
        val token = pref.getToken()
        val items = db.allPending()
        GlobalScope.launch {
            items.forEach {
                when(it.status){
                    2->api.markItemAsDone(it.itemId, token!!)
                    3->api.markItemAsNotAvailable(it.itemId, token!!)
                }
                db.deletePending(it.itemId)
            }
        }
    }
}