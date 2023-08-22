package com.kotlin101.group2.grocerylist.data.sharedpreference

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.kotlin101.group2.grocerylist.data.api.models.UserResponse

class GroceryAppSharedPreference {
    private lateinit var preference: SharedPreferences
    private lateinit var gson: Gson

    constructor(activity: Activity){
        preference = PreferenceManager.getDefaultSharedPreferences(activity.applicationContext)
        gson = Gson()
    }

    companion object{
        private val JWT = "JWT"
        private val USER = "USER"
        private val AVATAR = "AVATAR"

        fun getInstance(activity: Activity) : GroceryAppSharedPreference{
            return GroceryAppSharedPreference(activity)
        }
    }

    fun setToken(token:String){
        var editor = preference.edit()
        editor.putString(JWT, token)
        editor.commit()
    }

    fun getToken(): String?{
        var token = preference.getString(JWT, "")
        if (token!!.isEmpty()){
            return null
        }
        return "bearer $token"
    }

    fun setUser(user: UserResponse){
        var editor = preference.edit()
        editor.putString(USER, gson.toJson(user))
        editor.commit()
    }

    fun getUser() : UserResponse{
        var userStr = preference.getString(USER, gson.toJson(UserResponse(0,ArrayList<Any>(),"","")))
        return gson.fromJson(userStr, UserResponse::class.java)
    }

    fun setAvatar(avatar: String){
        var editor = preference.edit()
        editor.putString(AVATAR, avatar)
        editor.commit()
    }

    fun getAvatar() : String?{
        return preference.getString(AVATAR,"")
    }
}