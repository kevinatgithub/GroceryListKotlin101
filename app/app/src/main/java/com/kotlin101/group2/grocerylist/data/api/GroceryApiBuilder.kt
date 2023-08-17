package com.group2.kotlinlab.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GroceryApiBuilder {
    val API_BASE_URL = "https://kevkotlin101g2.azurewebsites.net/api/"

    fun GetInstance(): GroceryApi {
        var api = Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GroceryApi::class.java)
        return api
    }
}