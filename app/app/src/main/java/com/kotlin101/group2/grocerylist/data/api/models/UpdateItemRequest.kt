package com.kotlin101.group2.grocerylist.data.api.models

data class UpdateItemRequest(
    val description: String,
    val name: String,
    val price: Int
)