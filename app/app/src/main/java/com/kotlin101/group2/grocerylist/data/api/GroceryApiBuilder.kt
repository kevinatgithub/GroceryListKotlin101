package com.kotlin101.group2.grocerylist.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GroceryApiBuilder {
    val API_BASE_URL = "https://kevkotlin101g2.azurewebsites.net/api/"

    fun getInstance(): GroceryApi {
        var api = Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GroceryApi::class.java)
        return api
    }
}