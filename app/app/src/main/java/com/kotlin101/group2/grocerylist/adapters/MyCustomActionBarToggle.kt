package com.kotlin101.group2.grocerylist.adapters

import android.app.Activity
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout

class MyCustomActionBarToggle(
    activity: Activity?,
    val drawerLayout: DrawerLayout?,
    openDrawerContentDescRes: Int,
    closeDrawerContentDescRes: Int
) : ActionBarDrawerToggle(
    activity,
    drawerLayout,
    openDrawerContentDescRes,
    closeDrawerContentDescRes
) {

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        Log.d("test","test")

        return super.onOptionsItemSelected(item)
    }
}