package com.kotlin101.group2.grocerylist.data.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object GroceryApiBuilder {
    val API_BASE_URL = "https://kevkotlin101g2.azurewebsites.net/api/"
//    val API_BASE_URL_LOCAL = "https://10.0.2.2:7122/api/"

    fun getInstance(): GroceryApi {
        val client = OkHttpClient.Builder().connectTimeout(100, TimeUnit.SECONDS).readTimeout(100, TimeUnit.SECONDS).build()
        var api = Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GroceryApi::class.java)
        return api
    }
}